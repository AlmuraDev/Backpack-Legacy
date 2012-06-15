package com.almuramc.backpack.bukkit.util;

import java.io.File;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.keyboard.Keyboard;

/**
 * Utility helper config class that uses a cached config instance to access the config
 * file. Helper methods are provided for config entries.
 */
public final class CachedConfigurationUtil {
	private final Plugin plugin;
	private FileConfiguration cached;	

	public CachedConfigurationUtil() {
		plugin = BackpackPlugin.getInstance();
		setup();
	}

	public boolean isSQLEnabled() {
		return cached.getBoolean("general.sql");
	}

	public String bpHotkey() {
		return cached.getString("spoutgui.hotkey");
	}
	
	public boolean spoutguiEnabled() {
		return cached.getBoolean("spoutgui.enable");
	}
	
	public boolean useEconomy() {
		return cached.getBoolean("general.useEconomy");
	}
	/**
	 * Reloads the cached config. Configs files are always guaranteed to be present.
	 */
	public void reload() {
		try {
			if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
				plugin.saveDefaultConfig();
				cached = plugin.getConfig();
			} else {
				plugin.reloadConfig();
				cached = plugin.getConfig();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the cached configuration to disk, else it will save the builtin config.
	 */
	public void save() {
		try {
			if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
				plugin.saveDefaultConfig();
			} else {
				plugin.saveConfig();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simple config setup method.
	 */
	private void setup() {
		try {
			if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
				plugin.saveDefaultConfig();
			}

			cached = plugin.getConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
