package com.almuramc.backpack.bukkit;

import java.util.logging.Logger;

import com.almuramc.backpack.bukkit.input.BackpackInputHandler;
import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.util.CachedConfigurationUtil;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static final Logger log = Logger.getLogger("minecraft");
	private static BackpackPlugin instance;
	private static BackpackHandler handler;
	private static CachedConfigurationUtil cached;

	@Override
	public void onDisable() {
		if (cached != null) {
			cached.save();
			cached = null;
		}
		instance = null;
		handler = null;
		log.info("disabled.");
	}

	@Override
	public void onEnable() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfigurationUtil();
		//Setup handler
		handler = new BackpackHandler();
		//Set keybinding
		SpoutManager.getKeyBindingManager().registerBinding("Backpack", Keyboard.KEY_B, "Opens the backpack", new BackpackInputHandler(), this);
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
		log.info("enabled.");
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final BackpackHandler getHandler() {
		return handler;
	}

	public final CachedConfigurationUtil getCached() {
		return cached;
	}
}