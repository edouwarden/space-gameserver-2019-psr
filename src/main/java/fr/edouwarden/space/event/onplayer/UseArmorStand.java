package fr.edouwarden.space.event.onplayer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class UseArmorStand implements Listener {
    @EventHandler
    public void clickOnArmorstand(PlayerArmorStandManipulateEvent event)  {
        Player player = event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player)) event.setCancelled(true);
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
        }
    }
}
