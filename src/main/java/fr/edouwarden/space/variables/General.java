package fr.edouwarden.space.variables;

import de.slikey.effectlib.EffectManager;
import fr.edouwarden.space.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.logging.Logger;

public final class General {

    // plugin
    public static Main plugin = Main.getPlugin(Main.class);
    public static EffectManager effectManager;
    public static Economy econ;
    public static Logger console = plugin.getLogger();
    public static NamespacedKey key = new NamespacedKey(plugin, "space");

    // property
    public static int maxRentDays = 20;

    // files
    public static File weaponsFile;
    public static File fishingFile;
    public static File foodFile;
    public static File materialFile;
    public static File frontierFile;
    public static File propertysFile;
    public static File stationsFile;

    // loaded configurations
    public static YamlConfiguration materialConfig;
    public static YamlConfiguration fishingConfig;
    public static YamlConfiguration foodConfig;
    public static YamlConfiguration frontierMap;
    public static YamlConfiguration propertysConfig;

    // time
    public static int seconds = 20;
    public static int minutes = 1200;
    public static long loopspeed = 1000;

    // voice
    public static Byte highVoiceRange = 15;
    public static Byte normalVoiceRange = 9;
    public static Byte lowVoiceRange = 3;
    public static Byte muteVoiceRange = 0;
    public static String voiceHost = "127.0.0.1";
    public static int voicePort = 5000;
    public static int voiceControlPort = 81;

    // gameplay
    public static boolean raidEnable;
    public static long defaultRaidDuration = 5 * 60 * 1000;
    public static String powerDisplayName = ChatColor.RED + "Power";

    // ship grid
    public static int startx = 1195;
    public static int startz = -1081;
    public static int spacingX = 75;
    public static int spacingZ = 75;
    public static int spacingXMax = 750 + startx;
    public static int spacingZMax = -750 + startz;

    // player
    public static int foodLevel = 19;
    public static float walkSpeed = 0.095F;
    public static float runSpeed = 0.25F;

    // paths
    public static String resourcePackPath = "";
    public static String avatarUrl = "";
    public static String pluginPath = plugin.getDataFolder() + File.separator;
    public static String powerPlayersFilePath = pluginPath + "powerPlayers.yml";
    public static String fishFilePath = pluginPath + "fishDatas.yml";
    public static String foodFilePath = pluginPath + "foodDatas.yml";
    public static String materialFilePath = pluginPath + "materials.yml";
    public static String frontierFilePath = pluginPath + File.separator + "WorldMaps" + File.separator + "frontier.yml";
    public static String powerConfigFilePath = pluginPath + "powerConfig.yml";
    public static String playerJobsFilePath = pluginPath + "playerJobs.yml";
    public static String stationsFilePath = pluginPath + "stations.yml";
    public static String propertysFilePath = pluginPath + "propertys.yml";
    public static String customMapFilePath = pluginPath + "customMap.yml";
    public static String mapsFolderPath = pluginPath + File.separator + "CustomMaps" + File.separator;
    public static String playersDataFolderPath = pluginPath + File.separator + "PlayersData" + File.separator;
    public static String bountyFramePath = pluginPath + "bounty.png";
    public static String testImgPath = pluginPath + "test.png";
    public static String backgroundPath = pluginPath + "nothing.png";
    public static String schematicsFolderPath = pluginPath + "schematics" + File.separator;

    // worlds
    public static World spaceBukkitWorld;
    public static World frontierBukkitWorld;
    public static World spaceWorld;
    public static World frontierWorld;

    private General() {
    }

    public static void loadConfig(FileConfiguration config) {
        resourcePackPath = config.getString("resource-pack.url", "");
        voiceHost = config.getString("voice.host", voiceHost);
        voicePort = config.getInt("voice.audio-port", voicePort);
        voiceControlPort = config.getInt("voice.control-port", voiceControlPort);
        highVoiceRange = (byte) config.getInt("voice.range.high", highVoiceRange);
        normalVoiceRange = (byte) config.getInt("voice.range.normal", normalVoiceRange);
        lowVoiceRange = (byte) config.getInt("voice.range.low", lowVoiceRange);
        muteVoiceRange = (byte) config.getInt("voice.range.mute", muteVoiceRange);
        seconds = config.getInt("loop.seconds", seconds);
        loopspeed = config.getLong("loop.speed", loopspeed);
        foodLevel = config.getInt("gameplay.food-level", foodLevel);
        maxRentDays = config.getInt("gameplay.max-rent-days", maxRentDays);
        walkSpeed = (float) config.getDouble("gameplay.walk-speed", walkSpeed);
        runSpeed = (float) config.getDouble("gameplay.run-speed", runSpeed);
        raidEnable = config.getBoolean("gameplay.raid-enabled", raidEnable);
        avatarUrl = config.getString("rendering.avatar-url", avatarUrl);
        startx = config.getInt("grid.start-x", startx);
        startz = config.getInt("grid.start-z", startz);
        spacingX = config.getInt("grid.spacing-x", spacingX);
        spacingZ = config.getInt("grid.spacing-z", spacingZ);
        spacingXMax = 750 + startx;
        spacingZMax = -750 + startz;
    }

    public static String ship(Integer shiptype) {
        return "ship-" + shiptype;
    }

    public static String undoShip(Integer shiptype) {
        return "ship-" + shiptype + "-reset";
    }

    public static String cage() {
        return "cage";
    }

    /** Creates the data folders the plugin writes to. */
    public static void createDirectories() {
        String[] folders = {
                pluginPath,
                pluginPath + "WorldMaps",
                pluginPath + "CustomMaps",
                pluginPath + "PlayersData",
                pluginPath + "schematics"
        };
        for (String folder : folders) {
            File file = new File(folder);
            if (!file.exists() && !file.mkdirs()) {
                console.warning("Cannot create data folder: " + folder);
            }
        }
    }

    public static ItemStack item(Material material, String name) {
        ItemStack itemstack = new ItemStack(material);
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(name);
        itemstack.setItemMeta(meta);
        return itemstack;
    }
}
