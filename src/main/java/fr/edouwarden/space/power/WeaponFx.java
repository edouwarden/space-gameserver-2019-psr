package fr.edouwarden.space.power;

import fr.edouwarden.space.fight.Damage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Deterministic particle geometry shared by the weapon systems.
 *
 * <p>Every point is produced from a closed-form expression: a ring, a helix or
 * a line. No sampling and no random offsets, so a cast looks the same on every
 * server tick and can be described by the same equations shown in the demo.</p>
 */
public final class WeaponFx {

    private WeaponFx() {
    }

    /** Horizontal right vector for a look direction, with a safe fallback. */
    public static Vector right(Vector direction) {
        Vector right = new Vector(-direction.getZ(), 0.0D, direction.getX());
        if (right.lengthSquared() < 1.0E-6D) {
            right = new Vector(1.0D, 0.0D, 0.0D);
        }
        return right.normalize();
    }

    public static Vector up(Vector direction, Vector right) {
        Vector up = right.clone().crossProduct(direction);
        if (up.lengthSquared() < 1.0E-6D) {
            up = new Vector(0.0D, 1.0D, 0.0D);
        }
        return up.normalize();
    }

    public static void ring(World world, Location centre, Vector right, Vector up,
                           double radius, int points, Particle particle, int count) {
        for (int i = 0; i < points; i++) {
            double angle = 2.0D * Math.PI * i / points;
            Location point = centre.clone()
                    .add(right.clone().multiply(radius * Math.cos(angle)))
                    .add(up.clone().multiply(radius * Math.sin(angle)));
            world.spawnParticle(particle, point, count, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void line(World world, Location from, Location to, int steps, Particle particle) {
        Vector delta = to.toVector().subtract(from.toVector());
        for (int i = 0; i <= steps; i++) {
            Location point = from.clone().add(delta.clone().multiply((double) i / steps));
            world.spawnParticle(particle, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    /** Living targets around a player, nearest first, with the caster excluded. */
    public static List<LivingEntity> targets(Player player, double range, double vertical) {
        List<LivingEntity> found = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(range, vertical, range)) {
            if (entity.equals(player) || !(entity instanceof LivingEntity)) {
                continue;
            }
            LivingEntity living = (LivingEntity) entity;
            if (living.isDead()) {
                continue;
            }
            found.add(living);
        }
        found.sort(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(player.getLocation())));
        return found;
    }

    /** Applies a linear falloff from {@code maxDamage} at the centre to zero at {@code radius}. */
    public static void damage(Player source, Location centre, double radius, double maxDamage) {
        World world = centre.getWorld();
        if (world == null) {
            return;
        }
        for (Entity entity : world.getNearbyEntities(centre, radius, radius, radius)) {
            if (entity.equals(source) || !(entity instanceof LivingEntity) || ((LivingEntity) entity).isDead()) {
                continue;
            }
            double distance = entity.getLocation().distance(centre);
            double damage = maxDamage * (1.0D - distance / radius);
            if (damage > 0.0D) {
                Damage.attack(entity, damage);
            }
        }
    }

    public static Location ground(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return location;
        }
        int y = world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
        return new Location(world, location.getX(), y + 1.0D, location.getZ());
    }
}
