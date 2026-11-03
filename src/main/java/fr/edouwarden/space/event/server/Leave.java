package fr.edouwarden.space.event.server;

import com.sk89q.worldedit.MaxChangedBlocksException;
import fr.edouwarden.space.data.Data;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class Leave implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException, MaxChangedBlocksException {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        if(playerEntity==null)return;
        if( playerEntity.haveShip())playerEntity.getOwnedShip().remove();
        Data.unLinkAccountData(playerEntity);
        // TODO: handle disconnect during combat
    }
}
