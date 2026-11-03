package fr.edouwarden.space.event.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class Creature implements Listener {
    @EventHandler
    public void spawn(CreatureSpawnEvent event){
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        if (reason!= CreatureSpawnEvent.SpawnReason.BREEDING && reason!= CreatureSpawnEvent.SpawnReason.CUSTOM && reason!= CreatureSpawnEvent.SpawnReason.SPAWNER_EGG&& reason!= CreatureSpawnEvent.SpawnReason.DEFAULT) event.setCancelled(true);
    }
    @EventHandler
    public void explode(EntityExplodeEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    public void interact(EntityInteractEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    public void love(EntityBreedEvent event){
        //event.
        //event.setCancelled(true);
    }
    @EventHandler
    public void pig(PigZapEvent event){
        event.setCancelled(true);
    }
}
