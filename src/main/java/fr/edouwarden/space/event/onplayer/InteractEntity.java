package fr.edouwarden.space.event.onplayer;

import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractEntity implements Listener {
    @EventHandler
    public void grabAction(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        if (event.getRightClicked() instanceof Player && (player.getItemInHand().getType() == Material.CARROT || player.getItemInHand().getType() == Material.BAKED_POTATO)){
            Player target = (Player) event.getRightClicked();
            PlayerEntity ocTarget = PlayerEntity.getPlayerEntity(target);
            if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(target))return;
            cuff(player,ocTarget);
            if (unCatch(player,ocTarget))return;
            grab(PlayerEntity.getPlayerEntity(player), ocTarget);
        }
    }
    public static void grab(PlayerEntity playerEntity,PlayerEntity ocTarget){
        Player player = playerEntity.getBukkitPL();
        Player target = ocTarget.getBukkitPL();
        playerEntity.getBukkitPL().setWalkSpeed(0.15F);
        ocTarget.getBukkitPL().setWalkSpeed(0.05F);
        ocTarget.setGrabber(playerEntity);
        playerEntity.setGrab(target);
        playerEntity.setGrabloc(player.getLocation());
        target.teleport(player.getLocation());

    }
    public static void cuff(Player player,PlayerEntity ocTarget){
        if(player.getItemInHand().getType() == Material.BAKED_POTATO // the guard applies the handcuffs
            && !player.isSneaking()) // not sneaking: sneaking releases the target
                ocTarget.cuffed = true;
    }
    public boolean unCatch(Player player,PlayerEntity ocTarget){
        if (player.isSneaking() // the grabber sneaks to release
            && ocTarget.getGrabber() != null
            && ocTarget.getGrabber().getBukkitPL().getUniqueId().equals(player.getUniqueId())) {
            PlayerEntity grabber = ocTarget.getGrabber();
            ocTarget.cuffed = false;
            ocTarget.setGrabber(null);
            grabber.setGrab(null);
            player.setWalkSpeed(0.2F);
            ocTarget.getBukkitPL().setWalkSpeed(0.2F);
            return true;
        }
        return false;
    }
}
