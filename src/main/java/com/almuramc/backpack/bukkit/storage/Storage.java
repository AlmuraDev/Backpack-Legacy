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
package com.almuramc.backpack.bukkit.storage;

import java.util.HashMap;
import java.util.UUID;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.util.InventoryHelper;
import com.almuramc.backpack.bukkit.util.PermissionHelper;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class Storage {
	private static final HashMap<UUID, HashMap<String, Inventory>> INVENTORIES = new HashMap<UUID, HashMap<String, Inventory>>();

	public final void put(Player player, World world, Inventory inventory) {
		if (player == null || world == null) {
			return;
		}
		HashMap<String, Inventory> current = INVENTORIES.get(world.getUID());
		if (inventory == null && contains(player, world)) {
			INVENTORIES.get(world.getUID()).remove(player);
			return;
		}
		current.put(player.getName(), inventory);
		INVENTORIES.put(world.getUID(), current);
	}

	public final Inventory get(Player player, World world) {
		HashMap<String, Inventory> map = INVENTORIES.get(world.getUID());
		int maxSize = PermissionHelper.getSizeByPermFor(player);
		Inventory current = contains(player, world) ? map.get(player.getName()) : Bukkit.createInventory(player, BackpackPlugin.getInstance().getCached().getDefaultSize(), "Backpack");
		if (current.getSize() > maxSize) {
			current = InventoryHelper.resizeInventory(player, world, current, maxSize);
		}
		return current;
	}

	public final HashMap<UUID, Inventory> getAll(Player player) {
		if (player == null) {
			return null;
		}
		HashMap<UUID, Inventory> playerInventories = new HashMap<UUID, Inventory>();
		for (World world : Bukkit.getWorlds()) {
			playerInventories.put(world.getUID(), get(player, world));
		}

		return playerInventories;
	}

	public final boolean contains(Player player, World world) {
		if (player == null || world == null || !contains(world)) {
			return false;
		}
		HashMap<String, Inventory> worldMap = INVENTORIES.get(world.getUID());
		if (worldMap.containsKey(player.getName())) {
			return true;
		}
		return false;
	}

	public final boolean contains(World world) {
		if (world == null || (world != null && INVENTORIES.get(world.getUID()) == null)) {
			return false;
		}
		return true;
	}

	public void initialize() {
		for (World world : Bukkit.getWorlds()) {
			if (!contains(world)) {
				INVENTORIES.put(world.getUID(), new HashMap<String, Inventory>());
			}
		}
	}

	public abstract Inventory load(Player player, World world);

	public abstract void save(Player player, World world, Inventory inventory);
}
