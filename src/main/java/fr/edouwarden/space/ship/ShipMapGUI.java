package fr.edouwarden.space.ship;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static fr.edouwarden.space.variables.General.item;

public class ShipMapGUI implements InventoryProvider {
    private final YamlConfiguration map;
    private final ShipEntity ship;
    boolean water =false;
    int item = 0;
    String line;
    float vlocX ;
    float vlocZ;

    public ShipMapGUI(YamlConfiguration map, ShipEntity ship){
        this.map = map;
        this.ship = ship;
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(item(Material.BLUE_STAINED_GLASS_PANE, "Space")));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);
        if(state % 5 != 0)
            return;

        vlocX = ship.getVlocX();
        vlocZ = ship.getVlocZ();
        for (int i = Math.round(vlocZ)-2; i < Math.round(vlocZ)+3; i++) {
            if(!map.contains(String.valueOf(i))) water=true;
            else line=map.getString(String.valueOf(i));
            for (int j = Math.round(vlocX)-4; j < Math.round(vlocZ)+4; j++) {
                char c = line.charAt(j);
                if(water || line.charAt(j)==' '||c=='X');//contents.set()inv.setItem(item,item(Material.BLUE_STAINED_GLASS_PANE,"Space"));
                else if (i==vlocX && j==vlocZ) contents.set(i,j,ClickableItem.empty(item(Material.BIRCH_BOAT,"Ship")));
                else contents.set(i,j,ClickableItem.empty(item(Material.GRASS_BLOCK,"Station")));//inv.setItem(item,item(Material.GRASS_BLOCK,"Space"));
                item++;

            }
            water =false;
        }
        contents.fillBorders(ClickableItem.empty(item(Material.BLUE_STAINED_GLASS_PANE,"Space")));

    }
}
