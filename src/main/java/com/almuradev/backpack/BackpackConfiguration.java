/*
 * This file is part of Backpack.
 *
 * © 2012-2013 AlmuraDev <http://www.almuradev.com/>
 * Backpack is licensed under the GNU General Public License.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in GNU General Public License.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuradev.backpack;

import java.util.HashMap;
import java.util.Map;

import com.almuradev.backpack.util.Size;

import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.configuration.file.FileConfiguration;

public class BackpackConfiguration {
	private final BackpackPlugin plugin;
	private final HashMap<Size, Double> SIZE_COSTS = new HashMap<>(6);
	private FileConfiguration config;

	public BackpackConfiguration(final BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	public void onEnable() {
		plugin.saveDefaultConfig();
		config = plugin.getConfig();

		//Parse costs
		final Map<String, Object> rawValues = config.getConfigurationSection("backpack.costs").getValues(false);
		for (Map.Entry<String, Object> entry : rawValues.entrySet()) {
			Size size = null;
			try {
				size = Size.valueOf(entry.getKey().toUpperCase());
			} catch (Exception e) {
				plugin.getLogger().warning(entry.getKey() + " is not a valid size. Skipping...");
			}

			if (size != null) {
				Object obj = entry.getValue();
				if (obj instanceof Integer) {
					SIZE_COSTS.put(size, (double) ((Integer) entry.getValue()).intValue());
				} else {
					SIZE_COSTS.put(size, (Double) entry.getValue());
				}
			}
		}
	}

	public boolean isEconomyEnabled() {
		return config.getBoolean("general.use-economy", false);
	}

	public boolean isSpoutEnabled() {
		return config.getBoolean("general.use-spout", false);
	}

	public int getSaveInterval() {
		return config.getInt("general.save-interval", 100);
	}

	public Keyboard getHotkey() {
		final String raw = config.getString("backpack.hotkey", "KEY_B");
		try {
			return Keyboard.valueOf(raw);
		} catch (Exception e) {
			plugin.getLogger().warning(raw + " is not a valid hotkey. Defaulting to b (KEY_B)");
			return Keyboard.KEY_B;
		}
	}

	public double getCostFor(final Size size) {
		final Double cost = SIZE_COSTS.get(size);
		return cost == null ? 0.0 : cost;
	}
}
