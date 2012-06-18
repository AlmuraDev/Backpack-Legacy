package com.almuramc.backpack.bukkit.storage;

import org.apache.commons.collections.map.MultiKeyMap;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class Storage {
	protected static final MultiKeyMap INVENTORIES = new MultiKeyMap();

	public abstract StorageMode getMode();

	public abstract void setup();

	public abstract Inventory getBackpackFor(Player player, World world);

	public abstract void setBackpackFor(Player player, World world, Inventory inventory);
}
