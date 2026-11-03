#!/usr/bin/env python3
"""Proximity-voice relay for the Space game server.

The Minecraft plugin never carries audio. It only tells this relay which connected
client should hear which other client, and at what volume:

    &<speaker>&<target>#<volume>&<target>#<volume>...

Speakers and targets are player IP addresses. The plugin only knows the address
of the Minecraft connection, not the source port the voice client opens, so the
relay keys connections by IP. One voice client per IP is supported; a second
connection from the same IP replaces the first. The control channel is a plain
TCP socket: the plugin opens a connection, writes one routing line, and closes
it. Audio itself flows on the audio socket, one connection per player.

Usage::

    python3 server.py --host 0.0.0.0 --audio-port 5000 --control-port 81
"""

from __future__ import annotations

import argparse
import logging
import socket
import socketserver
import threading

import numpy as np

LOG = logging.getLogger("space.voice.server")

SAMPLE_WIDTH = 2  # int16


class VoiceClient(socketserver.BaseRequestHandler):
    """One connected player audio stream."""

    def handle(self) -> None:
        address = self.client_address[0]
        with self.server._lock:
            previous = self.server.clients.get(address)
            self.server.clients[address] = self.request
        if previous is not None and previous is not self.request:
            LOG.info("replacing previous voice client for %s", address)
            try:
                previous.close()
            except OSError:
                pass
        LOG.info("voice client connected %s", address)
        try:
            while True:
                data = self.request.recv(4096)
                if not data:
                    break
                self.server.relay(address, data)
        except OSError as exc:
            LOG.debug("voice client %s dropped: %s", address, exc)
        finally:
            with self.server._lock:
                if self.server.clients.get(address) is self.request:
                    self.server.clients.pop(address, None)
                    self.server.routes.pop(address, None)
            LOG.info("voice client disconnected %s", address)


class AudioServer(socketserver.ThreadingTCPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, address: tuple[str, int]):
        super().__init__(address, VoiceClient)
        self.clients: dict[str, socket.socket] = {}
        self.routes: dict[str, list[tuple[str, int]]] = {}
        self._lock = threading.Lock()

    def set_routes(self, speaker: str, targets: list[tuple[str, int]]) -> None:
        with self._lock:
            self.routes[speaker] = targets

    def relay(self, speaker: str, data: bytes) -> None:
        with self._lock:
            targets = list(self.routes.get(speaker, ()))
        if not targets:
            return
        samples = np.frombuffer(data, dtype=np.int16)
        for address, volume in targets:
            target = self.clients.get(address)
            if target is None:
                continue
            scaled = np.multiply(samples, volume / 100.0).astype(np.int16)
            try:
                target.sendall(scaled.tobytes())
            except OSError as exc:
                LOG.debug("cannot forward audio to %s: %s", address, exc)


class ControlHandler(socketserver.BaseRequestHandler):
    """Receives routing lines from the Minecraft plugin."""

    def handle(self) -> None:
        data = b""
        while True:
            chunk = self.request.recv(4096)
            if not chunk:
                break
            data += chunk
        if not data:
            return
        try:
            message = data.decode("utf-8")
        except UnicodeDecodeError:
            LOG.warning("discarding malformed control message")
            return
        LOG.debug("routing update: %s", message)
        self.server.apply(message)


class ControlServer(socketserver.ThreadingTCPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, address: tuple[str, int], audio: AudioServer):
        super().__init__(address, ControlHandler)
        self.audio = audio

    def apply(self, message: str) -> None:
        message = message.strip()
        if not message.startswith("&"):
            LOG.warning("ignoring control message without speaker: %s", message)
            return
        parts = message[1:].split("&")
        speaker = parts[0]
        targets: list[tuple[str, int]] = []
        for entry in parts[1:]:
            address, _, raw_volume = entry.partition("#")
            if not address or not raw_volume:
                continue
            try:
                volume = max(0, min(100, int(raw_volume)))
            except ValueError:
                continue
            targets.append((address, volume))
        self.audio.set_routes(speaker, targets)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Space proximity-voice relay")
    parser.add_argument("--host", default="0.0.0.0", help="bind address (default: 0.0.0.0)")
    parser.add_argument("--audio-port", type=int, default=5000,
                        help="client audio port (default: 5000)")
    parser.add_argument("--control-port", type=int, default=81,
                        help="plugin control port (default: 81)")
    parser.add_argument("--verbose", action="store_true", help="enable debug logging")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(levelname)-7s %(message)s",
    )

    audio = AudioServer((args.host, args.audio_port))
    control = ControlServer((args.host, args.control_port), audio)

    threading.Thread(target=audio.serve_forever, name="audio", daemon=True).start()
    LOG.info("audio relay listening on %s:%d", args.host, args.audio_port)
    LOG.info("control channel listening on %s:%d", args.host, args.control_port)
    try:
        control.serve_forever()
    except KeyboardInterrupt:
        LOG.info("shutting down")
    finally:
        control.server_close()
        audio.server_close()


if __name__ == "__main__":
    main()
