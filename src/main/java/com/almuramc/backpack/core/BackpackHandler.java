package com.almuramc.backpack.core;

import java.io.File;

import com.almuramc.backpack.Backpack;

import org.apache.commons.collections.map.MultiKeyMap;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Serves as a handler for backpacks.
 */
public class BackpackHandler {
	//In-Memory store of paired player and world keys with inventories that are backpacks.
	private final MultiKeyMap inventories;

	public BackpackHandler() {
		inventories = new MultiKeyMap();
		setupBackpackDirectories();
	}

	/**]
	 * Sets a backpack for a player in a world.
	 * @param player Player that will have backpack set.
	 * @param world  World the player is in.
	 * @param inventory the backpack's inventory.
	 */
	public void setBackpackFor(Player player, World world, Inventory inventory) {
		inventories.put(player, world, inventory);
	}

	/**
	 * Gets a backpack for a player in a world.
	 * @param player Player that is accessing their backpack.
	 * @param world World the player is in.
	 * @return
	 */
	public Inventory getBackpackFor(Player player, World world) {
		return (Inventory) inventories.get(player, world);
	}

	/**
	 * Loads all the backpacks from disk.
	 */
	public void loadBackpacks() {
		//TODO load all from disk
	}

	/**
	 * Loads a backpack for a particular player.
	 * @param player
	 */
	public void loadBackpackFor(Player player) {
		//TODO load from disk
	}

	/**
	 * Saves all the backpacks to the disk.
	 */
	public void saveBackpacks() {
		//TODO save all to disk
	}

	/**
	 * Saves a backpack for a particular player.
	 * @param player
	 */
	public void saveBackpackFor(Player player) {
		//TODO save to disk
	}

	private void setupBackpackDirectories() {

	}
}
