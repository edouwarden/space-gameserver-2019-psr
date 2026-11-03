package fr.edouwarden.space.resources;

import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

/**
 * Binds the frontier world.
 *
 * <p>The generated dimensions ({@code space}, {@code exoplanet}) are created by
 * {@link fr.edouwarden.space.world.SpaceWorlds}. This class only resolves the
 * regular server world used as the station frontier.</p>
 */
public final class World {

    public static final String FRONTIER_WORLD = "world";

    private World() {
    }

    public static void loadWorld() {
        org.bukkit.World frontier = Bukkit.getWorld(FRONTIER_WORLD);
        if (frontier == null) {
            frontier = Bukkit.createWorld(new WorldCreator(FRONTIER_WORLD)
                    .generateStructures(true));
        }
        General.frontierBukkitWorld = frontier;
        General.frontierWorld = frontier;
    }

    public static void unloadWorld() {
        // Worlds are persisted; the plugin never deletes region folders.
    }
}
