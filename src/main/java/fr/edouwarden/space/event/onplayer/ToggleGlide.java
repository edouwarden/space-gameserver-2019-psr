package fr.edouwarden.space.event.onplayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ToggleGlide implements Listener {
    @EventHandler
    public static void toggleglide(EntityToggleGlideEvent event){
        event.setCancelled(true);
    }
}
