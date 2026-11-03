package fr.edouwarden.space.voyage;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-player voyage state shared by the sign flow and the space session.
 * Replaces the ad-hoc maps that used to live in a class called {@code HashMap}.
 */
public final class VoyageState {

    public static final Map<UUID, Long> COOLDOWNS = new ConcurrentHashMap<>();
    public static final Map<UUID, List<Integer>> SPAWN_DATA = new ConcurrentHashMap<>();
    public static final Map<UUID, Boolean> IN_SPACE = new ConcurrentHashMap<>();
    public static final Map<UUID, Boolean> CONFIRMED = new ConcurrentHashMap<>();

    private VoyageState() {
    }

    public static boolean isInSpace(UUID uuid) {
        return IN_SPACE.getOrDefault(uuid, false);
    }

    public static void clear(UUID uuid) {
        COOLDOWNS.remove(uuid);
        SPAWN_DATA.remove(uuid);
        IN_SPACE.remove(uuid);
        CONFIRMED.remove(uuid);
    }
}
