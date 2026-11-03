package fr.edouwarden.space.event.server;

import fr.edouwarden.space.fight.Damage;
import fr.edouwarden.space.power.abilities.WeightPower;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    @EventHandler
    public void damage(EntityDamageEvent event){
        if(event.getEntity().getType() == EntityType.PLAYER) {
            PlayerEntity playerEntity = PlayerEntity.getPlayerEntity((Player) event.getEntity());
            boolean isFallDamage = event.getCause() == EntityDamageEvent.DamageCause.FALL;
            // A power protects from fall damage when it reports no falling damage.
            boolean isDFProtected = playerEntity.hasPower() && !playerEntity.getPower().fallingDamage();
            boolean isNotGrabbed = !playerEntity.isGrabbed();
            if (isFallDamage && !isDFProtected && isNotGrabbed) {
                Damage.player(playerEntity, event.getDamage());
            } else if (isFallDamage && isDFProtected) {
                if (playerEntity.getPower().getName().equals("weight")) WeightPower.touchGround(playerEntity.bukkitPL);
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
