package fr.edouwarden.space.commands;

import fr.edouwarden.space.other.RaidStation;
import fr.edouwarden.space.variables.General;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RAIDCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //sender.sendMessage();
        if (args[1].toLowerCase().equals("start") && args.length == 3) {       //OF ID
            General.raidEnable = true;
            if (args.length == 3) RaidStation.startLoop(60*Integer.valueOf(args[2]));
            else RaidStation.startLoop(60*10);
        }else if (args[1].toLowerCase().equals("stop")) {// OF player
            General.raidEnable = false;
        }


        return true;
    }
}
