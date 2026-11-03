package fr.edouwarden.space.event.server;

import fr.edouwarden.space.data.Data;
import fr.edouwarden.space.resources.NameTags;
import fr.edouwarden.space.variables.General;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class Join implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(PlayerJoinEvent event) {
        Data.linkAccountData(event.getPlayer());
    }

    @EventHandler
    public void playerLogin(fr.xephi.authme.events.LoginEvent event) {
        Player player = event.getPlayer();
        player.setFoodLevel(General.foodLevel);
        if (!General.resourcePackPath.isEmpty()) {
            try {
                player.setResourcePack(General.resourcePackPath);
            } catch (IllegalArgumentException e) {
                General.console.log(Level.WARNING,
                        "Invalid resource-pack URL in config.yml", e);
            }
        }
        player.setInvulnerable(false);
        player.setCustomNameVisible(true);
        NameTags.setTag(player);
        player.setDisplayName("");
    }
}
