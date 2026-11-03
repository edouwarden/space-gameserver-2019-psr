package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.variables.General;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HasHungerChanges implements Listener {
    @EventHandler
    public void onFoodLeveChange(FoodLevelChangeEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER){
            //Player player = (Player) event.getEntity();
            event.setFoodLevel(General.foodLevel);
        }

    }
}
