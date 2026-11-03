package fr.edouwarden.space.fight.weapons;

import fr.edouwarden.space.fight.Damage;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Weapon {
    public void Weapon(){

    }
    public static boolean playerHoldOne(Player player){
        if (player.getItemInHand() ==null ||player.getItemInHand().getType() == Material.AIR)return false;
        return player.getItemInHand().getItemMeta().getLocalizedName() !=null;
    }

    public static void usePower(Player player, String weaponType, String weaponName, int skill){
        ItemStack weapon = player.getItemInHand();

        try {
            Class className = Class.forName("fr.edouwarden.space.fight.weapons.custom."+weaponType);
            Method method = className.getDeclaredMethod("use", Player.class, ItemStack.class, String.class, int.class);

            method.invoke(null, player,weapon,weaponName,skill);
            return;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            General.console.log(java.util.logging.Level.WARNING, "Cannot invoke weapon use method", e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            General.console.log(java.util.logging.Level.WARNING, "Cannot invoke weapon use method", e);
        }
    }
    public static void use(Player player, String[] weapondata, Entity entity) {
        if (player.hasCooldown(player.getItemInHand().getType()))return;
        double range = WeaponsInfo.range(weapondata);
        if(player.getLocation().distance(entity.getLocation())> range*1.5) return;
        ArrayList<Entity> victim = new ArrayList<>();// Entity range of Player => Entity range/2 of mid of mid line => Entity range of last block of line
        if (WeaponsInfo.area(weapondata) != 1) victim = Weapon.entitynear(entity, (int) (player.getLocation().distance(entity.getLocation())), WeaponsInfo.area(weapondata)-1);
        if(entity instanceof Player)
            if (WorldGuardAPI.canPlayerHaveDamage(entity.getLocation()))victim.add(entity);
        else
            if (WorldGuardAPI.canEntityHaveDamage(entity.getLocation()))victim.add(entity);
        double damage =  WeaponsInfo.damage(weapondata);
        victim.remove((Entity)player);
        if (!victim.isEmpty()) {
            victim.forEach(e -> {
                Damage.attack(entity,damage);
            });
            player.setCooldown(player.getItemInHand().getType(), (int) (20*WeaponsInfo.cooldown(weapondata)));
        }

    }
    public static void use(Player player, String[] weapondata) {
        if (player.hasCooldown(player.getItemInHand().getType()))return;
        double range = WeaponsInfo.range(weapondata);
        ArrayList<Entity> victim = getNearestEntityInSight(player,range,WeaponsInfo.area(weapondata),WeaponsInfo.accuracy(weapondata));
        double damage =  WeaponsInfo.damage(weapondata);
        if (victim != null && !victim.isEmpty()) {
            for (Entity e : victim) {
                if (e instanceof Player) {
                    Damage.player(PlayerEntity.getPlayerEntity((Player) e), damage);//TODO custom damaged
                } else if (e instanceof Animals) {
                    ((Animals) e).damage(damage);
                } else if (e instanceof Monster) {
                    ((Monster) e).damage(damage);
                }
                player.setCooldown(player.getItemInHand().getType(), (int) (20 * WeaponsInfo.cooldown(weapondata)));
            }
        }

    }
    public static ArrayList<Entity> entitynear(Entity entity,int range,int area){
        ArrayList<Entity> victimes = new ArrayList<Entity>();
        ArrayList<Entity> arround = (ArrayList<Entity>) entity.getNearbyEntities(range,2,range);
        victimes.add(entity);
        for (Entity ent : arround) {
            if(ent instanceof Player)
                if (WorldGuardAPI.canPlayerHaveDamage(ent.getLocation()))victimes.add(ent);
            else
                if (WorldGuardAPI.canEntityHaveDamage(ent.getLocation()))victimes.add(ent);

            victimes.add(ent);
            if(victimes.size()==area)break;
        }
        return victimes;
    }
    public static ArrayList<Entity> getNearestEntityInSight(Player player, double range, int area,double accuracy) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, (int) Math.round(range));
        ArrayList<Location> sight = new ArrayList<Location>();
        ArrayList<Entity> victimes = new ArrayList<Entity>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        Material type;
        for (Block block : sightBlock) {
            type = block.getType();
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME,
                    block.getLocation().add(0.5D, 0.05D, 0.5D),
                    1, 0.0D, 0.0D, 0.0D, 0.02D);

            if (block.getType() == Material.AIR)
                for (Entity entity: entities) {
                    if(block.getLocation().distance(entity.getLocation()) <=accuracy){
                        victimes.add(entity);
                        if(victimes.size()==area)
                            return victimes;
                    }
                }

        }

        if (!victimes.isEmpty())return victimes;
        return null; //Return null/nothing if no entity was found
    }
    public static ArrayList<Entity> getNearestEntityFrom(Player player, int range, int area,Entity entity) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        double distance = player.getLocation().distance(entity.getLocation());
        ArrayList<Entity> entitiesNear = (ArrayList<Entity>) entity.getNearbyEntities(distance, distance, distance);
        ArrayList<Entity> victimes = new ArrayList<Entity>();
        victimes.add(entity);
        for (Entity victim :entitiesNear) {
            if (victimes.size()==area )break;
            if (entities.contains(victim)) victimes.add(victim);
        }
        if (!victimes.isEmpty())return victimes;
        return null; //Return null/nothing if no entity was found
    }
    public static ArrayList<Entity> getNearestEntity(Player player, int range, int area) {
        return null; //Return null/nothing if no entity was found
    }
    public static boolean isOne(ItemStack item){
        return  item.getItemMeta() != null && item.getItemMeta().getLocalizedName() !=null;//TODO check if weapon
    }

    public static boolean isAPower(ItemStack item) {
        return item.getType() == Material.COBWEB;
    }

}
