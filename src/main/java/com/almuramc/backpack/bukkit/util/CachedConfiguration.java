/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuramc.backpack.bukkit.util;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class CachedConfiguration {
	private final Plugin plugin;
	private FileConfiguration cachedConfig;
	private FileConfiguration cachedBlacklist;
	private FileConfiguration cachedShare;
	private HashMap<String, List<String>> entries = null;
	private HashMap<String, Double> costMap = null;
	private HashSet<String> elements = null;

	public CachedConfiguration() {
		plugin = BackpackPlugin.getInstance();
		setup();
	}

	public String getBackpackHotkey() {
		return cachedConfig.getString("backpack.hotkey");
	}

	public String getPanelHotkey() {
		return cachedConfig.getString("admin.hotkey");
	}

	public boolean useControlPanel() {
		return cachedConfig.getBoolean("admin.control-panel");
	}

	public boolean useEconomy() {
		return cachedConfig.getBoolean("general.use-economy");
	}

	public boolean useSpout() {
		return cachedConfig.getBoolean("general.use-spoutplugin");
	}

	public int getDefaultSize() {
		return cachedConfig.getInt("backpack.default-size");
	}

	public int getMaximumSize() {
		return cachedConfig.getInt("backpack.maximum-no-perm-size");
	}

	public HashSet<String> getBlacklistedItems() {
		if (elements != null) {
			return elements;
		}
		List<String> temp = (List<String>) cachedBlacklist.getList("blacklist", Collections.emptyList());
		if (temp == null || temp.isEmpty()) {
			return new HashSet<String>();
		}
		temp.removeAll(Collections.singletonList(null));
		for (int i = 0; i < temp.size(); i ++) {
			String t = temp.get(i);
			temp.set(i, t.toUpperCase());
		}
		elements = new HashSet<String>(temp);
		return elements;
	}

	public HashMap<String, Double> getUpgradeCosts() {
		if (costMap != null) {
			return costMap;
		}
		costMap = new HashMap<String, Double>();
		ConfigurationSection parent = cachedConfig.getConfigurationSection("backpack.cost");
		Set<String> keys = parent.getKeys(false);
		for (String key : keys) {
			costMap.put(key, parent.getDouble(key, 0));
		}
		if (!costMap.isEmpty()) {
			return costMap;
		}
		return null;
	}

	public HashMap<String, List<String>> getShareEntries() {
		if (entries != null) {
			return entries;
		}
		entries = new HashMap<String, List<String>>();
		ConfigurationSection parent = cachedShare.getConfigurationSection("share");
		for (String worldName : parent.getKeys(false)) {
			List<String> children = parent.getStringList(parent.getCurrentPath() + "." + worldName);
			if (children != null || !children.isEmpty()) {
				entries.put(worldName, children);
			}
		}
		if (!entries.isEmpty()) {
			return entries;
		}

		return null;
	}

	public void reload() {
		try {
			if (!setupConfig()) {
				plugin.reloadConfig();
				cachedConfig = plugin.getConfig();
			}
			if (!setupBlacklist()) {
				cachedBlacklist = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blacklist.yml"));
			}
			if (!setupShare()) {
				cachedShare = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "share.yml"));
			}
			//Set the storage to null so they can be re-generated when called for after the reload.
			entries = null;
			costMap = null;
			elements = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setup() {
		try {
			setupConfig();
			setupBlacklist();
			setupShare();
			cachedConfig = plugin.getConfig();
			cachedBlacklist = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blacklist.yml"));
			cachedShare = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "share.yml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean setupConfig() {
		if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
			plugin.saveDefaultConfig();
			return true;
		}
		return false;
	}

	private boolean setupBlacklist() {
		if (!new File(plugin.getDataFolder(), "blacklist.yml").exists()) {
			plugin.saveResource("blacklist.yml", true);
			return true;
		}
		return false;
	}

	private boolean setupShare() {
		if (!new File(plugin.getDataFolder(), "share.yml").exists()) {
			plugin.saveResource("share.yml", true);
			return true;
		}
		return false;
	}
}
