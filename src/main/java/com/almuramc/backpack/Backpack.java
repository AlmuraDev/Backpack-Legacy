package com.almuramc.backpack;

import java.util.logging.Logger;

import com.almuramc.backpack.core.BackpackHandler;
import com.almuramc.backpack.input.BackpackInputHandler;
import com.almuramc.backpack.listener.BackpackListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;

public class Backpack extends JavaPlugin {
	private static Backpack instance;
	private final BackpackHandler backpackHandler;
	private Logger log = Logger.getLogger("minecraft");

	public Backpack() {
		backpackHandler = new BackpackHandler();
	}

	@Override
	public void onDisable() {
		backpackHandler.saveBackpacks();
		log.info("disabled.");
	}

	@Override
	public void onEnable() {
		instance = this;
		backpackHandler.loadBackpacks();
		SpoutManager.getKeyBindingManager().registerBinding("Backpack", Keyboard.KEY_B, "Opens the backpack", new BackpackInputHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
		log.info("enabled.");
	}

	public static Backpack getInstance() {
		return instance;
	}

	public BackpackHandler getBackpackHandler() {
		return backpackHandler;
	}
}