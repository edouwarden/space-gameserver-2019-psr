package fr.edouwarden.space.variables.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Tab-completion data for {@code /space}.
 */
public final class ArgumentCompletion {

    private ArgumentCompletion() {
    }

    // BaseCommand
    public static final List<String> base1 = Arrays.asList(
            "ko", "worlds", "weapon", "bounty", "power", "meta", "map",
            "jobs", "loopspeed", "raid", "rpk");

    // KOCommand
    public static final List<String> ko1 = Arrays.asList("set", "remove");

    // POWERCommand
    public static final List<String> power1 = Arrays.asList("set", "remove", "forceSet");
    public static final List<String> power3 = Arrays.asList(
            "sand", "magma", "flame", "quake", "bomb", "swamp", "snow",
            "smoke", "ice", "light", "clone", "weight", "phase", "void", "volt");

    // JOBSCommand
    public static final List<String> jobs1 = Arrays.asList("set", "remove");
    public static final List<String> jobs3 = Arrays.asList(
            "fabricator", "nutrient", "sentinel", "astrogator", "stationmaster");

    // WEAPONCommand
    public static final List<String> weapon1 = Arrays.asList("give");
    public static final List<String> weapon2 = Arrays.asList("katana");
    public static final List<String> weapon3 = Arrays.asList("T1");

    // RPKCommand
    public static final List<String> rpk1 = Arrays.asList("ROLEPLAYKILL");
    public static final List<String> rpk2 = Arrays.asList("<player>");
}
