package fr.edouwarden.space.jobs;

import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Woodcutter {
    public static void mineBlock(Block block, PlayerEntity playerEntity) {
        Player player = playerEntity.getBukkitPL();
        if(WorldGuardAPI.isLocInRegionIncompleteName("forest",player.getLocation()))
        if(player.getItemInHand().getType().toString().contains("_AXE")){
            if(block.getType() == Material.LEGACY_LOG || block.getType() == Material.LEGACY_LOG_2){
                if(InventoryManager.addItem(player.getInventory(),new ItemStack(block.getType()))){
                    block.setType(Material.AIR);
                }
            }

        }

    }
}
