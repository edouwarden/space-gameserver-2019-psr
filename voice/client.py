#!/usr/bin/env python3
"""Proximity-voice client for the Space game server.

Records the local microphone and streams raw int16 audio to the relay, while
playing whatever the relay forwards. The relay decides who receives the audio;
this client only knows the relay host and audio port.

Usage::

    python3 client.py --host 127.0.0.1 --port 5000
"""

from __future__ import annotations

import argparse
import queue
import socket
import threading
import tkinter as tk
from tkinter import messagebox

import pyaudio

CHUNK = 2048
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100


class VoiceClient:
    """Socket wrapper plus two worker threads (capture and playback)."""

    def __init__(self, host: str, port: int):
        self.host = host
        self.port = port
        self.socket: socket.socket | None = None
        self.audio = pyaudio.PyAudio()
        self.input = None
        self.output = None
        self.running = threading.Event()
        self.playback: queue.Queue[bytes] = queue.Queue(maxsize=32)

    def connect(self) -> None:
        self.socket = socket.create_connection((self.host, self.port), timeout=5)
        self.socket.settimeout(None)
        self.input = self.audio.open(
            format=FORMAT, channels=CHANNELS, rate=RATE,
            input=True, frames_per_buffer=CHUNK)
        self.output = self.audio.open(
            format=FORMAT, channels=CHANNELS, rate=RATE,
            output=True, frames_per_buffer=CHUNK)
        self.running.set()
        threading.Thread(target=self._capture, name="capture", daemon=True).start()
        threading.Thread(target=self._receive, name="receive", daemon=True).start()
        threading.Thread(target=self._play, name="play", daemon=True).start()

    def close(self) -> None:
        self.running.clear()
        for stream in (self.input, self.output):
            if stream is not None:
                try:
                    stream.stop_stream()
                    stream.close()
                except OSError:
                    pass
        self.input = self.output = None
        if self.socket is not None:
            try:
                self.socket.close()
            except OSError:
                pass
            self.socket = None

    def _capture(self) -> None:
        while self.running.is_set():
            try:
                self.socket.sendall(self.input.read(CHUNK, exception_on_overflow=False))
            except OSError:
                break

    def _receive(self) -> None:
        while self.running.is_set():
            try:
                data = self.socket.recv(CHUNK)
            except OSError:
                break
            if not data:
                break
            try:
                self.playback.put_nowait(data)
            except queue.Full:
                pass

    def _play(self) -> None:
        while self.running.is_set():
            try:
                data = self.playback.get(timeout=0.5)
            except queue.Empty:
                continue
            try:
                self.output.write(data)
            except OSError:
                break


class VoiceWindow:
    def __init__(self, root: tk.Tk):
        self.root = root
        self.client: VoiceClient | None = None
        root.title("Space Voice")
        root.resizable(False, False)

        frame = tk.Frame(root, padx=12, pady=12)
        frame.pack()

        tk.Label(frame, text="Relay host").grid(row=0, column=0, sticky="w")
        self.host = tk.Entry(frame, width=20)
        self.host.insert(0, "127.0.0.1")
        self.host.grid(row=0, column=1, padx=6)

        tk.Label(frame, text="Audio port").grid(row=1, column=0, sticky="w")
        self.port = tk.Entry(frame, width=8)
        self.port.insert(0, "5000")
        self.port.grid(row=1, column=1, padx=6, sticky="w")

        self.button = tk.Button(frame, text="Connect", width=14, command=self.toggle)
        self.button.grid(row=2, column=0, columnspan=2, pady=(10, 4))

        self.status = tk.Label(frame, text="Disconnected", fg="#b00020")
        self.status.grid(row=3, column=0, columnspan=2)

        root.protocol("WM_DELETE_WINDOW", self.on_close)

    def toggle(self) -> None:
        if self.client is not None:
            self.client.close()
            self.client = None
            self.button.config(text="Connect")
            self.status.config(text="Disconnected", fg="#b00020")
            return
        try:
            port = int(self.port.get())
            client = VoiceClient(self.host.get().strip(), port)
            client.connect()
        except (ValueError, OSError) as exc:
            messagebox.showerror("Space Voice", f"Cannot connect:\n{exc}")
            return
        self.client = client
        self.button.config(text="Disconnect")
        self.status.config(text="Connected", fg="#1b5e20")

    def on_close(self) -> None:
        if self.client is not None:
            self.client.close()
        self.root.destroy()


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Space proximity-voice client")
    parser.add_argument("--host", help="relay host (overrides the dialog default)")
    parser.add_argument("--port", type=int, help="relay audio port")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    root = tk.Tk()
    window = VoiceWindow(root)
    if args.host:
        window.host.delete(0, tk.END)
        window.host.insert(0, args.host)
    if args.port:
        window.port.delete(0, tk.END)
        window.port.insert(0, str(args.port))
    root.mainloop()


if __name__ == "__main__":
    main()
