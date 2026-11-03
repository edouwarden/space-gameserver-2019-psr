package fr.edouwarden.space.jobs;

import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.edouwarden.space.resources.ArmorStand.addItem;

public class AnvilCraft {
    public static List<List> craft = new ArrayList<>();
    public static HashMap<Block, HashMap<String, Object>> anvils = new HashMap<>();

    public static void oncraft(Block anvil, Player player){
        ItemStack itemInHand = player.getItemInHand();
        HashMap<String, Object> datas = anvils.get(anvil);
        if (datas==null)datas = new HashMap<>();
        ItemStack item1 = (ItemStack) datas.get("item1");
        ItemStack item2 = (ItemStack) datas.get("item2");
        boolean hammer = itemInHand != null
                && itemInHand.getType() == Material.STICK
                && itemInHand.hasItemMeta()
                && itemInHand.getItemMeta().hasDisplayName()
                && itemInHand.getItemMeta().getDisplayName().contains("Fabrication Hammer");
        if(hammer){
            if (player.isSneaking()){
                ItemStack taken = item2 != null ? item2 : item1;
                int slot = item2 != null ? 2 : 1;
                if (taken != null && InventoryManager.addItem(player.getInventory(), taken))
                    removeItem(anvil, slot, datas);
            }
            else if(item1 ==null || item2 == null)player.sendMessage(TChat.cannotWork());//TODO change
            else {
                //TODO craft
                 ItemStack craft = getCorrespondingCraft(item1,item2);
                if (craft!=null){
                    InventoryManager.addItem(player.getInventory(), craft);
                    player.getWorld().spawnParticle(org.bukkit.Particle.CRIT_MAGIC,
                            anvil.getLocation().add(0.5D, 1.1D, 0.5D), 28, 0.35D, 0.35D, 0.35D, 0.0D);
                    player.getWorld().spawnParticle(org.bukkit.Particle.END_ROD,
                            anvil.getLocation().add(0.5D, 1.1D, 0.5D), 12, 0.2D, 0.2D, 0.2D, 0.0D);
                    player.getWorld().playSound(anvil.getLocation(), org.bukkit.Sound.BLOCK_ANVIL_USE, 1.0F, 1.35F);
                    clear(anvil);
                }

            }
        } else if(item1 ==null ){
            placeItem(anvil,itemInHand, datas,1);
            takeFromHand(player);
        }else if(item2 ==null){
            placeItem(anvil,itemInHand, datas,2);
            takeFromHand(player);
        }

    }

    private static void clear(Block anvil) {
        HashMap<String, Object> datas = anvils.remove(anvil);
        if (datas == null) return;
        for (String key : new String[]{"as1", "as2"}) {
            Object stand = datas.get(key);
            if (stand instanceof ArmorStand) ((ArmorStand) stand).remove();
        }
    }

    private static void removeItem(Block anvil, int i, HashMap<String, Object> datas) {
        ArmorStand as = (ArmorStand) datas.get("as" + i);
        if (as != null) as.remove();

        datas.remove("item" + i);
        datas.remove("as" + i);
        anvils.put(anvil, datas);
    }

    private static void placeItem(Block anvil, ItemStack item, HashMap<String, Object> datas, int i){
        // Offset the two display items so both ingredients stay visible on the
        // block instead of one hiding the other.
        Location loc = anvil.getLocation().add(i == 1 ? 0.25 : 0.75, 0.8, 0.3);
        // Face the display items toward the player side of the bench so the
        // ingredient models read clearly from the front.
        loc.setYaw(0F);
        // addItem takes radians; a shallow arm keeps flat item models (ingots)
        // facing the bench instead of turning edge-on.
        ArmorStand as = addItem(loc, item.getType(), true, (float) Math.toRadians(-0.3));
        datas.put("item"+String.valueOf(i), item.clone());
        datas.put("as"+String.valueOf(i), as);
        anvils.put(anvil, datas);
    }

    private static void takeFromHand(Player player) {
        ItemStack hand = player.getItemInHand();
        if (hand == null) return;
        if (hand.getAmount() <= 1) player.setItemInHand(null);
        else {
            hand.setAmount(hand.getAmount() - 1);
            player.setItemInHand(hand);
        }
    }

    private static ItemStack getCorrespondingCraft(ItemStack item1, ItemStack item2) {
        for (List entry : craft) {
            ItemStack result = (ItemStack) entry.get(0);
            ItemStack first = (ItemStack) entry.get(1);
            ItemStack second = (ItemStack) entry.get(2);
            if (first == null || second == null || result == null) continue;
            if (matches(first, item1) && matches(second, item2)
                    || matches(first, item2) && matches(second, item1)) {
                return result.clone();
            }
        }
        return null;
    }

    private static boolean matches(ItemStack expected, ItemStack actual) {
        if (actual == null || expected.getType() != actual.getType()) return false;
        String name = expected.getItemMeta() == null ? null : expected.getItemMeta().getDisplayName();
        if (name == null || name.isEmpty()) return true;
        return actual.getItemMeta() != null && name.equals(actual.getItemMeta().getDisplayName());
    }

    public static void addCraft(ItemStack result,ItemStack item1, ItemStack item2){
        List<ItemStack> list = new ArrayList<>();
        list.add(result);
        list.add(item1);
        list.add(item2);
        craft.add(list);
    }
}
