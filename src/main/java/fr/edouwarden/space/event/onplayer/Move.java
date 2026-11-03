package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.crawl.Crawling;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player= event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player) )return;
        if(Crawling.isCrawling(player))Crawling.updateBarrierCeiling(player);
        if(event.getFrom().getBlockX()==event.getTo().getBlockX() && event.getFrom().getBlockY()==event.getTo().getBlockY() && event.getFrom().getBlockZ()==event.getTo().getBlockZ())return;
        Location from = event.getFrom();
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        if (playerEntity.isGrabber() && from.getBlock() != event.getTo().getBlock()){ // keep the leader and the grabbed player in sync
            Location loc = playerEntity.getGrabloc();
            loc.setPitch(player.getLocation().getPitch());
            loc.setYaw(player.getLocation().getYaw());
            playerEntity.getGrab().teleport(loc);
            playerEntity.setGrabloc(from.getBlock().getLocation());
        }
        if(playerEntity.isGrabbed() && playerEntity.getGrabber().getGrabloc().getBlock() != event.getTo().getBlock()) {// keep the grabbed player in place
            Location loc = playerEntity.getGrabber().getGrabloc();
            loc.setPitch(player.getLocation().getPitch());
            loc.setYaw(player.getLocation().getYaw());
            player.teleport(loc);
        }


        else if(player.getGameMode() == GameMode.SURVIVAL && player.getLocation().add(0,1,0).getBlock().getType() == Material.WATER && playerEntity.hasPower() ) //TODO staffmode
            player.setVelocity(player.getVelocity().setY(-1.1).setZ(0).setX(0));// TODO: power-related slow effect
        else if(playerEntity.isKo()) {
            if (playerEntity.getKo() > (int) (System.currentTimeMillis() / 1000)) {
                event.setCancelled(true);
            }else
                playerEntity.setKo(null);
        }
    }
}
