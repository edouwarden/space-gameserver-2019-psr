package fr.edouwarden.space.event.onplayer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlock implements Listener {
    @EventHandler
    public void blockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        if(player.getGameMode() == GameMode.CREATIVE) return;
        else event.setCancelled(true);
    }
}
