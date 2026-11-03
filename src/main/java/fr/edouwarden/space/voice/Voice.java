package fr.edouwarden.space.voice;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Particle;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Bridge between the game server and the proximity-voice relay.
 *
 * <p>For every audible player the plugin sends a routing line to the relay's control
 * port: {@code &speaker&target#volume&target#volume}. The relay keys connections by
 * player IP, because the plugin only knows the Minecraft connection address, not the
 * source port opened by the voice client.</p>
 */
public final class Voice {

    private Voice() {
    }

    public static void update(PlayerEntity playerEntity) {
        if (playerEntity.isMute()) {
            return;
        }
        byte range = playerEntity.getVoiceRange();
        if (range <= 0) {
            return;
        }
        Player player = playerEntity.getBukkitPL();
        ArrayList<Player> listeners = getNearbyPlayers(player, range);
        try {
            link(player, listeners, range);
        } catch (IOException e) {
            General.console.log(Level.FINE, "Voice relay unavailable: " + e.getMessage());
        }
    }

    public static ArrayList<Player> getNearbyPlayers(Player player, int range) {
        ArrayList<Player> players = new ArrayList<>();
        for (Entity entity : getNearbyEntities(player.getLocation(), range)) {
            if (entity instanceof Player && !entity.getUniqueId().equals(player.getUniqueId())) {
                players.add((Player) entity);
            }
        }
        return players;
    }

    public static ArrayList<Entity> getNearbyEntities(Location location, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        ArrayList<Entity> entities = new ArrayList<>();
        for (int chunkX = -chunkRadius; chunkX <= chunkRadius; chunkX++) {
            for (int chunkZ = -chunkRadius; chunkZ <= chunkRadius; chunkZ++) {
                int x = location.getBlockX();
                int y = location.getBlockY();
                int z = location.getBlockZ();
                Location probe = new Location(location.getWorld(),
                        x + chunkX * 16, y, z + chunkZ * 16);
                for (Entity entity : probe.getChunk().getEntities()) {
                    if (entity.getWorld().equals(location.getWorld())
                            && entity.getLocation().distance(location) <= radius
                            && !entity.getLocation().getBlock().equals(location.getBlock())) {
                        entities.add(entity);
                    }
                }
            }
        }
        return entities;
    }

    public static void link(Player player, List<Player> listeners, byte range) throws IOException {
        String speaker = addressOf(player);
        if (speaker == null) {
            return;
        }
        StringBuilder message = new StringBuilder("&").append(speaker);
        if (listeners != null && !listeners.isEmpty()) {
            for (Player listener : listeners) {
                String target = addressOf(listener);
                if (target == null) {
                    continue;
                }
                float distance = (float) player.getLocation().distance(listener.getLocation());
                int volume = (int) ((1 - (distance / range)) * 100);
                volume = Math.max(0, Math.min(100, volume));
                message.append("&").append(target).append('#').append(volume);
            }
        }
        send(message.toString());
    }

    public static void send(String message) throws IOException {
        try (Socket socket = new Socket(General.voiceHost, General.voiceControlPort);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
            dataOutputStream.flush();
        }
    }

    public static Byte defineRange(Byte range, Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (range == General.muteVoiceRange) {
                return range;
            } else if (range == General.lowVoiceRange) {
                return General.muteVoiceRange;
            } else if (range == General.normalVoiceRange) {
                return General.lowVoiceRange;
            } else if (range == General.highVoiceRange) {
                return General.normalVoiceRange;
            }
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (range == General.muteVoiceRange) {
                return General.lowVoiceRange;
            } else if (range == General.lowVoiceRange) {
                return General.normalVoiceRange;
            } else if (range == General.normalVoiceRange) {
                return General.highVoiceRange;
            } else if (range == General.highVoiceRange) {
                return range;
            }
        }
        return 0;
    }

    public static void changeVoiceRange(PlayerEntity player, byte range) {
        player.setVoiceRange(range);
    }

    public static ItemMeta volumeItem(byte range) {
        ItemStack item = new ItemStack(Material.LEVER);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        if (range == General.lowVoiceRange) {
            meta.setDisplayName("Voice: low");
        } else if (range == General.normalVoiceRange) {
            meta.setDisplayName("Voice: normal");
        } else if (range == General.highVoiceRange) {
            meta.setDisplayName("Voice: high");
        } else {
            meta.setDisplayName("Voice: muted");
        }
        return meta;
    }

    public static void generateParticleArroundPlayer(Player player, double radius) {
        int points = 50;
        Location origin = player.getLocation();
        for (int i = 0; i < points; i++) {
            double angle = Math.toRadians(i * (380.0 / points));
            Location point = origin.clone().add(
                    radius * Math.cos(angle), 0.0D, radius * Math.sin(angle));
            player.spawnParticle(Particle.FLAME, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static String addressOf(Player player) {
        if (player.getAddress() == null || player.getAddress().getAddress() == null) {
            return null;
        }
        return player.getAddress().getAddress().getHostAddress();
    }
}
