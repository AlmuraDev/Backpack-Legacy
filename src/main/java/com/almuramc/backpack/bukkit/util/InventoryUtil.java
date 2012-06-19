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

import java.util.ArrayList;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Simple class to handle inventory transactions.
 */
public class InventoryUtil {
	public static Inventory filterIllegalItemsFromInventory(ArrayList<ItemStack> blacklist, Inventory inventory) {
		ItemStack[] contents = inventory.getContents();
		Inventory inv = inventory;
		//Sanity checks
		if (inventory == null) {
			return null;
		}
		if (blacklist == null) {
			return inventory;
		}
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < i; j++) {
				if (!contents[i].equals(blacklist.get(j))) {
					continue;
				}
				//If blacklisted item, null the entry
				contents[i] = null;
			}
		}
		inv.setContents(contents);
		return inv;
	}
}
