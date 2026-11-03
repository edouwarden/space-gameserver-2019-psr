package fr.edouwarden.space.commands;

import fr.edouwarden.space.fight.weapons.WeaponsGenerator;
import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WEAPONCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length != 4) return true;
            InventoryManager.addItem(player.getInventory(), WeaponsGenerator.getDefineWeapon(args[2], Integer.parseInt(args[3].replace("T",""))));
            sender.sendMessage(TChat.done());
        }

        return true;
    }
}
