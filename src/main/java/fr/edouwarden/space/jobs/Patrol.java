package fr.edouwarden.space.jobs;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.other.Door;
import fr.edouwarden.space.variables.TextGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class Patrol {
    public static void clickBlock(Block clickedBlock, PlayerEntity playerEntity) {
        int rank = 5;//TODO rank
        Player player = playerEntity.getBukkitPL();
        if(clickedBlock.getType() == Material.IRON_DOOR){
            if (((Bisected)clickedBlock.getBlockData()).getHalf() == Bisected.Half.TOP) clickedBlock = clickedBlock.getLocation().add(0,-1,0).getBlock();

            BlockState blockState = clickedBlock.getState();

            Openable door = (Openable) blockState.getData();

            Material under = clickedBlock.getLocation().add(0,-1,0).getBlock().getType();
            if(under == Material.GOLD_BLOCK && rank ==5) {//TODO rank
                Door.click(clickedBlock);//TODO Door class
                door.setOpen(!door.isOpen());
                blockState.setData((MaterialData) door);
                blockState.update();
            }


        } else if (clickedBlock.getType() == Material.BREWING_STAND){
            player.openInventory(inv());
        }

    }
    public static Inventory inv(){
        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, TextGUI.bountyGUITitle);
        return inv;
    }

    public static void addItem(AnvilInventory top) {
        for(ItemStack item :top.getContents()){
        }
        ItemStack first = top.getItem(0);
        ItemStack second = top.getItem(1);
        ItemStack result = top.getItem(2);
        if (first != null && second != null && first.getType() == second.getType())top.setItem(2, new ItemStack(Material.MAGENTA_SHULKER_BOX));

    }
}
