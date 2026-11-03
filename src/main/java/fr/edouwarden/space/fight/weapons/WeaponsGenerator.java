package fr.edouwarden.space.fight.weapons;

import fr.edouwarden.space.jobs.Craft;
import fr.edouwarden.space.variables.General;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class WeaponsGenerator {

    public static ItemStack getDefineWeapon(String type, int tier){

        Map<String, Object> datas = General.materialConfig.getConfigurationSection("Weapons."+type.toUpperCase()+".T"+ tier).getValues(false);
        if (datas==null||datas.isEmpty()){
            return null;
        }
        String name = (String) datas.get("name");
        double damage = (double) datas.get("damage");
        double range = (double) datas.get("range");
        short cooldown = ((Integer)datas.get("cooldown")).shortValue();
        short area = ((Integer)datas.get("area")).shortValue();
        double accuracy = (double) datas.get("accuracy");
        //weaponItem(type.toUpperCase(),tier,randomizer());

        return weaponItem(name,tier,damage,range,cooldown,area,accuracy,type);
    }
    private static double randomizer (double defaultValue,double multiplier){
        return defaultValue*multiplier;
    }
    public static ItemStack weaponItem(String name,int tier,double damage,double range,short cooldown, short area,double accuracy, String type){
        ItemStack item = Craft.item(name, Material.WOODEN_SWORD,false,null);
        ItemMeta meta = item.getItemMeta();
        meta.setLocalizedName(tier + "," + damage + "," + range + "," + cooldown + "," + area + "," + accuracy + "," + type);
        meta.setDisplayName(ChatColor.RED + type.toUpperCase());
        //meta.addEnchant(Enchantment.getByName("knock"),15,true);
        // TIER, Danage, Range, Cooldown(1s = 4 ticks), Area, Accuracy, Type, UniqueName
        //meta.setLore(test);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        //.setCustomTag(key, ItemTagType.DOUBLE, Math.PI);


        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack uniqueWeaponItem(String name,double damage,double range,int cooldown, int area,double accuracy, String type,String uniqueName){
        ItemStack item = Craft.item(name, Material.WOODEN_SWORD,false,null);
        ItemMeta meta = item.getItemMeta();
        meta.setLocalizedName("5," + damage + "," + range + "," + cooldown + "," + area + "," + accuracy + "," + type+","+uniqueName);
        meta.setDisplayName(ChatColor.RED + uniqueName.toUpperCase());
        //meta.addEnchant(Enchantment.getByName("knock"),15,true);
        // TIER, Danage, Range, Cooldown(1s = 4 ticks), Area, Accuracy, Type, UniqueName
        //meta.setLore(test);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        //.setCustomTag(key, ItemTagType.DOUBLE, Math.PI);


        item.setItemMeta(meta);
        return item;
    }
}
