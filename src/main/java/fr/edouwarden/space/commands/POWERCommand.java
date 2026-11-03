package fr.edouwarden.space.commands;

import fr.edouwarden.space.power.Powers;
import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.commands.CommandError;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class POWERCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1].toLowerCase().equals("set") && args.length == 4) {
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime == null) {
                sender.sendMessage(CommandError.playerNotExist());
                return true;
            }
            PlayerEntity victim = PlayerEntity.getPlayerEntity(victime);
            Powers.addToPlayers(victim, args[3], false);
            sender.sendMessage(TChat.done());


        }else if (args[1].toLowerCase().equals("forceset") && args.length == 4) {
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime == null) {
                sender.sendMessage(CommandError.playerNotExist());
                return true;
            }
            PlayerEntity victim = PlayerEntity.getPlayerEntity(victime);
            Powers.addToPlayers(victim, args[3], true);
            sender.sendMessage(TChat.done());


        }else if (args[1].toLowerCase().equals("give") && args.length == 4) {
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime == null) {
                sender.sendMessage(CommandError.playerNotExist());
                return true;
            }
            ItemStack df = new ItemStack(Material.APPLE);
            ItemMeta meta =df.getItemMeta();
            meta.setDisplayName(General.powerDisplayName);
            meta.setLocalizedName(args[3]);
            df.setItemMeta(meta);
            InventoryManager.addItem(victime.getInventory(), df);
            sender.sendMessage(TChat.done());


        }else if (args[1].toLowerCase().equals("remove") && args.length == 2 && sender instanceof Player){
            PlayerEntity victime = PlayerEntity.getPlayerEntity((Player) sender);
            victime.clearPower();
            sender.sendMessage(TChat.done());
        }else if (args[1].toLowerCase().equals("remove") && args.length == 3){
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime == null) {
                sender.sendMessage(CommandError.playerNotExist());
                return true;
            }
            PlayerEntity victim = PlayerEntity.getPlayerEntity(victime);
            victim.clearPower();
            sender.sendMessage(TChat.done());

        }else
            sender.sendMessage(CommandError.dfCommandIncomplete());
        return true;
    }
}
