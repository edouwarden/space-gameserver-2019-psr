package fr.edouwarden.space.fight.armor;

import fr.edouwarden.space.event.onplayer.CraftItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Armor {
    public static boolean isOne(ItemStack item) {
        return IsEquipable(item);
    }
    public static boolean IsEquipable(ItemStack i)
    {
        ItemStack test = new ItemStack(i.getType());

        try
        {
            test.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        }
        catch(IllegalArgumentException e)
        {
            return false;
        }

        return true;
    }
}
