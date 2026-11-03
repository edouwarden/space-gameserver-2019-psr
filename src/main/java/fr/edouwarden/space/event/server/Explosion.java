package fr.edouwarden.space.event.server;

import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class Explosion implements Listener {
    @EventHandler
    public void onExploded(ExplosionPrimeEvent event){
        event.setFire(false);

    }
    @EventHandler

    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            PlayerEntity player = PlayerEntity.getPlayerEntity(((Player) event.getEntity()).getPlayer());

            if (player.hasPower() && player.getPower().getName().equals("bomb")){
            event.setCancelled(true);
            }
        }
    }

}
