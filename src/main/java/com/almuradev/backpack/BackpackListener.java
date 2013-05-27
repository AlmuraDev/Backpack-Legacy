/*
 * This file is part of Backpack.
 *
 * © 2012-2013 AlmuraDev <http://www.almuradev.com/>
 * Backpack is licensed under the GNU General Public License.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in GNU General Public License.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuradev.backpack;

import com.almuradev.backpack.inventory.Backpack;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class BackpackListener implements Listener {
	private final BackpackPlugin plugin;

	public BackpackListener(final BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		checkAndCreateInitialBackpackIfValid(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		checkAndCreateInitialBackpackIfValid(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		//Handle only player clicks
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		final Inventory inventory = event.getInventory();

		//Handle only Backpacks
		if (!inventory.getTitle().contains("Backpack")) {
			return;
		}

		final Player holder = (Player) inventory.getHolder();
		final Backpack backpack = plugin.getStorage().get(holder.getWorld().getName(), holder);

		//Hypothetical situation A says that if the admin removes their Backpack while they still have the window open, lets immediately get rid of it.
		if (backpack == null) {
			event.getCurrentItem().setType(Material.AIR); //TODO Test this
			holder.closeInventory();
		} else {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
				return;
			}
			//TODO This might be too severe, come back and optimize later
			backpack.setDirty(true);
		}
	}

	/**
	 * This is for creating the inner {@link org.bukkit.inventory.Inventory} during a login or during a {@link World} change.
	 * @param player The player who caused the event
	 */
	private void checkAndCreateInitialBackpackIfValid(final Player player) {
		final World world = player.getWorld();
		final Backpack backpack = plugin.getStorage().get(world.getName(), player.getName());
		if (backpack != null && !backpack.isCreated()) {
			backpack.create();
		}
	}
}
