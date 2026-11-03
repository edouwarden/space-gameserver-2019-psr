package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.voice.Voice;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class HoldItem implements Listener {
    @EventHandler
    public void onPlayerHoldItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        if(player.getInventory().getItem(event.getNewSlot())==null||player.getInventory().getItem(event.getNewSlot()).getType()!= Material.LEVER)return;
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Voice.generateParticleArroundPlayer(player, playerEntity.getVoiceRange());
                if (player.getItemInHand().getType()!= Material.LEVER)
                    this.cancel();
            }
        }.runTaskTimer(General.plugin,5, (long) (General.seconds / 2.5));

    }
}
