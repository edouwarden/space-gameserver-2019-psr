package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.fight.weapons.Weapon;
import fr.edouwarden.space.fight.weapons.WeaponsInfo;
import fr.edouwarden.space.jobs.Jobs;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.Powers;
import fr.edouwarden.space.ship.ShipEntity;
import fr.edouwarden.space.voice.Voice;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Click implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public static void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player)
                || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!event.hasItem()) {
            return;
        }
        ItemStack item = event.getItem();
        if (player.isSneaking() && item.hasItemMeta()
                && "Keys".equals(item.getItemMeta().getDisplayName())) {
            return;
        }
        if (playerEntity.isInSpace() && item.getType() == Material.CLOCK
                && playerEntity.getJobDatas().getJob() == Jobs.ASTROGATOR) {
            ShipEntity ship = playerEntity.navigatorOn();
            if (ship != null) {
                ship.map(player);
            }
        }
        if (item.getType() == Material.LEVER) {
            event.setCancelled(true);
            byte currentRange = playerEntity.getVoiceRange();
            byte newRange = Voice.defineRange(currentRange, event.getAction());
            Voice.changeVoiceRange(playerEntity, newRange);
            player.getInventory().getItemInMainHand()
                    .setItemMeta(Voice.volumeItem(newRange));
        } else if (playerEntity.hasPower()
                && (event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(Powers.use(playerEntity));
        } else if (Weapon.playerHoldOne(player)) {
            event.setCancelled(true);
            String[] weaponData = item.getItemMeta().getLocalizedName().split(",");
            if (weaponData.length == 1) {
                return;
            }
            int tier = WeaponsInfo.tier(weaponData);
            if (player.isSneaking() && tier == 5) {
                Weapon.usePower(player, WeaponsInfo.type(weaponData),
                        WeaponsInfo.uniqueName(weaponData), 1);
            }
            if (event.getAction() == Action.LEFT_CLICK_AIR
                    || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Weapon.use(player, weaponData);
            }
        }
    }
}
