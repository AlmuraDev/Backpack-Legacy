package com.almuramc.backpack.listener;

import com.almuramc.backpack.BackpackPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class BackpackListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		InventoryView viewer = event.getView();
		Player player = (Player) event.getPlayer();
		Inventory backpack = viewer.getTopInventory();

		if (backpack.getHolder() == player && backpack.getTitle().equals("Backpack")) {
			BackpackPlugin.getInstance().getHandler().setBackpackFor(player, player.getWorld(), backpack);
		}
	}
}
