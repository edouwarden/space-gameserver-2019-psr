package fr.edouwarden.space.resources;

import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Visual knock-out effect: the player is held in place behind a barrier block
 * for a short animated duration.
 *
 * <p>The task must run on the server thread: {@code setSwimming} and
 * {@code sendBlockChange} both touch the player entity, which Bukkit only allows
 * synchronously. The prototype used an async repeating task with a zero period,
 * which threw on every tick and never restored the player's state.</p>
 */
public final class Sleep {

    private static final int TICKS = 10;

    private Sleep() {
    }

    @SuppressWarnings("deprecation")
    public static void doBedAction(final Player player, boolean extended) {
        final int ticks = extended ? TICKS * 2 : TICKS;
        final Block above = player.getLocation().add(0, 1, 0).getBlock();

        new BukkitRunnable() {
            private int remaining = ticks;

            @Override
            public void run() {
                if (remaining-- > 0) {
                    player.sendBlockChange(above.getLocation(), Material.BARRIER, (byte) 1);
                    player.setSwimming(true);
                    return;
                }

                player.setSwimming(false);
                // Spigot 1.14 has no BlockData overload; the byte data keeps the real state.
                player.sendBlockChange(above.getLocation(), above.getType(), above.getData());
                cancel();
            }
        }.runTaskTimer(General.plugin, 5L, 1L);
    }
}
