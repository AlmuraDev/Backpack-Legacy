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
package com.almuramc.backpack.bukkit;

import com.almuramc.backpack.bukkit.command.BackpackCommands;
import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.type.SQLStorage;
import com.almuramc.backpack.bukkit.storage.type.YamlStorage;
import com.almuramc.backpack.bukkit.util.CachedConfiguration;
import com.almuramc.backpack.bukkit.util.Dependency;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static BackpackPlugin instance;
	private static Storage store;
	private static CachedConfiguration cached;
	private static Dependency hooks;
	private BackpackCommands executor;

	@Override
	public void onDisable() {
		cached = null;
		instance = null;
		store = null;
		hooks = null;
	}

	@Override
	public void onEnable() {
		//Assign configured storage
		if (cached.useSQL()) {
			store = new SQLStorage();
		} else {
			store = new YamlStorage(getDataFolder());
		}
		//Setup dependencies
		hooks = new Dependency();
		hooks.setup();
		//Register commands
		executor = new BackpackCommands();
		getCommand("backpack").setExecutor(executor);
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
	}

	@Override
	public void onLoad() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfiguration();
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final Storage getStore() {
		return store;
	}

	public final CachedConfiguration getCached() {
		return cached;
	}

	public final Dependency getHooks() {
		return hooks;
	}
}