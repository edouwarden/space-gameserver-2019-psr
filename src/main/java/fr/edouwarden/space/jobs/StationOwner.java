package fr.edouwarden.space.jobs;

import fr.minuskube.inv.SmartInventory;
import fr.edouwarden.space.gui.MayorGUI;
import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class StationOwner {
    public static void clickBlock(Block clickedBlock, PlayerEntity playerEntity) {
        //String data = WorldGuardAPI.getRegionNameFromLoc("townhall_",clickedBlock.getLocation());
        boolean isInTownHall = clickedBlock.getType() == Material.CRAFTING_TABLE && WorldGuardAPI.isLocInRegionIncompleteName("townhall_", clickedBlock.getLocation());
        if( isInTownHall){//data!=null){
            Station station = Station.getFromLoc(clickedBlock.getLocation());
            if(station.owner == playerEntity.getBukkitPL().getUniqueId())
                openTownHallGUI(playerEntity,station);

        }
    }

    private static void openTownHallGUI(PlayerEntity playerEntity, Station station) {
        SmartInventory inv = SmartInventory.builder()
                .id("townhall_"+station.name())
                .provider(new MayorGUI())
                .size(1, 9)
                .title(ChatColor.BLUE + "Station hall: " + station.name())
                .build();
        inv.open(playerEntity.getBukkitPL());
    }
}
