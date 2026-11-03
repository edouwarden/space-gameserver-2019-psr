package fr.edouwarden.space.ship;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static fr.edouwarden.space.variables.General.item;

public class ShipGUI implements InventoryProvider {
    public static String buttonLeft = "Gauche";
    public static String buttonRight = "Droite";
    private final ShipEntity ship;

    public ShipGUI(ShipEntity ship) {
        this.ship = ship;
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(item(Material.GRAY_STAINED_GLASS, " ")));
        contents.set(0,2,ClickableItem.of(item(Material.BONE,buttonLeft),e -> {
            ship.vRotAddition(-1);
        }));
        contents.set(0,6,ClickableItem.of(item(Material.BONE,buttonRight),e -> {
            ship.vRotAddition(1);
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
