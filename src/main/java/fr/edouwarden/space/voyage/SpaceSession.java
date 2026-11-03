package fr.edouwarden.space.voyage;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import fr.edouwarden.space.resources.WorldEditAPI;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.sign.SpawnSign;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Spawn, travel and undo for ships that leave a station and enter the space layer.
 *
 * <p>Two directions are supported:</p>
 * <ul>
 *   <li>{@link #spawnAtSign} parks a ship on a station slot (schematic + region);</li>
 *   <li>{@link #enter} moves a docked ship into the generated space world.</li>
 * </ul>
 * Both are undone by {@link #remove(Player)}.
 */
public final class SpaceSession {

    private static final int MARKER_Y = 81;
    private static final int SPAWN_Y = 61;

    private SpaceSession() {
    }

    /**
     * Parks the player's ship at a station slot.
     */
    public static void spawnAtSign(Player player, Location slot, int rotation, int shipType) {
        if (VoyageState.SPAWN_DATA.containsKey(player.getUniqueId())) {
            remove(player);
        }
        try {
            Clipboard clipboard = WorldEditAPI.paste(General.ship(shipType), rotation, slot);
            if (clipboard == null) {
                return;
            }
            createRegion(player, clipboard, rotation, slot);
            remember(player, slot, rotation, shipType, false);
        } catch (IOException | MaxChangedBlocksException e) {
            General.console.log(Level.SEVERE, "Cannot paste ship schematic", e);
        }
    }

    /**
     * Moves the player and their crew from a station slot into the space world.
     */
    public static void enter(Player player) {
        World space = Bukkit.getWorld("space");
        if (space == null) {
            player.sendMessage(TChat.spaceFull());
            return;
        }

        List<Player> crew = new ArrayList<>(
                WorldGuardAPI.playersInRegion(player.getWorld(), "Ship_" + player.getUniqueId()));
        if (!crew.contains(player)) {
            crew.add(player);
        }

        Vector slot = allocate(space);
        if (slot == null) {
            player.sendMessage(TChat.spaceFull());
            return;
        }

        space.getBlockAt(slot.getBlockX(), MARKER_Y, slot.getBlockZ()).setType(Material.GOLD_BLOCK);

        int shipType = 1; // TODO: read the owned ship type from persistence
        try {
            WorldEditAPI.paste(General.cage(), 0,
                    new Location(space, slot.getBlockX(), slot.getBlockY(), slot.getBlockZ()));
            WorldEditAPI.paste(General.ship(shipType), 0,
                    new Location(space, slot.getBlockX(), slot.getBlockY(), slot.getBlockZ()));
        } catch (IOException | MaxChangedBlocksException e) {
            General.console.log(Level.SEVERE, "Cannot paste space schematics", e);
            return;
        }

        // Undo the docked ship while the player is still in the station world so the
        // station region is removed from the correct world. The space record is stored
        // afterwards, replacing the station entry.
        remove(player, false);

        player.teleport(new Location(space, slot.getBlockX(), SPAWN_Y, slot.getBlockZ()));
        VoyageState.CONFIRMED.remove(player.getUniqueId());
        remember(player, new Location(space, slot.getBlockX(), SPAWN_Y, slot.getBlockZ()),
                0, shipType, true);
    }

    /**
     * Undoes the current voyage: clears the marker, the region and the station sign.
     */
    public static void remove(Player player) {
        remove(player, true);
    }

    private static void remove(Player player, boolean notify) {
        List<Integer> data = VoyageState.SPAWN_DATA.remove(player.getUniqueId());
        if (data == null) {
            return;
        }
        WorldGuardAPI.removeRegion(player.getWorld(), player.getUniqueId());

        int x = data.get(0);
        int y = data.get(1);
        int z = data.get(2);
        int rotation = data.get(3);
        int shipType = data.get(4);
        boolean inSpace = VoyageState.isInSpace(player.getUniqueId());

        try {
            String undo = inSpace ? General.cage() + "-reset" : General.undoShip(shipType);
            WorldEditAPI.paste(undo, rotation, new Location(player.getWorld(), x, y, z));
        } catch (IOException | MaxChangedBlocksException e) {
            General.console.log(Level.WARNING, "Cannot paste undo schematic", e);
        }

        if (inSpace) {
            player.getWorld().getBlockAt(x, MARKER_Y, z).setType(Material.AIR);
            VoyageState.IN_SPACE.remove(player.getUniqueId());
        } else {
            Block sign = player.getWorld().getBlockAt(x, y, z);
            if (sign.getType() == Material.OAK_SIGN) {
                SpawnSign.clear(sign);
            }
        }
        VoyageState.IN_SPACE.remove(player.getUniqueId());
        if (notify) {
            player.sendMessage(TChat.done());
        }
    }

    public static void removeAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            remove(player, false);
        }
    }

    /**
     * Finds the next free grid cell in the space world.
     */
    public static Vector allocate(World world) {
        int x = General.startx;
        int z = General.startz;
        while (world.getBlockAt(x, MARKER_Y, z).getType() == Material.GOLD_BLOCK) {
            if (x >= General.spacingXMax) {
                x = General.startx;
                z -= General.spacingZ;
                if (z < General.spacingZMax) {
                    return null;
                }
            } else {
                x += General.spacingX;
            }
        }
        return new Vector(x, SPAWN_Y, z);
    }

    private static void createRegion(Player player, Clipboard clipboard, int rotation, Location slot) {
        BlockVector3 origin = clipboard.getOrigin().add(0, 0, -1);
        BlockVector3 min = origin.subtract(clipboard.getMinimumPoint());
        BlockVector3 max = origin.subtract(clipboard.getMaximumPoint());
        min = WorldEditAPI.extremePoint(rotation, min, slot.toVector());
        max = WorldEditAPI.extremePoint(rotation, max, slot.toVector());
        WorldGuardAPI.addRegion(player,
                new BlockVector(min.getX(), min.getY(), min.getZ()),
                new BlockVector(max.getX(), max.getY(), max.getZ()),
                slot.getWorld());
    }

    private static void remember(Player player, Location location, int rotation,
                                 int shipType, boolean inSpace) {
        List<Integer> data = new ArrayList<>();
        data.add(location.getBlockX());
        data.add(location.getBlockY());
        data.add(location.getBlockZ());
        data.add(rotation);
        data.add(shipType);
        data.add(inSpace ? 1 : 0);
        VoyageState.SPAWN_DATA.put(player.getUniqueId(), data);
        VoyageState.IN_SPACE.put(player.getUniqueId(), inSpace);
    }
}
