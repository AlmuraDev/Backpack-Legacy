package com.almuramc.backpack.bukkit;

import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.mode.SQLStorage;
import com.almuramc.backpack.bukkit.storage.mode.YamlStorage;
import com.almuramc.backpack.bukkit.util.CachedConfigurationUtil;
import com.almuramc.backpack.bukkit.util.DependencyUtil;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static BackpackPlugin instance;
	private static Storage store;
	private static CachedConfigurationUtil cached;
	private static DependencyUtil hooks;

	@Override
	public void onDisable() {
		if (cached != null) {
			cached.save();
			cached = null;
		}
		instance = null;
		store = null;
	}

	@Override
	public void onEnable() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfigurationUtil();
		//Assign configured storage
		if (cached.useSQL()) {
			store = new SQLStorage();
		} else {
			store = new YamlStorage(getDataFolder());
		}
		//Setup storage
		store.setup();
		//Setup dependencies
		hooks = new DependencyUtil();
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final Storage getStore() {
		return store;
	}

	public final CachedConfigurationUtil getCached() {
		return cached;
	}

	public final DependencyUtil getHooks() {
		return hooks;
	}
}