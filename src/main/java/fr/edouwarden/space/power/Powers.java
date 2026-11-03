package fr.edouwarden.space.power;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.abilities.BombPower;
import fr.edouwarden.space.power.abilities.ClonePower;
import fr.edouwarden.space.power.abilities.FlamePower;
import fr.edouwarden.space.power.abilities.IcePower;
import fr.edouwarden.space.power.abilities.LightPower;
import fr.edouwarden.space.power.abilities.MagmaPower;
import fr.edouwarden.space.power.abilities.PhasePower;
import fr.edouwarden.space.power.abilities.QuakePower;
import fr.edouwarden.space.power.abilities.SandPower;
import fr.edouwarden.space.power.abilities.SmokePower;
import fr.edouwarden.space.power.abilities.SnowPower;
import fr.edouwarden.space.power.abilities.SwampPower;
import fr.edouwarden.space.power.abilities.VoidPower;
import fr.edouwarden.space.power.abilities.VoltPower;
import fr.edouwarden.space.power.abilities.WeightPower;
import fr.edouwarden.space.variables.General;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Registry and lifecycle for weapon powers.
 *
 * <p>The original build resolved abilities by reflection on a generated class name.
 * That convention only matched the old ability names, so the registry is now explicit:
 * a stable id maps to a constructor, and stored player data keeps the id.</p>
 */
public final class Powers {

    private static final Map<String, Supplier<Power>> REGISTRY = new LinkedHashMap<>();

    static {
        register("sand", SandPower::new);
        register("magma", MagmaPower::new);
        register("flame", FlamePower::new);
        register("quake", QuakePower::new);
        register("bomb", BombPower::new);
        register("swamp", SwampPower::new);
        register("snow", SnowPower::new);
        register("smoke", SmokePower::new);
        register("ice", IcePower::new);
        register("light", LightPower::new);
        register("clone", ClonePower::new);
        register("weight", WeightPower::new);
        register("phase", PhasePower::new);
        register("void", VoidPower::new);
        register("volt", VoltPower::new);
    }

    private Powers() {
    }

    private static void register(String id, Supplier<Power> factory) {
        REGISTRY.put(id, factory);
    }

    public static Map<String, Supplier<Power>> registry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static boolean exists(String id) {
        return id != null && REGISTRY.containsKey(normalize(id));
    }

    public static Power create(String id) {
        if (id == null) {
            return null;
        }
        Supplier<Power> factory = REGISTRY.get(normalize(id));
        return factory == null ? null : factory.get();
    }

    public static void addToPlayers(PlayerEntity player, String id, boolean force) {
        Power power = create(id);
        if (power == null) {
            General.console.log(Level.WARNING, "Unknown power id: " + id);
            return;
        }
        if (player.hasPower() && !force) {
            General.console.log(Level.WARNING, player.getBukkitPL().getName()
                    + " tried to acquire " + id
                    + " while already holding " + player.getPower().getName());
            PlayerEntity.execRPK(player.getBukkitPL());
            // execRPK replaces the linked entity, so resolve it again before assigning.
            player = PlayerEntity.getPlayerEntity(player.getBukkitPL());
            if (player == null) {
                return;
            }
        }
        player.setPower(power);
    }

    public static void removeFromPlayers(PlayerEntity player) {
        player.clearPower();
    }

    public static boolean use(PlayerEntity player) {
        Power power = player.getPower();
        if (power == null) {
            return false;
        }
        // Abilities are suppressed while the player is submerged.
        if (player.getBukkitPL().getLocation().add(0, 1, 0).getBlock().getType() == Material.WATER) {
            return false;
        }
        return power.useSkills(player);
    }

    /**
     * Inventory marker for a power item. The original build used a dead bush as the
     * placeholder object; the item type is kept so existing inventories still work.
     */
    public static boolean isAPower(ItemStack item) {
        return item != null && item.getType() == Material.DEAD_BUSH;
    }

    public static boolean isOne(ItemStack item) {
        if (item == null || item.getType() != Material.APPLE || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName()
                && General.powerDisplayName.equals(meta.getDisplayName());
    }

    private static String normalize(String id) {
        return id.toLowerCase(Locale.ROOT);
    }
}
