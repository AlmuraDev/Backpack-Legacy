package com.almuramc.backpack.bukkit.storage;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Storage {
	public StorageMode getMode();

	public void setup();

	public Inventory getBackpackFor(Player player, World world);

	public void setBackpackFor(Player player, World world, Inventory inventory);
}
