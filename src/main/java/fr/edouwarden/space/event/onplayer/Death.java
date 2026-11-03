package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.event.server.PlayerKO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class Death implements Listener {
    @EventHandler
    public void playerRespawn(PlayerDeathEvent event){
        PlayerKO.set(event.getEntity());
    }
}
