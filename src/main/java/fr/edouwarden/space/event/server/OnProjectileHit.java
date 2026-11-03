package fr.edouwarden.space.event.server;

import fr.edouwarden.space.power.abilities.BombPower;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class OnProjectileHit implements Listener {
    @EventHandler

    public static void ProjHit(ProjectileHitEvent event){
        if(event.getEntity().getShooter() instanceof Player){
            BombPower.projectile(event.getEntityType(),event.getHitBlock(),event.getHitEntity());
        }
    }
}
