package fr.edouwarden.space.commands;

import fr.edouwarden.space.bounty.BountyPoster;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MAPCommand {
    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
            if (args[1].toLowerCase().equals("give") && args.length == 3) {       //OF ID

                BountyPoster.give(player, Short.valueOf(args[2]));
            }else if (args[1].toLowerCase().equals("get") && args.length == 3) {// OF player
                BountyPoster.get(playerEntity, Bukkit.getServer().getPlayer((String) args[2]));
            }
            else if (args[1].toLowerCase().equals("update") && args.length == 4) {
                BountyPoster.update(player, Short.valueOf(args[2]),Long.valueOf(args[3]));
            }else if (args[1].toLowerCase().equals("generate") && args.length == 2) {
                BountyPoster.generate(playerEntity, 100L);
            }else if (args[1].toLowerCase().equals("info") && args.length == 2) {
            }
        }
        return true;
    }
}
