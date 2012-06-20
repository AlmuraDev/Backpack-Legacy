/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which reference GPL licensed code are hereby
 * licensed under the GNU General Public License, as described in the
 * Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the GNU General Public License (for classes that fulfill the exception) and the
 * Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU Lesser General Public License and
 * the GNU General Public License.
 */
package com.almuramc.backpack.bukkit.util;

import java.io.File;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

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

	public String getBackpackHotkey() {
		return cached.getString("backpack.hotkey");
	}

	public String getWorkbenchHotkey() {
		return cached.getString("workbench.hotkey");
	}

	public String getPanelHotkey() {
		return cached.getString("admin.hotkey");
	}

	public boolean useControlPanel() {
		return cached.getBoolean("admin.control-panel");
	}

	public boolean useEconomy() {
		return cached.getBoolean("general.use-economy");
	}

	public boolean useSpout() {
		return cached.getBoolean("general.use-spout");
	}

	public boolean useSQL() {
		return cached.getBoolean("general.use-sql");
	}

	public String getSQLHost() {
		return cached.getString("sql.host");
	}

	public int getDefaultSize() {
		int size = cached.getInt("backpack.no-perm-default-size");
		if (MathUtil.isValidBackpackSize(size)) {
			return size;
		}
		BackpackPlugin.getInstance().getLogger().warning("Invalid backpack size specified for no-perm-default-size. Defaulting to 9");
		return 9;
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
