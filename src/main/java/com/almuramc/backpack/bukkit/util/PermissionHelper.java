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
package com.almuramc.backpack.bukkit.util;

import java.util.HashMap;
import java.util.List;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Simple class to handle checking of permissions.
 */
public class PermissionHelper {
	private static final String[] BACKPACK_SIZE_PERMS = {
			"backpack.size.9",
			"backpack.size.18",
			"backpack.size.27",
			"backpack.size.36",
			"backpack.size.45",
			"backpack.size.54"
	};

	/**
	 * Checks the permissions of a player and compares it to an array of size perms. This method
	 * will return the size of a backpack.
	 * @param player
	 * @return
	 */
	public static int getMaxSizeFor(Player player) {
		String found;
		int size = -1;
		for (String perm : BACKPACK_SIZE_PERMS) {
			if (BackpackPlugin.getInstance().getHooks().getPermHook().has(player.getWorld().getName(), player.getName(), perm)) {
				found = perm;
				int temp = Integer.parseInt(found.split("backpack.size.")[1]);
				//Only set biggest size
				if (temp > size) {
					size = temp;
				}
			}
		}
		if (size == -1) {
			return BackpackPlugin.getInstance().getCached().getMaximumSize();
		}
		return size;
	}

	public static World getWorldToOpen(Player player, World world) {
		HashMap<String, List<String>> shares = BackpackPlugin.getInstance().getCached().getShareEntries();
		/**
		 * Shares has no entries
		 * Player doesn't have share permission
		 * Shares contains the world being shared to as a parent(key)
		 */
		if (shares == null || !BackpackPlugin.getInstance().getHooks().getPermHook().has(world, player.getName(), "backpack.share") || shares.containsKey(world.getName().toLowerCase())) {
			return world;
		}

		/**
		 * None of the above conditions checked out, lets see if it is a child
		 */
		World w = null;
		for (String key : shares.keySet()) {
			List<String> temp = shares.get(key);
			if (temp == null) {
				continue;
			}
			//If the children list of worlds has either a wildcard or the world passed in return the parent
			if (temp.contains("*") || temp.contains(world.getName().toLowerCase())) {
				w = Bukkit.getWorld(key.toLowerCase());
				break;
			}
		}
		return w == null ? world : w;
	}
}
