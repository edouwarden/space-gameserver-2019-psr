package fr.edouwarden.space.other;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public class Door {
	public static void click(Block block){
		   BlockState blockState = block.getState();
            if (blockState.getData().getData() == 8|| blockState.getData().getData() ==9)block = block.getLocation().add(0,-1,0).getBlock();
            Block side =null;
            //block.getM
            if(block.getRelative(BlockFace.EAST).getType()==Material.OAK_DOOR) side = block.getRelative(BlockFace.EAST);
            else if(block.getRelative(BlockFace.NORTH).getType()==Material.OAK_DOOR) side = block.getRelative(BlockFace.NORTH);
            else if(block.getRelative(BlockFace.SOUTH).getType()==Material.OAK_DOOR) side = block.getRelative(BlockFace.SOUTH);
            else if(block.getRelative(BlockFace.WEST).getType()==Material.OAK_DOOR) side = block.getRelative(BlockFace.WEST);
            doorAlternate(blockState);
            if (side !=null)doorAlternate(side.getState());
	}
	private static void doorAlternate(BlockState blockState){
        org.bukkit.block.data.type.Door door = (org.bukkit.block.data.type.Door) blockState.getBlock().getBlockData();
        door.setOpen(!door.isOpen());
        blockState.setData((MaterialData) door);
        blockState.update();
    }
}