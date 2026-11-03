package fr.edouwarden.space.resources;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.UUID;


public class WorldGuardAPI {
    public static ProtectedCuboidRegion addRegion(Player player, BlockVector locmin, BlockVector locmax, World world){
        ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion("Ship_" + player.getUniqueId(),BlockVector3.at(locmin.getX(), locmin.getY(), locmin.getZ()) ,BlockVector3.at(locmax.getX(),locmax.getY(),locmax.getZ()));
        RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        assert regions != null;
        regions.addRegion(protectedCuboidRegion);
        return protectedCuboidRegion;
    }

    public static void removeRegion(World world, UUID owner) {
        RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(world));
        if (regions != null) {
            regions.removeRegion("Ship_" + owner);
        }
    }
    public static ArrayList<String> getRegions(Location loc){
        ArrayList<String> list = new ArrayList<>();
        ApplicableRegionSet regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()));
        for (ProtectedRegion r : regions) {
            list.add(r.getId());
        }
        return list;
    }
    public static boolean isLocInRegionName(String regionname,Location loc){
        return getRegions(loc).contains(regionname);
    }
    public static boolean canPlayerHaveDamage(Location loc){
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()));
        return set.testState(null, Flags.PVP);
    }
    public static boolean canEntityHaveDamage(Location loc){
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()));
        return set.testState(null, Flags.DAMAGE_ANIMALS);
    }
    public static boolean isLocInRegionIncompleteName(String regionname,Location loc){
        for (String name : getRegions(loc)) {
            if (name.contains(regionname))
                return true;
        }
        return false;
    }
    public static ArrayList<Player> playersInRegion(World world, String regionName){
        ArrayList<Player> players = new ArrayList<>();
        RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(world));
        if (regions == null) {
            return players;
        }
        ProtectedRegion ship = regions.getRegion(regionName);
        if (ship == null) {
            return players;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(world)
                    && ship.contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(),
                    p.getLocation().getBlockZ())) {
                players.add(p);
            }
        }
        return players;
    }
    public static UUID uuidOfProperty(Location loc){
        for (String name : getRegions(loc)) {
            if (name.contains("property_"))
                return UUID.fromString(name);
        }
        return null;
    }

    public static String getRegionNameFromLoc(String partOfRegionName, Location loc) {
        for (String name : getRegions(loc)) {
            if (name.contains(partOfRegionName))
                return name;
        }
        return null;
    }
}
