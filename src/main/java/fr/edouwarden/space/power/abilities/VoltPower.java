package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.fight.Damage;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.power.WeaponFx;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Arc Lance: a chain of deterministic arcs.
 *
 * <p>Each arc is a straight chord with a sinusoidal lateral offset. The
 * delivered voltage falls exponentially with distance,
 * {@code V(d) = V0 exp(-lambda d)}, so the first link hits hardest.</p>
 */
public class VoltPower implements Power {

    private static final double MAX_RANGE = 16.0D;
    private static final double MAX_VERTICAL = 10.0D;
    private static final double LAMBDA = 0.12D;
    private static final double VOLTAGE = 9.0D;
    private static final int MAX_LINKS = 3;

    @Override
    public boolean useSkills(PlayerEntity playerEntity) {
        Player player = playerEntity.getBukkitPL();
        if (player.getItemInHand().getType() != Material.END_ROD) {
            return false;
        }

        Location start = player.getEyeLocation();
        List<LivingEntity> targets = WeaponFx.targets(player, MAX_RANGE, MAX_VERTICAL);
        Set<LivingEntity> visited = new HashSet<>();

        if (targets.isEmpty()) {
            Location end = start.clone().add(start.getDirection().normalize().multiply(10.0D));
            arc(player, start, end, 0.0D);
            WeaponFx.ring(player.getWorld(), end, WeaponFx.right(start.getDirection()),
                    WeaponFx.up(start.getDirection(), WeaponFx.right(start.getDirection())),
                    1.3D, 32, Particle.CRIT_MAGIC, 1);
            player.getWorld().playSound(end, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.5F, 1.6F);
            return true;
        }

        Location from = start;
        int links = 0;
        for (LivingEntity target : targets) {
            if (links >= MAX_LINKS || visited.contains(target)) {
                break;
            }
            visited.add(target);
            Location to = target.getLocation().add(0.0D, 1.0D, 0.0D);
            arc(player, from, to, links * 0.7D);
            double distance = from.distance(to);
            double damage = VOLTAGE * Math.exp(-LAMBDA * distance);
            if (!target.equals(player)) {
                Damage.attack(target, damage);
            }
            player.getWorld().playSound(to, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.45F, 1.7F);
            from = to;
            links++;
        }
        return true;
    }

    private void arc(Player player, Location from, Location to, double phase) {
        Vector direction = to.toVector().subtract(from.toVector());
        double length = direction.length();
        if (length < 0.1D) {
            return;
        }
        Vector unit = direction.clone().normalize();
        Vector right = WeaponFx.right(unit);
        Vector up = WeaponFx.up(unit, right);
        int points = Math.max(12, (int) Math.round(length * 4.0D));
        for (int i = 0; i <= points; i++) {
            double t = (double) i / points;
            double offset = Math.sin(t * Math.PI * 7.0D + phase) * 0.18D * Math.sin(t * Math.PI);
            Location point = from.clone()
                    .add(direction.clone().multiply(t))
                    .add(right.clone().multiply(offset))
                    .add(up.clone().multiply(offset * 0.45D));
            player.getWorld().spawnParticle(Particle.CRIT_MAGIC, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            if (i % 2 == 0) {
                player.getWorld().spawnParticle(Particle.END_ROD, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "volt";
    }
}
