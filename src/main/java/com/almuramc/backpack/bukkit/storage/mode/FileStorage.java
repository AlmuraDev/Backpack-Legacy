package com.almuramc.backpack.bukkit.storage.mode;

import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.StorageMode;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FileStorage extends Storage {
	public FileStorage(StorageMode mode) {
		super(mode);
	}

	@Override
	public void setup() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Inventory getBackpackFor(Player player, World world) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setBackpackFor(Player player, World world, Inventory inventory) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
