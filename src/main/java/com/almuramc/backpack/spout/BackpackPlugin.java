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
