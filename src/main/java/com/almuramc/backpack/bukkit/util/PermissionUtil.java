package com.almuramc.backpack.bukkit.util;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.configuration.InvalidConfigurationException;
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
			try {
				return BackpackPlugin.getInstance().getCached().getDefaultSize();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
}
