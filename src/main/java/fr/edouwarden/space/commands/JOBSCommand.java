package fr.edouwarden.space.commands;

import fr.edouwarden.space.jobs.JobAction;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.commands.CommandError;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JOBSCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1].toLowerCase().equals("set") && args.length == 4) {
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime != null){
                JobAction.set(victime,args[3],true);
                sender.sendMessage(TChat.done());

            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else if (args[1].toLowerCase().equals("remove") && args.length == 2 && sender instanceof Player){
            Player victime = (Player) sender;

            JobAction.remove(victime);
            sender.sendMessage(TChat.done());

        }else if (args[1].toLowerCase().equals("remove") && args.length == 3){
            Player victime = Bukkit.getPlayer(args[2]);
            if (victime !=null) {
                JobAction.remove(victime);

                sender.sendMessage(TChat.done());
            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else
            sender.sendMessage(CommandError.jobsCommandIncomplete());
        return true;
    }
}
