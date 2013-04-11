package com.almuradev.backpack;

import com.almuradev.backpack.backend.Backpacks;
import com.almuradev.backpack.backend.Storage;

import org.bukkit.plugin.java.JavaPlugin;

public final class BackpackPlugin extends JavaPlugin {
	private Backpacks backend;
	private Storage storage;

	@Override
	public void onEnable() {
		storage = new Storage(this);
		backend = new Backpacks(storage);
		backend.onEnable();
	}

	public Backpacks getBackend() {
		return backend;
	}

	public Storage getStorage() {
		return storage;
	}
}
