package com.almuramc.backpack.bukkit.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created with IntelliJ IDEA.
 * User: Chris
 * Date: 9/14/12
 * Time: 12:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryUtil {
	public static boolean hasOnlyOneFreeSlot(Inventory inv) {
		if (inv == null) {
			throw new IllegalStateException("Inventory passed in should not be null!");
		}
		ItemStack[] contents = inv.getContents();
		int count = 0;
		for (ItemStack stack : contents) {
			if (stack == null || stack.getType().equals(Material.AIR)) {
				continue;
			}
			count++;
		}
		if (count == contents.length - 1) {
			return true;
		}
		return false;
	}
}
