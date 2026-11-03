package fr.edouwarden.space.data;

import fr.edouwarden.space.resources.ImageRenderer;
import fr.edouwarden.space.variables.Database;
import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Map {
    private static File mapFile;
    private static YamlConfiguration customMaps;
    public static void load(){
        File folderPath = new File(General.mapsFolderPath);
        File[] fileNames = folderPath.listFiles();
        if (fileNames == null) {
            return;
        }
        for(File file : fileNames) {
            BufferedImage source = null;
            try {
                source = ImageIO.read(file);
            } catch (IOException e) {
                General.console.log(java.util.logging.Level.WARNING, "Cannot read map image " + file.getName(), e);
            }
            if (source != null) {
                short id = Short.valueOf(file.getName().replace(".jpg", ""));
                MapView view = Bukkit.getMap(id);
                view.getRenderers().clear();

                view.addRenderer(new ImageRenderer(source));
            }

        }
    }
}
