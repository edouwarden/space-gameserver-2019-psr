package fr.edouwarden.space.commands;

import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LOOPSPEEDCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            General.loopspeed = Long.parseLong(args[1]);
            sender.sendMessage(TChat.done());
        } catch (NumberFormatException e) {
            sender.sendMessage(TChat.Head() + TChat.Error() + "Invalid loop speed: " + args[1]);
        }
        return true;
    }
}
