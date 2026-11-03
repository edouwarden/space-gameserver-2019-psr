package fr.edouwarden.space.event.onplayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawn implements Listener {
    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event){
        event.getPlayer().setFoodLevel(19);
    }
}
