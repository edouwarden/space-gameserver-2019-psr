package fr.edouwarden.space.ship;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.edouwarden.space.variables.General;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

import static fr.edouwarden.space.variables.General.item;

public class BoardingShipGUI implements InventoryProvider {
    public static String button = "Voyager";

    public static String wait = "You need a navigator.";
    private final Random random = new Random();
    private final ShipEntity ship;

    public BoardingShipGUI(ShipEntity ship) {
        this.ship = ship;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(item(Material.BLUE_STAINED_GLASS_PANE, " ")));
        defaultSetup(contents);

    }
    public void defaultSetup(InventoryContents contents){
        contents.set(0, 3, ClickableItem.empty(General.item(Material.LEGACY_BARRIER,wait)));
        contents.set(0, 5, ClickableItem.empty(General.item(Material.PLAYER_HEAD," ")));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);
        if(state % 5 != 0)
            return;
        if(ship.getNavigator()!=null){
            contents.set(0,5,ClickableItem.of(item(Material.PLAYER_HEAD, player.getName()),e -> {
                ship.setNavigator(null);
                defaultSetup(contents);
            }));
            contents.set(0,3,ClickableItem.of(item(Material.BIRCH_BOAT, button),e -> {
                contents.inventory().getManager().getOpenedPlayers(contents.inventory()).forEach( p ->p.closeInventory());
                ship.movetoSpace();
            }));
        }
    }

}
