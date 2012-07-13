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

import com.almuramc.backpack.bukkit.inventory.BackpackInventory;

import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class Storage {
	private static final HashMap<UUID, HashMap<UUID, BackpackInventory>> BACKPACKS = new HashMap<UUID, HashMap<UUID, BackpackInventory>>();

	public final void store(Player player, World world, BackpackInventory toStore) {
		if (player == null || world == null) {
			return;
		}
		HashMap<UUID, BackpackInventory> playerMap = BACKPACKS.get(world.getUID());
		if (playerMap == null) {
			playerMap = new HashMap<UUID, BackpackInventory>();
		}
		if (playerMap.containsKey(player.getUniqueId()) && (toStore == null || toStore.getInventory() == null)) {
			playerMap.remove(player.getUniqueId());
			BACKPACKS.put(world.getUID(), playerMap);
			return;
		}
		playerMap.put(player.getUniqueId(), toStore);
		BACKPACKS.put(world.getUID(), playerMap);
	}

	public final BackpackInventory fetch(Player player, World world) {
		if (player == null || world == null) {
			return null;
		}
		HashMap<UUID, BackpackInventory> playerMap = BACKPACKS.get(world.getUID());
		if (playerMap == null) {
			return null;
		}
		BackpackInventory backpack = playerMap.get(player.getUniqueId());
		if (backpack == null) {
			return null;
		}
		return backpack;
	}

	public final boolean has(World world) {
		return BACKPACKS.get(world.getUID()) != null;
	}

	public final boolean has(Player player, World world) {
		HashMap<UUID, BackpackInventory> map = BACKPACKS.get(world.getUID());
		return map != null && map.get(player.getUniqueId()) != null;
	}

	public final HashMap<UUID, BackpackInventory> fetchAll(Player player) {
		HashMap<UUID, BackpackInventory> fetched = new HashMap<UUID, BackpackInventory>();
		for (UUID entry : BACKPACKS.keySet()) {
			HashMap<UUID, BackpackInventory> map = BACKPACKS.get(entry);
			if (map.containsKey(player.getUniqueId())) {
				fetched.put(entry, map.get(player.getUniqueId()));
			}
		}
		return fetched;
	}

	public abstract void initWorld(World world);

	public abstract BackpackInventory load(Player player, World world);

	public abstract void save(Player player, World world, BackpackInventory backpack);
}
