package fr.edouwarden.space.commands;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RPKCommand  {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //sender.sendMessage();
        if (args.length == 4 && args[1].equals("ROLEPLAYKILL") && args[3].equals("yes")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            if(player.isOnline()) {
                PlayerEntity.execRPK(player.getPlayer());
                player.getPlayer().kickPlayer(TChat.RPKinfo());
            }


        }else if (args.length == 3 && args[1].equals("ROLEPLAYKILL")) {
            sender.sendMessage(TChat.confirmRPK());
        }


        return true;
    }
}
