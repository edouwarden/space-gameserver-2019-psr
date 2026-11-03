package fr.edouwarden.space.jobs;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TextGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class Blacksmith {
    public static void clickBlock(Block clickedBlock, PlayerEntity playerEntity) {
        if (clickedBlock.getType() == Material.ANVIL){
            final Player player = playerEntity.getBukkitPL();
            AnvilCraft.oncraft(clickedBlock, player);
            // The custom bench is driven by the items placed on the block, so the
            // vanilla anvil screen must never stay open.
            Bukkit.getScheduler().runTask(General.plugin, new Runnable() {
                @Override
                public void run() {
                    player.closeInventory();
                }
            });
        }

    }
    public static Inventory inv(){
        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, TextGUI.blacksmithGUITitle);
        return inv;
    }



}
