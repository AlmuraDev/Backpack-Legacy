package com.almuramc.backpack.bukkit.storage.mode;

import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.StorageMode;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SQLStorage implements Storage {
	public SQLStorage() {
	}

	@Override
	public StorageMode getMode() {
		return StorageMode.SQL;
	}

	@Override
	public void setup() {
	}

	@Override
	public Inventory getBackpackFor(Player player, World world) {
		return null;
	}

	@Override
	public void setBackpackFor(Player player, World world, Inventory inventory) {
	}
}
