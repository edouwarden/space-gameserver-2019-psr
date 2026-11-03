package fr.edouwarden.space.jobs;

import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Farmer {
    public static void mineBlock(Block block, PlayerEntity playerEntity) {
        Player player = playerEntity.getBukkitPL();
        if(WorldGuardAPI.isLocInRegionIncompleteName("farm",player.getLocation()))
        if(block.getType()== Material.WHEAT && block.getData()==7)
            if(InventoryManager.addItem(player.getInventory(),new ItemStack(Material.WHEAT))){
                block.setType(Material.WHEAT);
            }

    }
}
