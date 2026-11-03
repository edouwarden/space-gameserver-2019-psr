package fr.edouwarden.space.world;

import fr.edouwarden.space.resources.CustomChunkGenerator;
import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Lifecycle for the two generated dimensions of the space layer.
 *
 * <p>The prototype deleted the world folders on shutdown. That is destructive and is
 * deliberately not reproduced here: worlds are kept on disk between runs.</p>
 */
public final class SpaceWorlds {

    public static final String SPACE = "space";
    public static final String EXOPLANET = "exoplanet";

    private SpaceWorlds() {
    }

    public static void load() {
        World space = create(SPACE);
        General.spaceBukkitWorld = space;
        General.spaceWorld = space;
        create(EXOPLANET);
    }

    public static void unload() {
        // Worlds are persisted; nothing to tear down here.
    }

    private static World create(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator(name)
                    .generator(new CustomChunkGenerator())
                    .generateStructures(false));
        }
        return world;
    }
}
