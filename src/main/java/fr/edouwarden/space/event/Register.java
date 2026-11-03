package fr.edouwarden.space.event;

import fr.edouwarden.space.Main;
import fr.edouwarden.space.event.onplayer.*;
import fr.edouwarden.space.event.server.*;
import org.bukkit.Bukkit;

public class Register {
    public static Main plugin = Main.getPlugin(Main.class);
    public static void eventRegister(){
        Bukkit.getPluginManager().registerEvents(new ClickOnBlock(), plugin);
        Bukkit.getPluginManager().registerEvents(new GUIEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new Eat(), plugin);
        Bukkit.getPluginManager().registerEvents(new InteractEntity(), plugin);
        Bukkit.getPluginManager().registerEvents(new TryToAttack(), plugin);
        Bukkit.getPluginManager().registerEvents(new MoveItem(), plugin);
        Bukkit.getPluginManager().registerEvents(new Join(), plugin);
        Bukkit.getPluginManager().registerEvents(new ToggleGlide(), plugin);
        Bukkit.getPluginManager().registerEvents(new Leave(), plugin);
        Bukkit.getPluginManager().registerEvents(new ResourcePackStatus(), plugin);
        Bukkit.getPluginManager().registerEvents(new Respawn(), plugin);
        Bukkit.getPluginManager().registerEvents(new Death(), plugin);
        Bukkit.getPluginManager().registerEvents(new HasHungerChanges(), plugin);
        Bukkit.getPluginManager().registerEvents(new Weather(), plugin);
        Bukkit.getPluginManager().registerEvents(new DropItem(), plugin);
        Bukkit.getPluginManager().registerEvents(new Click(), plugin);
        Bukkit.getPluginManager().registerEvents(new Move(), plugin);
        Bukkit.getPluginManager().registerEvents(new RegainHealth(), plugin);
        Bukkit.getPluginManager().registerEvents(new BreakBlock(), plugin);
        Bukkit.getPluginManager().registerEvents(new Chat(), plugin);
        Bukkit.getPluginManager().registerEvents(new UseArmorStand(), plugin);
        Bukkit.getPluginManager().registerEvents(new CraftItem(), plugin);
        Bukkit.getPluginManager().registerEvents(new Shift(), plugin);
        Bukkit.getPluginManager().registerEvents(new Explosion(), plugin);
        Bukkit.getPluginManager().registerEvents(new Creature(), plugin);
        Bukkit.getPluginManager().registerEvents(new HoldItem(), plugin);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), plugin);
        Bukkit.getPluginManager().registerEvents(new OnProjectileHit(), plugin);



    }
}
