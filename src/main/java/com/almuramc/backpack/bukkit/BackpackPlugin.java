/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the GNU Public License version 3.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Backpack.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.almuramc.backpack.bukkit;

import com.almuramc.backpack.bukkit.command.BackpackCommands;
import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.mode.SQLStorage;
import com.almuramc.backpack.bukkit.storage.mode.YamlStorage;
import com.almuramc.backpack.bukkit.util.CachedConfigurationUtil;
import com.almuramc.backpack.bukkit.util.DependencyUtil;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static BackpackPlugin instance;
	private static Storage store;
	private static CachedConfigurationUtil cached;
	private static DependencyUtil hooks;
	private BackpackCommands executor;

	@Override
	public void onDisable() {
		if (cached != null) {
			cached.save();
			cached = null;
		}
		store.onUnload();
		instance = null;
		store = null;
	}

	@Override
	public void onEnable() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfigurationUtil();
		//Assign configured storage
		if (cached.useSQL()) {
			store = new SQLStorage();
		} else {
			store = new YamlStorage(getDataFolder());
		}
		//Setup storage
		store.onLoad();
		//Setup dependencies
		hooks = new DependencyUtil();
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
		//Register commands
		executor = new BackpackCommands(this);
		getCommand("backpack").setExecutor(executor);
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final Storage getStore() {
		return store;
	}

	public final CachedConfigurationUtil getCached() {
		return cached;
	}

	public final DependencyUtil getHooks() {
		return hooks;
	}
}