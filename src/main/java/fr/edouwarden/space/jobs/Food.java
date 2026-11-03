package fr.edouwarden.space.jobs;

import fr.edouwarden.space.variables.General;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Food {
    Map<String, Object> foodDatas;
    public Food(ItemStack food){
        this.foodDatas = General.foodConfig.getConfigurationSection(food.getItemMeta().getDisplayName()).getValues(true);

    }
    public static boolean isOne(ItemStack food){
        return General.foodConfig.contains(food.getItemMeta().getDisplayName());
    }
    public float heal(){
        return (float) foodDatas.get("heal");
    }
    public float effectAbsorbtion(){
        return (float) foodDatas.get("Effect.absorbtion");
    }
    public boolean raw(){
        return (boolean) foodDatas.get("raw");
    }

    public String getMaterial() {
        return (String) foodDatas.get("material");
    }

    public String getCoocked() {
        return (String) foodDatas.get("coocked");
    }

    public int cookTime() {
        return (int) foodDatas.get("cookTime");
    }
}
