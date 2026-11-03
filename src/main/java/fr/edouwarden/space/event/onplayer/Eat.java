package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.power.Powers;
import fr.edouwarden.space.jobs.Food;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class Eat implements Listener {
    @EventHandler
    public static void playerEat(PlayerItemConsumeEvent event){
        //if (!isFood(event.getItem())) return;
        Player player = event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        ItemStack foodItem = event.getItem();
        PlayerEntity playerEntity =PlayerEntity.getPlayerEntity(player);

        if (!Powers.isOne(foodItem)){
            Powers.addToPlayers(playerEntity, foodItem.getItemMeta().getLocalizedName(),false);
        }else if (General.foodConfig.contains(foodItem.getItemMeta().getDisplayName())){
            Food food = new Food(foodItem);

            player.setHealth(player.getHealth()+ (float)food.heal());
            //if (foodDatas.get("effect.heal"));
            //    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,false));
            //foodDatas.get("effect.resistance");
            //foodDatas.get("effect.resistance");


        }


    }
}