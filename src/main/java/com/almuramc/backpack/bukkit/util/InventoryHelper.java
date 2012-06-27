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

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Simple class to handle inventory transactions.
 */
public class InventoryHelper {
	public static final Inventory resizeInventory(Player player, World world, Inventory inventory, int size) {
		if (player == null || world == null || inventory == null) {
			return null;
		}
		if (inventory.getSize() == size) {
			return inventory;
		}
		ArrayList<ItemStack> resized = new ArrayList<ItemStack>();
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < size; i++) {
			if (i > items.length - 1) {
				break;
			} else {
				resized.add(items[i]);
			}
		}
		Inventory toReplace = Bukkit.createInventory(player, size, "Backpack");
		toReplace.setContents(resized.toArray(new ItemStack[resized.size()]));
		return toReplace;
	}

	public static final boolean hasActualContents(Inventory inventory) {
		if (inventory == null) {
			return false;
		}
		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null || (contents[i] != null && contents[i].equals(Material.AIR))) {
				continue;
			}
			return true;
		}
		return false;
	}

	public static final ItemStack[] getAllValidItems(Inventory inventory) {
		if (inventory == null || !InventoryHelper.hasActualContents(inventory)) {
			return null;
		}
		ItemStack[] contents = inventory.getContents();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < contents.length; i++) {
			ItemStack current = contents[i];
			if (current == null || (current != null && current.equals(Material.AIR))) {
				continue;
			}
			items.add(current);
		}
		ItemStack[] result = items.toArray(new ItemStack[items.size()]);
		if (result.length == 0) {
			return null;
		}
		return result;
	}
}
