package com.almuramc.backpack.bukkit.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Simple class to handle inventory transactions.
 */
public class InventoryUtil {
	public static Inventory filterIllegalItemsFromInventory(ItemStack[] blacklist, Inventory inventory) {
		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < i; j++) {
				if (!contents[i].equals(blacklist[j])) {
					continue;
				}
				//If blacklisted item, null the entry
				contents[i] = null;
			}
		}
		return inventory;
	}
}
