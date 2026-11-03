package fr.edouwarden.space.jobs;

import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Craft {
    public static void setup(){
        //setAll();
        YamlConfiguration yml = General.materialConfig;

        for (String key : yml.getKeys(true)) {
            try {

                if (!key.startsWith("Craft.") || !key.endsWith(".type")) continue;

                String type = yml.getString(key);
                key = key.substring(0, key.length() - ".type".length());
                ItemStack result = yml.getItemStack(key.replace("Craft","item"));
                switch (type) {
                    case "Anvil":
                        String keyItem1 = key + ".Anvil.Item1";
                        String keyItem2 = key + ".Anvil.Item2";
                        ItemStack item1;
                        ItemStack item2;
                        if (yml.getBoolean(keyItem1 + ".custom"))
                            item1 = yml.getItemStack("item." + yml.getString(keyItem1 + ".type"));
                        else item1 = new ItemStack(Material.getMaterial(yml.getString(keyItem1 + ".type")));
                        if (yml.getBoolean(keyItem2 + ".custom"))
                            item2 = yml.getItemStack("item." + yml.getString(keyItem2 + ".type"));
                        else item2 = new ItemStack(Material.getMaterial(yml.getString(keyItem2 + ".type")));
                        AnvilCraft.addCraft(result, item1, item2);
                        break;
                    case "Table":

                        String keyItem = "";
                        key = key + ".Table.";
                        List<MaterialData> items = new ArrayList<>();
                        MaterialData item;
                        for (int y = 1; y <= 3; y++) {
                            for (int x = 1; x <= 3; x++) {
                                keyItem = key + "l" + x + "r" + y;
                                if (!yml.contains(keyItem)) item = null;
                                else if (yml.getBoolean(keyItem + ".custom"))
                                    item = yml.getItemStack(yml.getString(keyItem + ".type") + ".item").getData();
                                else item = new MaterialData(Material.getMaterial(yml.getString(keyItem + ".type")));
                                if (item==null) item = new MaterialData(Material.AIR);
                                items.add(item);
                            }
                        }
                        addCraftingList(result, items);
                        break;
                    case "Furnace":
                        String top = key + ".Furnace.top";
                        String bottom = key + ".Furnace.bottom";
                        ItemStack topItem;
                        ItemStack bottomItem;
                        if (yml.getBoolean(top + ".custom"))
                            topItem = yml.getItemStack(yml.getString(top + ".type") + ".item");
                        else topItem = new ItemStack(Material.getMaterial(yml.getString(top + ".type")));
                        if (yml.getBoolean(bottom + ".custom"))
                            bottomItem = yml.getItemStack(yml.getString(bottom + ".type") + ".item");
                        else bottomItem = new ItemStack(Material.getMaterial(yml.getString(bottom + ".type")));
                        addMeltingCraft(result, topItem.getData(), bottomItem.getData());

                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //String type = key.split(".")[1];
                //Integer tier = Integer.valueOf(key.split(".")[2].replace("T",""));




        }
        General.console.info("Loaded " + AnvilCraft.craft.size() + " fabrication recipe(s).");
    }
    public static void setAll(){
        Map<String, Object> datas = General.materialConfig.getConfigurationSection("Weapons").getValues(false);
    }
    public static void addCrafting(ItemStack result, MaterialData l1r1, MaterialData l2r1, MaterialData l3r1, MaterialData l1r2, MaterialData l2r2, MaterialData l3r2, MaterialData l1r3, MaterialData l2r3, MaterialData l3r3){
        NamespacedKey key = new NamespacedKey(General.plugin, General.plugin.getDescription().getName());
        try {
            ShapedRecipe recipe = new ShapedRecipe(key, result);
            recipe.shape("ABC", "DEF", "GHI");
            if (l1r1!=null) recipe.setIngredient('A', l1r1);
            if (l1r2!=null) recipe.setIngredient('B', l1r2);
            if (l1r3!=null) recipe.setIngredient('C', l1r3);
            if (l2r1!=null) recipe.setIngredient('D', l2r1);
            if (l2r2!=null) recipe.setIngredient('E', l2r2);
            if (l2r3!=null) recipe.setIngredient('F', l2r3);
            if (l3r1!=null) recipe.setIngredient('G', l3r1);
            if (l3r2!=null) recipe.setIngredient('H', l3r2);
            if (l3r3!=null) recipe.setIngredient('I', l3r3);

            Bukkit.getServer().addRecipe(recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void addMeltingCraft(ItemStack result, MaterialData top, MaterialData bottom){
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(result, top);
        furnaceRecipe.setCookingTime(10);
        Bukkit.addRecipe(furnaceRecipe);

    }
    public static void addCraftingList(ItemStack result,List<MaterialData> items){
        addCrafting(result,items.get(0),items.get(1),items.get(2),items.get(3),items.get(4),items.get(5),items.get(6),items.get(7),items.get(8));
    }
    public static ItemStack item(String Name, Material material,Boolean enchanted, List<String> description){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.setLore(description);
        if(enchanted)meta.addEnchant(Enchantment.LURE,1,true);

        item.setItemMeta(meta);
        return item;
    }
}
