package fr.edouwarden.space;

import de.slikey.effectlib.EffectManager;
import fr.edouwarden.space.commands.BaseCommand;
import fr.edouwarden.space.data.Data;
import fr.edouwarden.space.data.DataFile;
import fr.edouwarden.space.event.Register;
import fr.edouwarden.space.jobs.Craft;
import fr.edouwarden.space.resources.CustomChunkGenerator;
import fr.edouwarden.space.resources.EconomySetup;
import fr.edouwarden.space.resources.ProtocolLib;
import fr.edouwarden.space.resources.World;
import fr.edouwarden.space.ship.ShipEntity;
import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.variables.DatabaseConfig;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.voyage.SpaceSession;
import fr.edouwarden.space.world.SpaceWorlds;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        DatabaseConfig.load(getConfig());
        General.loadConfig(getConfig());
        General.createDirectories();
        if (!new java.io.File(getDataFolder(), "materials.yml").exists()) {
            saveResource("materials.yml", false);
        }

        Register.eventRegister();
        DataFile.setup();
        World.loadWorld();
        SpaceWorlds.load();
        EconomySetup.setup();
        Station.registerSetup();
        Loop.loop(this);
        General.effectManager = new EffectManager(this);

        if (getCommand("space") != null) {
            getCommand("space").setExecutor(new BaseCommand());
        }
        Craft.setup();

        getLogger().info("Space enabled.");
    }

    @Override
    public void onLoad() {
        ProtocolLib.onLoad();
        getLogger().info("Space loaded.");
    }

    @Override
    public void onDisable() {
        ShipEntity.clearAll();
        SpaceSession.removeAll();
        Data.unLinkAll();
        getLogger().info("Saving stations.");
        Station.saveStation();
        SpaceWorlds.unload();
        getLogger().log(Level.INFO, "Space disabled.");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if (SpaceWorlds.SPACE.equals(worldName) || SpaceWorlds.EXOPLANET.equals(worldName)) {
            return new CustomChunkGenerator();
        }
        return null;
    }
}
