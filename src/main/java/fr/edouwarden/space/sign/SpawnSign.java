package fr.edouwarden.space.sign;

import fr.edouwarden.space.variables.TChat;
import fr.edouwarden.space.voyage.SpaceSession;
import fr.edouwarden.space.voyage.VoyageState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Sign-driven ship spawning for the space layer.
 *
 * <p>A sign is first claimed by writing {@code [Space]} on its first line. The next
 * interaction turns it into a slot marker, and the interaction after that parks a ship.
 * The cooldown is per player and kept in {@link VoyageState}.</p>
 */
public final class SpawnSign {

    private static final String MARKER = ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "[Orbit]";
    private static final String CLAIMED = ChatColor.BOLD + "" + ChatColor.DARK_RED + "Orbiter:";
    private static final String SETUP_TOKEN = "[Space]";
    private static final int COOLDOWN_SECONDS = 5;

    private SpawnSign() {
    }

    public static boolean isSpaceSign(Sign sign) {
        return sign.getLine(0).equals(SETUP_TOKEN)
                || sign.getLine(1).contains(MARKER)
                || sign.getLine(1).contains(CLAIMED);
    }

    public static void clear(Block block) {
        if (!(block.getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) block.getState();
        sign.setLine(1, MARKER);
        sign.setLine(2, "");
        sign.update();
    }

    public static void setup(Sign sign) {
        sign.setLine(0, "");
        sign.setLine(1, MARKER);
        sign.update();
    }

    public static void define(Sign sign, Player player) {
        sign.setLine(1, CLAIMED);
        sign.setLine(2, ChatColor.BOLD + player.getName());
        sign.update();
    }

    public static boolean click(Player player, Sign sign) {
        if (sign.getLine(0).equals(SETUP_TOKEN)) {
            setup(sign);
            return true;
        }
        if (sign.getLine(1).contains(MARKER)) {
            if (cooldownActive(player)) {
                return true;
            }
            Location slot = sign.getLocation();
            int rotation = rotation(sign.getBlock().getState().getData().toString());
            SpaceSession.spawnAtSign(player, slot, rotation, 1);
            VoyageState.COOLDOWNS.put(player.getUniqueId(), System.currentTimeMillis());
            define(sign, player);
            return true;
        }
        if (sign.getLine(1).contains(CLAIMED)) {
            player.sendMessage(TChat.CannotSpawnShip());
            return true;
        }
        return false;
    }

    public static int rotation(String facing) {
        if (facing.contains("EAST")) {
            return 0;
        }
        if (facing.contains("WEST")) {
            return 180;
        }
        if (facing.contains("NORTH")) {
            return 90;
        }
        return 270;
    }

    private static boolean cooldownActive(Player player) {
        UUID uuid = player.getUniqueId();
        Long since = VoyageState.COOLDOWNS.get(uuid);
        if (since == null) {
            return false;
        }
        long left = since / 1000L + COOLDOWN_SECONDS - System.currentTimeMillis() / 1000L;
        if (left > 0) {
            player.sendMessage(TChat.TimeLeft(left));
            return true;
        }
        return false;
    }
}
