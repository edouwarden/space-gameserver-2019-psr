package fr.edouwarden.space.gui;

import fr.edouwarden.space.fight.armor.Armor;
import fr.edouwarden.space.power.Powers;
import fr.edouwarden.space.fight.weapons.Weapon;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
    public static boolean addItem(Inventory inv,ItemStack item){
        int slot;
        if (item.getMaxStackSize()>1 && item.getAmount()<item.getMaxStackSize()) {
            slot = stackableItem(item,inv);
            if (slot==-1)slot = emptySlot(item,inv);
            else {
                inv.getItem(slot).setAmount(inv.getItem(slot).getAmount()+item.getAmount());
                return true;
            }
        }else
            slot = emptySlot(item,inv);
        if (slot !=-1) {
            inv.setItem(slot, item);
            return true;
        }
        return false;
    }

    private static int stackableItem(ItemStack item, Inventory inv) {
        for (int i = 27; i <= 35; i++) {
            if(inv.getItem(i) != null && inv.getItem(i).getType() == item.getType() && inv.getItem(i).getAmount()+item.getAmount()<=item.getMaxStackSize())return i;
        }
        return -1;
    }

    public static boolean itemMove(ItemStack item,int slot){

            if (Powers.isAPower(item)) {
                if (10 <= slot && slot <= 26) {
                    return false;
                }
            }else if (Weapon.isAPower(item)) {
                if (9 == slot || 18 <= slot && slot <= 26) {
                    return false;
                }
            } else if (Weapon.isOne(item)) {
               if (slot == 0 ) {
                   return false;
               }
           }else if (Armor.isOne(item)) {
                if (36 <= slot && slot <= 39 ) {
                    return false;
                }
            }
            if(27 <= slot && slot <= 35) {
                return false;
            }
            return true;

            //return false;

    }

    public static int emptySlot(ItemStack item, Inventory inv){
        for (int i = 27; i <= 35; i++) {
            if(inv.getItem(i) == null) return i;
        }
        if (Weapon.isOne(item) && inv.getItem(0)==null)return 0;
        return -1;
    }
}
