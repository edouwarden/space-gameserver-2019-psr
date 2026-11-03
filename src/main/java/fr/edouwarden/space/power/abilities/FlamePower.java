package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.power.WeaponFx;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Plasma Array: three interleaved helical filaments.
 *
 * <p>The emitter advances along the look axis while each filament follows
 * {@code x = r cos(omega t)}, {@code z = r sin(omega t)} and
 * {@code y = v t}; the radius grows linearly with the same parameter.</p>
 */
public class FlamePower implements Power {

    private static final double START_RADIUS = 0.36D;
    private static final double RADIUS_GROWTH = 0.045D;
    private static final double ANGULAR_SPEED = 0.62D;
    private static final double FORWARD_SPEED = 0.20D;
    private static final int STEPS = 52;

    @Override
    public boolean useSkills(PlayerEntity playerEntity) {
        final Player player = playerEntity.getBukkitPL();
        if (player.getItemInHand().getType() != Material.BLAZE_ROD) {
            return false;
        }

        final Location eye = player.getEyeLocation();
        final Vector direction = eye.getDirection().normalize();
        final Vector right = WeaponFx.right(direction);
        final Vector up = WeaponFx.up(direction, right);

        player.getWorld().playSound(eye, Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.35F);

        new BukkitRunnable() {
            private int step = 0;

            @Override
            public void run() {
                if (step > STEPS || !player.isOnline()) {
                    cancel();
                    return;
                }
                double radius = START_RADIUS + RADIUS_GROWTH * step;
                double angle = ANGULAR_SPEED * step;
                Location axis = eye.clone().add(direction.clone().multiply(FORWARD_SPEED * step));

                for (int filament = 0; filament < 3; filament++) {
                    double phase = angle + filament * 2.0D * Math.PI / 3.0D;
                    Location point = axis.clone()
                            .add(right.clone().multiply(radius * Math.cos(phase)))
                            .add(up.clone().multiply(radius * Math.sin(phase)));
                    player.getWorld().spawnParticle(Particle.FLAME, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    if (step % 2 == 0) {
                        player.getWorld().spawnParticle(Particle.END_ROD, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
                player.getWorld().spawnParticle(Particle.END_ROD, axis, 1, 0.0D, 0.0D, 0.0D, 0.0D);

                if (step == STEPS) {
                    impact(player, axis, right, up);
                }
                step++;
            }
        }.runTaskTimer(General.plugin, 0L, 1L);
        return true;
    }

    private void impact(Player player, Location centre, Vector right, Vector up) {
        WeaponFx.ring(player.getWorld(), centre, right, up, 1.7D, 48, Particle.FLAME, 1);
        WeaponFx.ring(player.getWorld(), centre, right, up, 1.2D, 36, Particle.DRAGON_BREATH, 1);
        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, centre, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        player.getWorld().playSound(centre, Sound.ENTITY_GENERIC_EXPLODE, 0.7F, 1.8F);
        // D(d) = D0 (1 - d/R), with D0 = 6 and R = 2.5.
        WeaponFx.damage(player, centre, 2.5D, 6.0D);
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "flame";
    }
}
