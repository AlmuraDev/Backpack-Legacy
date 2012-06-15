package com.almuramc.backpack.bukkit.listener;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class BackpackListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		InventoryView viewer = event.getView();
		Player player = (Player) event.getPlayer();
		Inventory backpack = viewer.getTopInventory();

		if (backpack.getHolder().equals(player) && backpack.getTitle().equals("Backpack")) {
			BackpackPlugin.getInstance().getCore().setBackpackFor(player, player.getWorld(), backpack);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		// World Detection & Config pull to see if player can share bp inventory.
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		// Fire Save event in-case player gets kicked while having backpack open.	
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			// Handle player death.  If player has permission to keep items do nothing, if player doesnt, set backpack inventory to null.

		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		//This should cover our dependency issues
		BackpackPlugin.getInstance().getHooks().setup();
	}
}
