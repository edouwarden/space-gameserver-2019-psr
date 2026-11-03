package fr.edouwarden.space.fight;

import fr.edouwarden.space.event.server.PlayerKO;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class Damage {
    public static void calculator(PlayerEntity attacker, double originDamage, PlayerEntity victim){
        Double damage = originDamage;

        player(victim, damage);
    }
    public static void player(PlayerEntity ocVictim, double damage){
        Player victim = ocVictim.getBukkitPL();

        if (ocVictim.isKo() || !fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(victim)) return;
        if (ocVictim.hasPower()){// TODO: a power should be able to absorb or deflect the hit
        }else {
            if (damage >= victim.getHealth()) {
                PlayerKO.set(victim);
            } else {
                victim.setHealth(victim.getHealth()-damage);
                victim.playEffect(EntityEffect.HURT);
                //victim.damage(damage);
            }
        }
    }


    public static void attack(Entity entity, Double damage) {
        if (entity instanceof Player) {
            Damage.player(PlayerEntity.getPlayerEntity((Player) entity), damage);//TODO custom damaged
        }else if (entity instanceof Animals) {
            ((Animals) entity).damage(damage);
        }else if (entity instanceof Monster) {
            ((Monster) entity).damage(damage);
        }
    }
}
