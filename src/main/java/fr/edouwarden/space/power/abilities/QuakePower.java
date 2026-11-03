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

/**
 * Seismic Driver: a travelling ground wave.
 *
 * <p>The surface follows {@code y(r,t) = A sin(k r - omega t) exp(-r / kappa)}.
 * The front advances linearly and the amplitude decays with distance, so the
 * visible ripple and the damage falloff come from the same curve.</p>
 */
public class QuakePower implements Power {

    private static final double AMPLITUDE = 0.55D;
    private static final double WAVE_NUMBER = 1.1D;
    private static final double ANGULAR_SPEED = 0.55D;
    private static final double DECAY = 9.0D;
    private static final double FRONT_SPEED = 0.32D;
    private static final int DURATION = 44;

    @Override
    public boolean useSkills(PlayerEntity playerEntity) {
        final Player player = playerEntity.getBukkitPL();
        if (player.getItemInHand().getType() != Material.DIAMOND_SHOVEL) {
            return false;
        }

        final Location centre = WeaponFx.ground(player.getLocation());
        player.getWorld().playSound(centre, Sound.BLOCK_ANVIL_LAND, 1.0F, 0.55F);

        new BukkitRunnable() {
            private int tick = 0;
            private boolean damaged = false;

            @Override
            public void run() {
                if (!player.isOnline() || tick > DURATION) {
                    cancel();
                    return;
                }

                double front = 1.0D + FRONT_SPEED * tick;
                // One particle-thin ring. Vanilla keeps each particle alive for
                // about a second, so extra radius samples turn the wave into a
                // sheet; a single front keeps the ring legible as it travels.
                double envelope = Math.exp(-front / DECAY);
                int points = Math.max(10, (int) Math.round(front * 3.0D));
                double height = AMPLITUDE
                        * Math.sin(WAVE_NUMBER * front - ANGULAR_SPEED * tick)
                        * envelope;
                for (int i = 0; i < points; i++) {
                    double angle = 2.0D * Math.PI * i / points;
                    Location point = centre.clone().add(
                            front * Math.cos(angle),
                            0.15D + height,
                            front * Math.sin(angle));
                    player.getWorld().spawnParticle(Particle.CRIT_MAGIC, point, 1,
                            0.0D, 0.0D, 0.0D, 0.0D);
                    if (i % 10 == 0) {
                        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, point, 1,
                                0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }

                if (!damaged && tick == 6) {
                    damaged = true;
                    applyDamage(player, centre);
                }
                tick++;
            }
        }.runTaskTimer(General.plugin, 0L, 1L);
        return true;
    }

    private void applyDamage(Player player, Location centre) {
        double radius = 8.0D;
        for (Entity entity : player.getWorld().getNearbyEntities(centre, radius, 4.0D, radius)) {
            if (entity.equals(player) || !(entity instanceof LivingEntity) || ((LivingEntity) entity).isDead()) {
                continue;
            }
            double distance = entity.getLocation().distance(centre);
            if (distance > radius) {
                continue;
            }
            double damage = 8.0D * Math.exp(-distance / DECAY);
            if (damage > 0.1D) {
                Damage.attack(entity, damage);
            }
        }
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "quake";
    }
}
