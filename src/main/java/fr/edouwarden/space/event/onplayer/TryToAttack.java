package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.fight.weapons.Weapon;
import fr.edouwarden.space.fight.weapons.WeaponsInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TryToAttack implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public static void attack(EntityDamageByEntityEvent event) {
        event.setCancelled(true);



        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        player.hasCooldown(Material.WOODEN_SWORD);
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player) || !Weapon.playerHoldOne(player)|| player.hasCooldown(player.getItemInHand().getType()))return;
        String[] weapondata = player.getItemInHand().getItemMeta().getLocalizedName().split(",");

        int tier = WeaponsInfo.tier(weapondata);
        if (player.isSneaking() && tier == 5)
            Weapon.usePower(player, WeaponsInfo.type(weapondata), WeaponsInfo.uniqueName(weapondata), 1);
        else {
            if(!player.hasCooldown(player.getItemInHand().getType())){
                Weapon.use(player, weapondata, event.getEntity());
            }
        }

    }
}
