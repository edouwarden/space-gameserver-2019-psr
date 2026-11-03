package fr.edouwarden.space.event.onplayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem implements Listener {
    @EventHandler
    public void playerDropItem(PlayerDropItemEvent event){
        event.setCancelled(true);
    }
}
