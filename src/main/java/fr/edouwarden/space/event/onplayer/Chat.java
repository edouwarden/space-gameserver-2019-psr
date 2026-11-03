package fr.edouwarden.space.event.onplayer;

import fr.xephi.authme.AuthMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.List;

public class Chat implements Listener {
    @EventHandler
    public static void onPlayerSendMsg(PlayerChatEvent event){
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;


        int range = 5;
        if (event.getMessage().startsWith("!")) range = 10;
        else if (event.getMessage().startsWith(".")) range = 5;

        for (Entity entity :player.getNearbyEntities(range, range, range)){
            if(entity instanceof Player)((Player) entity).sendMessage(ChatColor.RED+"["+player.getName().toString()+"]"+ChatColor.AQUA+event.getMessage());
        }
        player.sendMessage(ChatColor.RED+"["+player.getName().toString()+"]"+ChatColor.AQUA+event.getMessage());
    }
}
