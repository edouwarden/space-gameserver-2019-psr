package fr.edouwarden.space.event.onplayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegainHealth implements Listener {
    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN)
            event.setCancelled(true);
    }
}
