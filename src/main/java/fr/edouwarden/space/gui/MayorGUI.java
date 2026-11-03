package fr.edouwarden.space.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.edouwarden.space.jobs.Jobs;
import fr.edouwarden.space.player.JobsDatas;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MayorGUI implements InventoryProvider {



    private final Random random = new Random();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLUE_STAINED_GLASS_PANE)));

        contents.set(0, 1, ClickableItem.of(General.item(Material.FEATHER,"Collect rent"),
                e -> {
            PlayerEntity ocplayer = PlayerEntity.getPlayerEntity(player);
            JobsDatas jobdata = ocplayer.getJobDatas();
            if(jobdata.haveJob() && jobdata.getJob() == Jobs.STATIONMASTER){
                jobdata.getMayorStation().payOwnerDuty();
            }
                }));

        contents.set(0, 7, ClickableItem.of(new ItemStack(Material.BARRIER),
                e -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;

        short durability = (short) random.nextInt(15);

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, durability);
        contents.fillBorders(ClickableItem.empty(glass));
    }

}
