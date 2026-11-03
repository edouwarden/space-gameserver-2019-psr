package fr.edouwarden.space.event.onplayer;

import com.sk89q.worldedit.MaxChangedBlocksException;
import fr.edouwarden.space.gui.InventoryManager;
import fr.edouwarden.space.player.PlayerEntity;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIEvent implements Listener {
    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent event) throws IOException, MaxChangedBlocksException {
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();

        Player player = (Player) event.getWhoClicked();
        if (!fr.xephi.authme.api.v3.AuthMeApi.getInstance().isAuthenticated(player))return;
        InventoryAction action = event.getAction();
        if (player.isSneaking())event.setResult(Event.Result.DENY);

        if(!(player.getGameMode() == GameMode.CREATIVE)) {
            if (bottom.getType() == InventoryType.PLAYER) {//(event.getCursor() != null && (event.getCursor().getType() != Material.AIR) || event.isShiftClick()|| action== InventoryAction.HOTBAR_SWAP) &&event.getSlot() >=0&&event.getSlot() <=40)
                ItemStack cursor = event.getCursor();
                if (action == InventoryAction.PLACE_ALL && event.getSlot() != -999 || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME || action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.SWAP_WITH_CURSOR){//|| action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {

                    if (cursor != null && cursor.getType() != Material.AIR && (event.getSlotType() == InventoryType.SlotType.CONTAINER || event.getSlotType() == InventoryType.SlotType.QUICKBAR)) {
                        if (InventoryManager.itemMove(cursor, event.getSlot())) {
                            event.setResult(Event.Result.DENY);
                            //event.setCursor(cursor);
                        }
                    } else if (action == InventoryAction.HOTBAR_SWAP) {
                        if (InventoryManager.itemMove(cursor, event.getHotbarButton())) {
                            event.setResult(Event.Result.DENY);
                            //event.setCursor(cursor);
                        }
                    }

                } else if (!(action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME)) {
                    if (cursor != null && cursor.getType() != Material.AIR) {
                        event.setResult(Event.Result.DENY);
                        event.setCursor(cursor);

                    } else
                        event.setCancelled(true);
                }

            }
        }
        if (bottom.getType() == InventoryType.PLAYER || top.getType() == InventoryType.CREATIVE || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;//TODO  check




    }
    @EventHandler
    public void onInventoryDragItem(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void onItemSwapHand(PlayerSwapHandItemsEvent event){
        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
         if (playerEntity.hasPower()) {
            if (playerEntity.haveSwitchNonAbilities()) {
                List<ItemStack> hotbar = playerEntity.getSwitchNonAbilities();
                for (int i = 1; i < 9; i++) {
                    player.getInventory().setItem(i, hotbar.get(i-1));
                }
                playerEntity.setSwitchNonAbilities(null);
            } else {
                List<ItemStack> hotbar = new ArrayList<>();
                for (int i = 1; i < 9; i++) {
                    ItemStack item = player.getInventory().getItem(i);
                    if (item == null)item= new ItemStack(Material.AIR);
                    hotbar.add(item );
                }
                for (int i = 1; i < 9; i++) {
                    ItemStack item = player.getInventory().getItem(i+9);
                    if (item == null)item= new ItemStack(Material.AIR);
                    player.getInventory().setItem(i, item);
                }
                playerEntity.setSwitchNonAbilities(hotbar);

            }

        }
    }
}
