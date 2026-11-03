package fr.edouwarden.space.commands;

import fr.edouwarden.space.variables.TChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class METACommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player ) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();
            if(item !=null){
                ItemMeta meta = item.getItemMeta();

                //meta.addEnchant(Enchantment.getByName("knock"),15,true);
                // TIER, Danage, Range, Cooldown(1s = 4 ticks), Area, Accuracy, Type, UniqueName
                if (args.length == 8) {
                    meta.setLocalizedName(args[1] + "," + args[2] + "," + args[3] + "," + args[4] + "," + args[5] + "," + args[6] + "," + args[7]);
                    meta.setDisplayName(ChatColor.RED + args[7].toUpperCase());
                }else if (args.length == 9) {
                    meta.setLocalizedName(args[1] + "," + args[2] + "," + args[3] + "," + args[4] + "," + args[5] + "," + args[6] + "," + args[7] + "," + args[8]);
                    meta.setDisplayName(ChatColor.RED + args[8].toUpperCase());
                }else {
                    meta.setLocalizedName("1,0.4,6,0.5,1,100,Katana");
                    meta.setDisplayName(ChatColor.RED + "KATANA");
                }
                //meta.setLore(test);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                //.setCustomTag(key, ItemTagType.DOUBLE, Math.PI);


                item.setItemMeta(meta);
                sender.sendMessage(TChat.done());
            }
        }
        return true;
    }
}
