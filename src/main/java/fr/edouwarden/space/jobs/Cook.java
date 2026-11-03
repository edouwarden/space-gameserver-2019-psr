package fr.edouwarden.space.jobs;


import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static fr.edouwarden.space.resources.ArmorStand.addItem;
public class Cook {
    public static void clickBlock(Block clickedBlock, PlayerEntity playerEntity) {
        Player player = playerEntity.getBukkitPL();
        if (clickedBlock.getType() == Material.POWERED_RAIL && player.getItemInHand().getType()==Material.FLINT_AND_STEEL && (clickedBlock.getData()==0 || clickedBlock.getData()==1)) {
            startFire(clickedBlock,player);
            player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability()-2));
        }else if (clickedBlock.getType() == Material.POWERED_RAIL&& Food.isOne(player.getItemInHand())) {
            //List<Player> players= new ArrayList<>();
            //players.add(player);
            Food food = new Food(player.getItemInHand());
            if (!food.raw()) return;
            ArmorStand raw = addItem(clickedBlock.getLocation().add(0.9,-1.777,0.8), Material.matchMaterial(food.getMaterial()), false,-3.1F);
            Location loc = clickedBlock.getLocation();
            new BukkitRunnable() {
                int x=0;
                @Override
                public void run() {
                    if(x==40){
                        raw.setItemInHand(new ItemStack(Material.getMaterial(food.getCoocked())));
                    }else if (x==80){
                        raw.remove();
                        cancel();

                    }
                    if (loc.getBlock().getData()==(byte)1) {
                        x++;
                    }
                }
            }.runTaskTimerAsynchronously(General.plugin,0,General.seconds * food.cookTime() );
            //General.effectmanager.display(ParticleEffect.REDSTONE,  ,player.getLocation(),5, players);
        }
    }
    public static void startFire(Block clickedBlock,Player player){

        clickedBlock.getState().getData().setData((byte)8);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= General.seconds * 15) {
                    clickedBlock.getState().getData().setData((byte) 7);
                    this.cancel();
                    return;
                }
                player.getWorld().spawnParticle(org.bukkit.Particle.FLAME,
                        clickedBlock.getLocation().add(0.5D, 0.05D, 0.5D),
                        1, 0.0D, 0.0D, 0.0D, 0.02D);
                player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_NORMAL,
                        clickedBlock.getLocation().add(0.5D, 0.3D, 0.5D),
                        7, 0.0D, 0.0D, 0.0D, 0.01D);
                ticks += 5;
            }
        }.runTaskTimer(General.plugin, 0L, 5L);
    }
}
