package com.almuramc.backpack.bukkit.util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class StorageUtil {
	private static final HashMap<UUID, HashMap<String, Inventory>> INVENTORIES = new HashMap<UUID, HashMap<String, Inventory>>();

	/**
	 * Accessor for the inventory maps to put a player's inventory for a specific world
	 * @param player the player to set an inventory for.
	 * @param world the world to set an inventory for.
	 * @param inventory the inventory to set.
	 * @return the resulting inventory.
	 */
	public static final Inventory put(Player player, World world, Inventory inventory) {
		if (player == null || world == null || inventory == null) {
			return null;
		}
		HashMap<String, Inventory> current = INVENTORIES.get(world.getUID());
		//Player has no current entry
		if (current == null) {
			current = new HashMap<String, Inventory>();
			current.put(player.getName(), inventory);
			//Player has an entry
		} else {
			current.put(player.getName(), inventory);
		}
		INVENTORIES.put(world.getUID(), current);
		return current.get(player.getName());
	}

	/**
	 * Accessor for the inventory maps that puts a default permission determined inventory associated with
	 * a player's name for a specific world.
	 * @param player the player to set an inventory for.
	 * @param world the world to set an inventory for.
	 * @return the resulting inventory.
	 */
	public static final Inventory put(Player player, World world) {
		return put(player, world, Bukkit.createInventory(player, PermissionUtil.getSizeByPermFor(player), "Backpack"));
	}

	/**
	 * Accessor for the inventory maps that gets the inventory of a player for a world.
	 * @param player the player to get an inventory for.
	 * @param world the world to get an inventory for
	 * @return the resulting inventory.
	 */
	public static final Inventory get(Player player, World world) {
		if (player == null || world == null) {
			return null;
		}
		HashMap<String, Inventory> map = INVENTORIES.get(world.getUID());
		//If they have no inventory in the map, make one
		if (map == null) {
			map = new HashMap<String, Inventory>();
		}
		Inventory current = map.get(player.getName());
		if (current == null) {
			current = put(player, world);
			//Check for and adjust inventory size based on permissions
		} else {
			current = InventoryUtil.resizeInventory(player, world, current, PermissionUtil.getSizeByPermFor(player));
		}
		return current;
	}

	/**
	 * Accessor for the inventory maps that gets all inventories for all current worlds for a
	 * specific player.
	 * @param player the player to get all inventories for.
	 * @return the map containing a listing of inventories mapped to world UUIDs.
	 */
	public static final HashMap<UUID, Inventory> getAll(Player player) {
		if (player == null) {
			return null;
		}
		HashMap<UUID, Inventory> playerInventories = new HashMap<UUID, Inventory>();
		for (World world : Bukkit.getWorlds()) {
			playerInventories.put(world.getUID(), get(player, world));
		}

		return playerInventories;
	}
}
