package fr.edouwarden.space.commands;

import fr.edouwarden.space.event.server.PlayerKO;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.commands.CommandError;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class WORLDSCommand  {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if (args.length == 2) {

                ((Player) sender).getPlayer().teleport(new Location(Bukkit.getWorld(args[1]), 0 ,69, 0));
            }
        }

        return true;
    }
}
