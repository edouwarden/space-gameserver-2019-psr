package fr.edouwarden.space.event.onplayer;


import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class Shift implements Listener {
    @EventHandler
    public static void onSneak(PlayerToggleSneakEvent event){
        Player player= event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        if (playerEntity.isGrabbed() && !playerEntity.cuffed){// TODO: move this next to the interact-entity handling
            player.setWalkSpeed(0.2F);
            playerEntity.getGrabber().setGrab(null);
            playerEntity.setGrabber(null);

        }

        if (player.getInventory().getItem(9)!= null && player.getInventory().getItem(0) != null){//Weapons.isOne(player.getInventory().getItem(0))){
            ItemStack item =  player.getInventory().getItem(0);
            player.getInventory().setItem(0, player.getInventory().getItem(9));
            player.getInventory().setItem(9, item);

        }
    }
}
