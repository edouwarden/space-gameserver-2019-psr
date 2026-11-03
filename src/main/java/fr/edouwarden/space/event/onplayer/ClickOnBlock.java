package fr.edouwarden.space.event.onplayer;

import com.sk89q.worldedit.MaxChangedBlocksException;
import fr.minuskube.inv.SmartInventory;
import fr.edouwarden.space.ship.BoardingShipGUI;
import fr.edouwarden.space.jobs.JobAction;
import fr.edouwarden.space.jobs.Jobs;
import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.station.Property;
import fr.edouwarden.space.ship.ShipEntity;
import fr.edouwarden.space.other.Door;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.ship.ShipGUI;
import fr.edouwarden.space.sign.ShipSign;
import fr.edouwarden.space.sign.SpawnSign;
import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.variables.TextGUI;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ClickOnBlock implements Listener {

    @EventHandler
    public void playerClickOnBlock(PlayerInteractEvent event) throws IOException, MaxChangedBlocksException {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || event.getHand() == EquipmentSlot.OFF_HAND)return;
        Player player = event.getPlayer();

        if (!AuthMeApi.getInstance().isAuthenticated(player))return;
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        if (player.getItemInHand().getType()== Material.EMERALD &&player.getItemInHand().getItemMeta().getDisplayName().equals("Station")){
            event.setCancelled(true);
            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            List<String> lore =Station.setup(player,clickedBlock.getLocation(),meta.getLore());
            meta.setLore(lore);
            player.getItemInHand().setItemMeta(meta);
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE)return;
        // The fabrication bench is resolved from the two displayed ingredients.
        // Cancel the block interaction before the job handler runs so the client
        // never opens the vanilla anvil screen, not even for one frame.
        if (clickedBlock.getType() == Material.ANVIL) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
        }
        if (player.isSneaking() && player.getItemInHand().getType() == Material.AIR){
            if (clickedBlock.getType()== Material.JUKEBOX&&WorldGuardAPI.isLocInRegionName("mayor_"+player.getName(),clickedBlock.getLocation())){

            }
        }
        if(clickedBlock.getType() == Material.OAK_SIGN) {
            Sign sign = (Sign) clickedBlock.getState();
            if (SpawnSign.isSpaceSign(sign)) {
                event.setCancelled(SpawnSign.click(player, sign));
            } else {
                ShipSign.Click(playerEntity, sign);
            }
        }else if(clickedBlock.getType()==Material.OAK_DOOR ){//&& player.getItemInHand().getType()==Material.TRIPWIRE_HOOK && player.getItemInHand().getItemMeta().getDisplayName().equals("keys")) {
            Property property = Property.getFromLoc(clickedBlock.getLocation());
            if(property==null || property.owner ==null ||property.owner.equals(player.getUniqueId()))
                Door.click(clickedBlock);
            else {
                event.setCancelled(true);
                player.sendMessage(TChat.notDoorOwner());
            }
        } else if (clickedBlock.getType() == Material.CRAFTING_TABLE && WorldGuardAPI.isLocInRegionIncompleteName("ship_", clickedBlock.getLocation())){
            event.setCancelled(true);
            String region = WorldGuardAPI.getRegionNameFromLoc("ship_",clickedBlock.getLocation()).replace("ship_","");
            Player shipOwner;
            try {
                shipOwner = Bukkit.getServer().getPlayer(UUID.fromString(region));
            } catch (IllegalArgumentException e) {
                return;
            }
            if (shipOwner == null) {
                return;
            }
            PlayerEntity ocShipOwner = PlayerEntity.getPlayerEntity(shipOwner);
            ShipEntity ship = ocShipOwner.getOwnedShip();
            if (ship == null) {
                return;
            }
            if (!ship.isInSpace()) {
                if(player.getItemInHand().getType()==Material.CLOCK && PlayerEntity.getPlayerEntity(player).getJobDatas().getJob() == Jobs.ASTROGATOR) {
                    ship.setNavigator(player);
                    playerEntity.navigatorOf(ship);
                }else if (shipOwner.getUniqueId().equals(player.getUniqueId())){

                    SmartInventory inv = SmartInventory.builder()
                            .id("ship_"+ship.id)
                            .provider(new BoardingShipGUI(ship))
                            .size(1, 9)
                            .title(TextGUI.shipBoardingGUITitle)
                            .build();
                    inv.open(player);

                }
            } else {
                SmartInventory inv = SmartInventory.builder()
                        .id("ship_"+ship.id)
                        .provider(new ShipGUI(ship))
                        .size(1, 9)
                        .title(TextGUI.shipGUITitle)
                        .build();
                inv.open(player);
            }

        }else if (JobAction.hadOne(player) ){
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            if (event.getAction()==Action.LEFT_CLICK_BLOCK) JobAction.breakBlock(playerEntity,clickedBlock);
            else if (event.getAction()==Action.RIGHT_CLICK_BLOCK) JobAction.useBlock(playerEntity,clickedBlock);

        }
         if(!(clickedBlock.getType() == Material.CHEST ))
            event.setCancelled(true);
    }


}
