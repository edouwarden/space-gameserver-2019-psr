package fr.edouwarden.space.commands;

import fr.edouwarden.space.bounty.Bounty;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.commands.CommandError;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BOUNTYCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1].toLowerCase().equals("set") && args.length == 4) {//bounty set player bounty
            Player victim = Bukkit.getPlayer(args[2]);
            if (victim != null){
                UUID senderUUID =null;
                if (sender instanceof Player)
                    senderUUID = ((Player) sender).getPlayer().getUniqueId();

                Bounty.set(PlayerEntity.getPlayerEntity(victim), Long.valueOf(args[3]),senderUUID);
                sender.sendMessage(TChat.done());

            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else if (args[1].toLowerCase().equals("add") && args.length == 4){
            Player victim = Bukkit.getPlayer(args[2]);
            if (victim != null){
                UUID senderUUID =null;
                if (sender instanceof Player)
                    senderUUID = ((Player) sender).getPlayer().getUniqueId();

                Bounty.add(PlayerEntity.getPlayerEntity(victim), Long.valueOf(args[3]),senderUUID);
                sender.sendMessage(TChat.done());

            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else if (args[1].toLowerCase().equals("take") && args.length == 4){
            Player victim = Bukkit.getPlayer(args[2]);
            if (victim != null){
                UUID senderUUID =null;
                if (sender instanceof Player)
                    senderUUID = ((Player) sender).getPlayer().getUniqueId();

                Bounty.take(PlayerEntity.getPlayerEntity(victim), Long.valueOf(args[3]),senderUUID);
                sender.sendMessage(TChat.done());

            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else if (args[1].toLowerCase().equals("remove") && args.length == 3){
            Player victim = Bukkit.getPlayer(args[2]);
            if (victim != null){
                UUID senderUUID =null;
                if (sender instanceof Player)
                    senderUUID = ((Player) sender).getPlayer().getUniqueId();

                Bounty.remove(PlayerEntity.getPlayerEntity(victim),senderUUID);
                sender.sendMessage(TChat.done());

            }else
                sender.sendMessage(CommandError.playerNotExist());
        }else
            sender.sendMessage(CommandError.dfCommandIncomplete());
        return true;
    }
}
