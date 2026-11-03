package fr.edouwarden.space.bounty;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.ImageRenderer;
import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Renders bounty posters onto in-game maps.
 *
 * <p>The frame image is read from {@code bounty.png} in the plugin folder. The avatar
 * is optional and configured through {@code rendering.avatar-url}; the original
 * Discord attachment URL is not shipped.</p>
 */
public class BountyPoster {

    public static void generate(PlayerEntity playerEntity, long bounty) {
        World world = General.frontierBukkitWorld != null
                ? General.frontierBukkitWorld
                : Bukkit.getWorlds().get(0);
        MapView view = Bukkit.createMap(world);
        view.getRenderers().clear();
        try {
            ImageRenderer renderer = new ImageRenderer(General.avatarUrl, bounty);
            view.addRenderer(renderer);
            playerEntity.setLinkedMap((short) view.getId());
            playerEntity.getBukkitPL().sendMessage(ChatColor.AQUA + "[Map] Rendering bounty poster.");
        } catch (IOException e) {
            General.console.log(Level.WARNING, "Cannot render bounty poster", e);
            playerEntity.getBukkitPL().sendMessage(
                    ChatColor.RED + "[Map] The poster frame or avatar could not be loaded.");
        }
    }

    public static void give(Player player, short id) {
        MapView view = Bukkit.getMap(id);
        if (view == null) {
            return;
        }
        ItemStack item = new ItemStack(Material.MAP, 1);
        item.setDurability(id);
        player.getInventory().addItem(item);
        player.sendMessage(ChatColor.AQUA + "[Map] Bounty poster received.");
    }

    public static void update(Player player, short id, long bounty) {
        MapView view = Bukkit.getMap(id);
        if (view == null) {
            return;
        }
        view.getRenderers().clear();
        try {
            view.addRenderer(new ImageRenderer(General.avatarUrl, bounty));
            give(player, id);
        } catch (IOException e) {
            General.console.log(Level.WARNING, "Cannot update bounty poster", e);
            player.sendMessage(ChatColor.RED + "[Map] The poster could not be rendered.");
        }
    }

    public static void get(PlayerEntity playerEntity, Player player) {
        if (playerEntity.haveLinkedMap()) {
            give(playerEntity.getBukkitPL(), playerEntity.getLinkedMap());
        }
    }
}
