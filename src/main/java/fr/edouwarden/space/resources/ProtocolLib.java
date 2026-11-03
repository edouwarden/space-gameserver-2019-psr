package fr.edouwarden.space.resources;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;

public class ProtocolLib {
    public static ProtocolManager protocolManager;

    public static void onLoad() {
        protocolManager = (ProtocolManager) ProtocolLibrary.getProtocolManager();

    }
}
