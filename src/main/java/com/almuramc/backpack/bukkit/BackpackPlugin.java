package com.almuramc.backpack.bukkit;

import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.util.CachedConfigurationUtil;
import com.almuramc.backpack.bukkit.util.DependencyUtil;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static BackpackPlugin instance;
	private static BackpackHandler handler;
	private static CachedConfigurationUtil cached;
	private static DependencyUtil hooks;

	@Override
	public void onDisable() {
		if (cached != null) {
			cached.save();
			cached = null;
		}
		instance = null;
		handler = null;
	}

	@Override
	public void onEnable() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfigurationUtil();
		//Setup storage
		handler = new BackpackHandler();
		//Setup dependencies
		hooks = new DependencyUtil();
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final BackpackHandler getCore() {
		return handler;
	}

	public final CachedConfigurationUtil getCached() {
		return cached;
	}

	public final DependencyUtil getHooks() {
		return hooks;
	}
}