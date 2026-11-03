package fr.edouwarden.space.event.server;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.Sleep;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerKO {
    public static void set(Player player){ // TODO: move into PlayerEntity
        if (player.getGameMode() == GameMode.CREATIVE)return;
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 10));
        player.setHealthScale(1);
        player.setHealth(0.2);
        player.setWalkSpeed(0);
        PlayerEntity.getPlayerEntity(player).setKo((int) (System.currentTimeMillis()/1000)+60);
        player.setCollidable(false);
        player.setInvulnerable(true);
        Sleep.doBedAction(player,true);


    }
    public static void remove(Player player){ // TODO: move into PlayerEntity
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.setWalkSpeed((float) 0.2);
        player.setHealthScale(20);
        player.setHealth(20);
        PlayerEntity.getPlayerEntity(player).setKo(null);
        player.setCollidable(true);
        player.setInvulnerable(false);

    }
    private static Vector rotateAroundAxisY(Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

}
