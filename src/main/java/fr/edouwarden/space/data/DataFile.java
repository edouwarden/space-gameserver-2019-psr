package fr.edouwarden.space.data;

import fr.edouwarden.space.variables.General;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class DataFile {

    private DataFile() {
    }

    public static void setup() {
        General.fishingFile = file(General.fishFilePath);
        General.fishingConfig = YamlConfiguration.loadConfiguration(General.fishingFile);
        General.materialFile = file(General.materialFilePath);
        General.materialConfig = YamlConfiguration.loadConfiguration(General.materialFile);
        General.frontierFile = file(General.frontierFilePath);
        General.frontierMap = YamlConfiguration.loadConfiguration(General.frontierFile);
        General.foodFile = file(General.foodFilePath);
        General.foodConfig = YamlConfiguration.loadConfiguration(General.foodFile);
        General.propertysFile = file(General.propertysFilePath);
        General.propertysConfig = YamlConfiguration.loadConfiguration(General.propertysFile);
        // stations.yml is a Java-serialized map written by Station.saveStation, not YAML:
        // it must not be parsed here. Station.registerSetup reads it as binary.
        General.stationsFile = file(General.stationsFilePath);
    }

    public static File file(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            General.console.warning("Cannot create folder: " + parent);
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    General.console.warning("Cannot create data file: " + path);
                }
            } catch (IOException e) {
                General.console.log(Level.WARNING, "Cannot create data file: " + path, e);
            }
        }
        return file;
    }

    public static void set(FileConfiguration config, File file, String path, Object object) {
        config.set(path, object);
        saveConfig(config, file);
    }

    public static Object get(FileConfiguration config, String path) {
        return config.get(path);
    }

    public static boolean contains(FileConfiguration config, String path, Object object) {
        return get(config, path) == object;
    }

    public static void saveConfig(FileConfiguration config, File file) {
        try {
            config.save(file);
            General.console.log(Level.INFO, "Saved change for " + file.getName());
        } catch (IOException e) {
            General.console.log(Level.SEVERE, "Cannot save to " + file.getName(), e);
        }
    }
}
