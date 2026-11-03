package fr.edouwarden.space.data;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

/**
 * Serializes per-player state to {@code PlayersData/<name>-<uuid>.yml}.
 *
 * <p>The extension is historical; the payload is a Bukkit object stream, not YAML.</p>
 */
public final class Data {

    private Data() {
    }

    public static boolean linkAccountData(Player player) {
        String path = General.playersDataFolderPath + player.getName() + "-"
                + player.getUniqueId() + ".yml";
        File playerFile = new File(path);
        if (!playerFile.exists()) {
            new PlayerEntity(player);
            return true;
        }
        try (FileInputStream fin = new FileInputStream(playerFile);
             ObjectInputStream ois = new BukkitObjectInputStream(fin)) {
            PlayerEntity playerEntity = (PlayerEntity) ois.readObject();
            playerEntity.bukkitPL = player;
            PlayerEntity.addPlayerEntity(playerEntity);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            General.console.log(Level.SEVERE, "Cannot load player data for " + player.getName(), e);
            return false;
        }
    }

    public static void unLinkAll() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            unLinkAccountData(PlayerEntity.getPlayerEntity(player));
        }
    }

    public static void unLinkAccountData(PlayerEntity player) {
        player.leave();
        String path = General.playersDataFolderPath + player.getBukkitPL().getName() + "-"
                + player.getBukkitPL().getUniqueId() + ".yml";
        File data = new File(path);
        if (!data.exists()) {
            try {
                if (!data.createNewFile()) {
                    General.console.log(Level.FINE, "Player data file already exists: " + path);
                }
            } catch (IOException e) {
                General.console.log(Level.WARNING, "Cannot create player data file: " + path, e);
            }
        }
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new BukkitObjectOutputStream(fos)) {
            oos.writeObject(player);
        } catch (IOException e) {
            General.console.log(Level.SEVERE, "Cannot save player data for "
                    + player.getBukkitPL().getName(), e);
        }
    }
}
