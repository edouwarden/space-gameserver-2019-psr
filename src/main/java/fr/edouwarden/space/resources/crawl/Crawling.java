package fr.edouwarden.space.resources.crawl;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Visual crawl mode: a barrier block is shown above the player's head while the
 * player is crawling. The real world is never modified.
 *
 * <p>The package is kept for the movement listener hook; no command currently
 * calls {@link #beginCrawling(Player)}.</p>
 */
public final class Crawling {

    private static final double Y_OFFSET = 1.6;
    private static final BlockData CEILING = Material.BARRIER.createBlockData();
    private static final HashMap<Player, List<Block>> CRAWLING = new HashMap<>();

    private Crawling() {
    }

    public static void beginCrawling(Player player) {
        CRAWLING.putIfAbsent(player, new ArrayList<>());
        player.setGliding(true);
    }

    public static void stopCrawling(Player player) {
        List<Block> blocks = CRAWLING.remove(player);
        if (blocks == null) {
            return;
        }
        for (Block block : blocks) {
            player.sendBlockChange(block.getLocation(), block.getBlockData());
        }
        player.setGliding(false);
    }

    public static boolean isCrawling(Player player) {
        return CRAWLING.containsKey(player);
    }

    /**
     * Refreshes the fake barrier above the player after a movement.
     */
    public static void updateBarrierCeiling(Player player) {
        List<Block> previous = CRAWLING.getOrDefault(player, new ArrayList<>());
        Block above = player.getLocation().clone().add(0, Y_OFFSET, 0).getBlock();

        List<Block> ceiling = new ArrayList<>();
        if (above.isPassable()) {
            ceiling.add(above);
        }

        for (Block block : previous) {
            if (!ceiling.contains(block)) {
                player.sendBlockChange(block.getLocation(), block.getBlockData());
            }
        }
        for (Block block : ceiling) {
            if (!previous.contains(block)) {
                player.sendBlockChange(block.getLocation(), CEILING);
            }
        }
        CRAWLING.put(player, ceiling);
    }
}
