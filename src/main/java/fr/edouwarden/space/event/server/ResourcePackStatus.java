package fr.edouwarden.space.event.server;

import fr.edouwarden.space.variables.TChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackStatus implements Listener {
    @EventHandler
    public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent event){
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED)
            event.getPlayer().kickPlayer(TChat.declineTexturePack());



    }
}
