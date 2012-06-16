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
