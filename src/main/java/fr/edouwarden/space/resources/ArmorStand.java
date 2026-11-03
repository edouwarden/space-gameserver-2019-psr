package fr.edouwarden.space.resources;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class ArmorStand {
    public static org.bukkit.entity.ArmorStand addItem(Location loc, Material material,boolean small,Float armAngle) {
        org.bukkit.entity.ArmorStand as = (org.bukkit.entity.ArmorStand) loc.getWorld().spawn(loc, org.bukkit.entity.ArmorStand.class);

        as.setGravity(false);
        as.setCanPickupItems(false);
        as.setVisible(false);
        as.setArms(true);
        as.setSmall(small);
        as.setBasePlate(false);
        as.setItemInHand(new ItemStack(material));
        as.setCollidable(false);
        as.setInvulnerable(true);
        as.setRightArmPose(new EulerAngle(armAngle,0,0));
        return as;
    }
}
