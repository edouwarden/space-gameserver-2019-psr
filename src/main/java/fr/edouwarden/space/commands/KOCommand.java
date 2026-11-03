package fr.edouwarden.space.commands;

import fr.edouwarden.space.event.server.PlayerKO;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.commands.ArgumentCompletion;
import fr.edouwarden.space.variables.commands.CommandError;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KOCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1].toLowerCase().equals("set") && args.length == 3) {
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime != null){
                PlayerKO.set(victime);
                sender.sendMessage(TChat.done());
            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else if (args[1].toLowerCase().equals("remove") && args.length == 3){
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime !=null) {
                PlayerKO.remove(victime);
                sender.sendMessage(TChat.done());
            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else
            sender.sendMessage(CommandError.koCommandIncomplete());
        return true;
    }

}
