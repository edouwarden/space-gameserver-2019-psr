package fr.edouwarden.space;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.voice.Voice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Main game loop: refreshes proximity-voice routing for every online player.
 */
public final class Loop {

    private Loop() {
    }

    public static void loop(Main plugin) {
        long period = Math.max(20L, General.loopspeed / 50L);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Voice.update(PlayerEntity.getPlayerEntity(player));
            }
        }, 20L, period);
    }
}
