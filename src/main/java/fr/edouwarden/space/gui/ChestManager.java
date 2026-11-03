package fr.edouwarden.space.gui;

import fr.edouwarden.space.resources.WorldGuardAPI;
import org.bukkit.block.Chest;

public class ChestManager {
    public static boolean isChestOnShip(Chest chest){
        return WorldGuardAPI.isLocInRegionIncompleteName("ship_",chest.getLocation());
    }
}
