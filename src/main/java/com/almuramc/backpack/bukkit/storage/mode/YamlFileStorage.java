package com.almuramc.backpack.bukkit.storage.mode;

import java.io.File;

import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.StorageMode;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class YamlFileStorage implements Storage {
	private final File parentDir;
	private final YamlConfiguration reader = new YamlConfiguration();

	public YamlFileStorage(File parentDir) {
		this.parentDir = parentDir;
	}

	@Override
	public StorageMode getMode() {
		return StorageMode.FILE;
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
