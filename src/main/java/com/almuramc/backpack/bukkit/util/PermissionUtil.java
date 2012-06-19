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
package com.almuramc.backpack.bukkit.util;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * Simple class to handle checking of permissions.
 */
public class PermissionUtil {
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
	 * will return the size of a backpack or -1 if no size perm was found.
	 * @param player
	 * @return
	 */
	public static int getSizeByPermFor(Player player) {
		String found;
		int size = -1;
		for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
			for (String sizePerm : BACKPACK_SIZE_PERMS) {
				//Find a valid size perm
				if (perm.getPermission().equals(sizePerm)) {
					//Store the found permission.
					found = perm.getPermission();
					//Splice out the size
					int temp = Integer.parseInt(found.split("backpack.size.")[1]);
					//Only set biggest size
					if (temp > size) {
						size = temp;
					}
				}
			}
		}
		if (size == -1) {
			return BackpackPlugin.getInstance().getCached().getDefaultSize();
		}
		return size;
	}
}
