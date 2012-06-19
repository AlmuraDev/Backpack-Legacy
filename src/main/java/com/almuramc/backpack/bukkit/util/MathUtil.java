package com.almuramc.backpack.bukkit.util;

/**
 * Simple class to add math utility methods
 */
public class MathUtil {

	public static boolean isValidBackpackSize(int size) {
		if (size > 54 || size < 9 || size % 9 != 0) {
			return false;
		}

		return true;
	}
}
