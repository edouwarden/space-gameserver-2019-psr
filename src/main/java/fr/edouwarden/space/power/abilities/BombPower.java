package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.fight.Damage;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.power.WeaponFx;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Fracture Charge: an arcing charge that breaks into a forward cone.
 *
 * <p>The fragment count follows {@code N(theta) = N0 cos^2(theta)} and the
 * damage follows {@code D(theta) = D0 cos(theta)}. Both are evaluated on a
 * fixed angular sweep, so the cone is identical for every cast.</p>
 */
public class BombPower implements Power {

    private static final double RANGE = 6.0D;
    private static final double ARC_HEIGHT = 2.4D;
    private static final int FLIGHT_TICKS = 24;
    private static final double CONE_RADIUS = 5.0D;
    private static final double MAX_DAMAGE = 7.0D;

    @Override
    public boolean useSkills(PlayerEntity playerEntity) {
        final Player player = playerEntity.getBukkitPL();
        if (player.getItemInHand().getType() != Material.FIRE_CHARGE) {
            return false;
        }

        final Location start = player.getEyeLocation();
        final Vector direction = start.getDirection().normalize();
        final Location aim = start.clone().add(direction.clone().multiply(RANGE));
        final Location impact = WeaponFx.ground(aim);

        new BukkitRunnable() {
            private int step = 0;

            @Override
            public void run() {
                if (!player.isOnline() || step > FLIGHT_TICKS) {
                    cancel();
                    return;
                }
                double t = (double) step / FLIGHT_TICKS;
                Location point = start.clone()
                        .add(direction.clone().multiply(RANGE * t));
                point.setY(point.getY() + ARC_HEIGHT * Math.sin(Math.PI * t)
                        + (impact.getY() - point.getY()) * t * t);

                player.getWorld().spawnParticle(Particle.FLAME, point, 2, 0.05D, 0.05D, 0.05D, 0.0D);
                player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, point, 1, 0.02D, 0.02D, 0.02D, 0.0D);
                if (step % 3 == 0) {
                    player.getWorld().spawnParticle(Particle.END_ROD, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }

                if (step == FLIGHT_TICKS) {
                    fracture(player, impact, direction);
                }
                step++;
            }
        }.runTaskTimer(General.plugin, 0L, 1L);
        player.getWorld().playSound(start, Sound.ENTITY_GHAST_SHOOT, 0.6F, 1.6F);
        return true;
    }

    private void fracture(Player player, Location impact, Vector direction) {
        player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, impact, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, impact, 24, 0.4D, 0.2D, 0.4D, 0.0D);
        player.getWorld().playSound(impact, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.1F);

        Vector flat = new Vector(direction.getX(), 0.0D, direction.getZ());
        if (flat.lengthSquared() < 1.0E-6D) {
            flat = new Vector(0.0D, 0.0D, 1.0D);
        }
        flat.normalize();

        // Fixed angular sweep, from -80 to +80 degrees in 4-degree steps.
        for (int degrees = -80; degrees <= 80; degrees += 4) {
            double theta = Math.toRadians(degrees);
            double cosine = Math.cos(theta);
            int fragments = (int) Math.round(6.0D * cosine * cosine);
            Vector ray = rotate(flat, theta);
            for (int i = 0; i < fragments; i++) {
                double distance = 0.4D + 0.30D * i;
                Location point = impact.clone().add(ray.clone().multiply(distance));
                point.setY(impact.getY() + 0.1D + 0.35D * Math.abs(Math.sin(i * 0.9D)));
                player.getWorld().spawnParticle(Particle.CRIT, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        for (Entity entity : player.getWorld().getNearbyEntities(impact, CONE_RADIUS, CONE_RADIUS, CONE_RADIUS)) {
            if (entity.equals(player) || !(entity instanceof LivingEntity) || ((LivingEntity) entity).isDead()) {
                continue;
            }
            Vector toEntity = entity.getLocation().toVector().subtract(impact.toVector());
            double distance = toEntity.length();
            if (distance > CONE_RADIUS || distance < 0.1D) {
                continue;
            }
            Vector flatToEntity = new Vector(toEntity.getX(), 0.0D, toEntity.getZ()).normalize();
            double cosine = flat.dot(flatToEntity);
            if (cosine <= 0.0D) {
                continue;
            }
            double damage = MAX_DAMAGE * cosine * (1.0D - distance / CONE_RADIUS);
            if (damage > 0.0D) {
                Damage.attack(entity, damage);
            }
        }
    }


    /**
     * Legacy projectile hook kept for the snowball listener. The current
     * Fracture Charge uses the deterministic flight above, but a projectile
     * impact still resolves to the same radial burst.
     */
    public static void projectile(org.bukkit.entity.EntityType type, org.bukkit.block.Block block,
                                  Entity entity) {
        Location centre = block != null ? block.getLocation().add(0.5D, 0.5D, 0.5D)
                : entity != null ? entity.getLocation() : null;
        if (centre == null) {
            return;
        }
        centre.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, centre, 16, 0.3D, 0.2D, 0.3D, 0.0D);
    }

    private Vector rotate(Vector vector, double angle) {
        double cosine = Math.cos(angle);
        double sine = Math.sin(angle);
        return new Vector(vector.getX() * cosine - vector.getZ() * sine,
                0.0D,
                vector.getX() * sine + vector.getZ() * cosine).normalize();
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "bomb";
    }
}
