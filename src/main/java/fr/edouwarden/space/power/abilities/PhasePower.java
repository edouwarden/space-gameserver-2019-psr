package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhasePower implements Power {
    public boolean useSkills(PlayerEntity player1) {
        Player player = player1.getBukkitPL();
        Material item = player.getItemInHand().getType();




        if (item == Material.FEATHER) {
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                return true;}
            else{
                PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 100000000, 1, true, true);
                player.addPotionEffect(effect);
                return true;}



        }

    return false;
    }

    public boolean isProtected() {
        return false;
    }


    public String getName() {
        return "phase";
    }
}