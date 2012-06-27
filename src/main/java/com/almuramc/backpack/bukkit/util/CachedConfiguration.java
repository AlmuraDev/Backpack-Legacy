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
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Utility helper config class that uses a cached config instance to access the config
 * file. Helper methods are provided for config entries.
 */
public final class CachedConfiguration {
	private final Plugin plugin;
	private FileConfiguration cachedConfig;
	private FileConfiguration cachedBlacklist;

	public CachedConfiguration() {
		plugin = BackpackPlugin.getInstance();
		setup();
	}

	public String getBackpackHotkey() {
		return cachedConfig.getString("backpack.hotkey");
	}

	public String getWorkbenchHotkey() {
		return cachedConfig.getString("workbench.hotkey");
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
		return cachedConfig.getBoolean("general.use-spout");
	}

	public boolean useSQL() {
		return cachedConfig.getBoolean("general.use-sql");
	}

	public String getSQLHost() {
		return cachedConfig.getString("sql.host");
	}

	public int getDefaultSize() {
		int size = cachedConfig.getInt("backpack.no-perm-default-size");
		if (MathHelper.isValidBackpackSize(size)) {
			return size;
		}
		plugin.getLogger().warning("Invalid backpack size specified for no-perm-default-size. Defaulting to 9");
		return 9;
	}

	public HashSet<String> getBlacklistedItems() {
		List<String> elements = (List<String>) cachedBlacklist.getList("blacklist", Collections.emptyList());
		if (elements == null || (elements != null && elements.isEmpty())) {
			return new HashSet<String>();
		}
		elements.removeAll(Collections.singletonList(null));
		return new HashSet<String>(elements);
	}

	/**
	 * Reloads the cached config. Configs files are always guaranteed to be present.
	 */
	public void reload() {
		try {
			if (!setupConfig()) {
				plugin.reloadConfig();
				cachedConfig = plugin.getConfig();
			}
			if (!setupBlacklist()) {
				cachedBlacklist = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blacklist.yml"));
			}
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simple config setup method.
	 */
	private void setup() {
		try {
			setupConfig();
			setupBlacklist();
			cachedConfig = plugin.getConfig();
			cachedBlacklist = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blacklist.yml"));
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

	private boolean setupBlacklist() throws IOException, InvalidConfigurationException {
		if (!new File(plugin.getDataFolder(), "blacklist.yml").exists()) {
			plugin.saveResource("blacklist.yml", true);
			return true;
		}
		return false;
	}
}
