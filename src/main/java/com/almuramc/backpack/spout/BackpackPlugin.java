package com.almuramc.backpack.spout;

import com.almuramc.backpack.spout.config.BackpackConfiguration;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;

public class BackpackPlugin extends CommonPlugin {
	private static BackpackConfiguration config;
	private static BackpackPlugin instance;

	@Override
	public void onLoad() {
		config = new BackpackConfiguration(getDataFolder());
	}

	@Override
	public void onEnable() {
		instance = this;
		try {
			config.load();
		} catch (ConfigurationException e) {
			getLogger().warning("Could not load configuration!");
			e.printStackTrace();
		}
		getLogger().info("enabled.");
	}

	@Override
	public void onDisable() {
		instance = null;
		try {
			config.save();
		} catch (ConfigurationException e) {
			getLogger().warning("Could not save configuration!");
			e.printStackTrace();
		}
		getLogger().info("disabled.");
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}
}
