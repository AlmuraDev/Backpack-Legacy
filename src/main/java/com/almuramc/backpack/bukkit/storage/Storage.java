package com.almuramc.backpack.bukkit.storage;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class Storage {
	private StorageMode mode;

	public Storage(StorageMode mode) {
		this.mode = mode;
	}

	public abstract void setup();

	public abstract Inventory getBackpackFor(Player player, World world);

	public abstract void setBackpackFor(Player player, World world, Inventory inventory);

	public final StorageMode getMode() {
		return mode;
	}
}
