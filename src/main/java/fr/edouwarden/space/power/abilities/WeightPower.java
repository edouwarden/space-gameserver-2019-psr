package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.power.WeaponFx;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Graviton Press: a mass dropped from a fixed altitude.
 *
 * <p>The impact energy is {@code E = m g h}, the shock radius grows as
 * {@code r_s = k sqrt(h)} and the pressure at a point falls as
 * {@code D = D0 h / (r_s^2 + 1)}.</p>
 */
public class WeightPower implements Power {

    private static final double DROP_HEIGHT = 9.0D;
    private static final double SHOCK_FACTOR = 0.9D;
    private static final double DAMAGE_FACTOR = 5.0D;

    @Override
    public boolean useSkills(PlayerEntity playerEntity) {
        final Player player = playerEntity.getBukkitPL();
        if (player.getItemInHand().getType() != Material.ANVIL) {
            return false;
        }

        final Location aim = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(4.0D));
        final Location ground = WeaponFx.ground(aim);
        final Location start = ground.clone().add(0.0D, DROP_HEIGHT, 0.0D);

        WeaponFx.line(player.getWorld(), ground.clone().add(0.0D, 0.2D, 0.0D), start,
                18, Particle.END_ROD);
        player.getWorld().playSound(start, Sound.BLOCK_BEACON_ACTIVATE, 0.6F, 0.7F);

        final FallingBlock mass = player.getWorld().spawnFallingBlock(start, Material.ANVIL, (byte) 0);
        mass.setDropItem(false);
        mass.setVelocity(new Vector(0.0D, -0.15D, 0.0D));

        new BukkitRunnable() {
            private int ticks = 0;
            private Location last = start.clone();

            @Override
            public void run() {
                if (!mass.isValid() || mass.isDead() || mass.isOnGround() || ticks > 80) {
                    impact(player, last, ground);
                    if (mass.isValid()) {
                        mass.remove();
                    }
                    cancel();
                    return;
                }
                last = mass.getLocation().clone();
                player.getWorld().spawnParticle(Particle.DRAGON_BREATH, last, 2, 0.15D, 0.15D, 0.15D, 0.0D);
                ticks++;
            }
        }.runTaskTimer(General.plugin, 1L, 1L);
        return true;
    }

    private void impact(Player player, Location centre, Location ground) {
        double height = Math.max(1.0D, centre.getY() - ground.getY());
        double radius = SHOCK_FACTOR * Math.sqrt(height);
        double damage = DAMAGE_FACTOR * height / (radius * radius + 1.0D);

        Location flat = new Location(ground.getWorld(), centre.getX(), ground.getY() + 0.15D, centre.getZ());
        WeaponFx.ring(ground.getWorld(), flat, new Vector(1.0D, 0.0D, 0.0D),
                new Vector(0.0D, 0.0D, 1.0D), radius, 56, Particle.EXPLOSION_NORMAL, 1);
        WeaponFx.ring(ground.getWorld(), flat, new Vector(1.0D, 0.0D, 0.0D),
                new Vector(0.0D, 0.0D, 1.0D), radius * 0.6D, 36, Particle.CRIT, 1);
        ground.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, flat, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        ground.getWorld().playSound(flat, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 0.6F);
        WeaponFx.damage(player, flat, Math.max(1.5D, radius), damage);
    }


    /**
     * Landing hook used by {@code EntityDamage}. The fall distance is the same
     * altitude term as the dropped mass, so the ring and the damage share one
     * curve.
     */
    public static void touchGround(Player player) {
        double height = Math.max(1.0D, player.getFallDistance());
        double radius = SHOCK_FACTOR * Math.sqrt(height);
        double damage = DAMAGE_FACTOR * height / (radius * radius + 1.0D);
        Location flat = WeaponFx.ground(player.getLocation());
        WeaponFx.ring(flat.getWorld(), flat, new Vector(1.0D, 0.0D, 0.0D),
                new Vector(0.0D, 0.0D, 1.0D), radius, 40, Particle.EXPLOSION_NORMAL, 1);
        WeaponFx.damage(player, flat, Math.max(1.5D, radius), damage);
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "weight";
    }

    @Override
    public boolean fallingDamage() {
        return false;
    }
}
