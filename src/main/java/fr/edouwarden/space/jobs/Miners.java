package fr.edouwarden.space.jobs;

import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Miners {
    public static void mineBlock(Block block, PlayerEntity playerEntity){
        Player player = playerEntity.getBukkitPL();
        if(WorldGuardAPI.isLocInRegionIncompleteName("mine",player.getLocation()))
        if(player.getItemInHand().getType().toString().contains("_PICKAXE")){
            if(block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE ||block.getType() == Material.EMERALD_ORE){
                if(InventoryManager.addItem(player.getInventory(),new ItemStack(block.getType()))){
                    block.setType(Material.AIR);

                }
            }

        }

    }
}
