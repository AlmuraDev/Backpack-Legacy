/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuramc.backpack.bukkit.listener;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.inventory.BackpackInventory;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.util.CachedConfiguration;

import net.milkbowl.vault.permission.Permission;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class BackpackListener implements Listener {
	private static final BackpackPlugin plugin = BackpackPlugin.getInstance();
	private static final Storage STORE = BackpackPlugin.getInstance().getStore();
	private static final CachedConfiguration CONFIG = BackpackPlugin.getInstance().getCached();
	private static final Permission PERM = BackpackPlugin.getInstance().getHooks().getPermHook();

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			World world = player.getWorld();
			if (PERM.has(world.getName(), player.getName(), "backpack.keepitems")) {
				return;
			}
			BackpackInventory backpack = STORE.load(player, world);
			ItemStack[] contents = backpack.getVisibleContents();
			if (contents == null) {
				return;
			}
			for (ItemStack toDrop : contents) {
				world.dropItemNaturally(player.getLocation(), toDrop);
			}
			backpack.clear();
			STORE.save(player, world, backpack);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		Player who = (Player) event.getWhoClicked();
		if (!event.getView().getTopInventory().getTitle().equals("Backpack") || PERM.has(who.getWorld().getName(), who.getName(), "backpack.noblacklist") || !event.getSlotType().equals(InventoryType.SlotType.CONTAINER)) {
			return;
		}
		System.out.println(event.getSlotType().toString());
		Material material = event.getCursor().getType();
		String mat = material.name();
		for (String name : CONFIG.getBlacklistedItems()) {
			if (name.equalsIgnoreCase(mat)) {
				if (CONFIG.useSpout()) {
					((SpoutPlayer) who).sendNotification("Backpack", "Item is blacklisted", material);
				} else {
					who.sendMessage("[Backpack] " + mat + " is blacklisted!");
				}
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		onBackpackClose(event.getView(), event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent event) {
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getPlayer();
		if (!PERM.has(player.getWorld().getName(), player.getName(), "backpack.use")) {
			event.setCancelled(true);
			return;
		}
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
	public void onPlayerQuit(PlayerQuitEvent event) {
		STORE.save(event.getPlayer(), event.getPlayer().getWorld(), null);
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		plugin.getStore().initWorld(event.getWorld());
	}

	private void onBackpackClose(InventoryView viewer, HumanEntity entity) {
		Player player = (Player) entity;
		Inventory backpack = viewer.getTopInventory();

		if (backpack.getHolder().equals(player) && backpack.getTitle().equals("Backpack")) {
			STORE.save(player, player.getWorld(), new BackpackInventory(backpack));
		}
	}
}
