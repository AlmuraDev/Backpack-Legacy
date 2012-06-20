/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the GNU Public License version 3.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Backpack.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.almuramc.backpack.bukkit.listener;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.util.InventoryUtil;
import com.almuramc.backpack.bukkit.util.StorageUtil;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.ItemStack;

public class BackpackListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		onBackpackClose(event.getView(), (Player) event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		// World Detection & Config pull to see if player can share bp inventory.
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		onBackpackClose(event.getPlayer().getOpenInventory(), event.getPlayer());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (!player.hasPermission("backpack.keepitems")) {
				return;
			}
			Inventory inventory = StorageUtil.get(player, player.getWorld());
			if (inventory == null) {
				return;
			}
			ItemStack[] contents = InventoryUtil.getAllValidItems(inventory);
			if (contents == null) {
				return;
			}
			for (ItemStack toDrop : contents) {
				player.getWorld().dropItemNaturally(player.getLocation(), toDrop);
			}
			BackpackPlugin.getInstance().getStore().setBackpackFor(player, player.getWorld(), StorageUtil.put(player, player.getWorld()));
		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		//This should cover our dependency issues
		BackpackPlugin.getInstance().getHooks().setup();
	}

	private void onBackpackClose(InventoryView viewer, Player player) {
		Inventory backpack = viewer.getTopInventory();

		if (backpack.getHolder().equals(player) && backpack.getTitle().equals("Backpack")) {
			BackpackPlugin.getInstance().getStore().setBackpackFor(player, player.getWorld(), backpack);
		}
	}
}
