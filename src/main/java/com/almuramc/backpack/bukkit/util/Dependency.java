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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Dependency {
	private final Plugin plugin;
	private final PluginManager manager;
	private Permission permHook;
	private Economy econHook;

	public Dependency(Plugin plugin) {
		this.plugin = plugin;
		manager = plugin.getServer().getPluginManager();
	}

	public boolean isSpoutPluginEnabled() {
		return manager.isPluginEnabled("Spout");
	}

	public Permission getPermissions() {
		return permHook;
	}

	public Economy getEconomy() {
		return econHook;
	}

	public void setupVaultEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			econHook = economyProvider.getProvider();
			plugin.getLogger().info("Successfully hooked into Vault for economy transactions");
		}
	}

	public void setupVaultPermissions() {
		RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp != null) {
			permHook = rsp.getProvider();
			plugin.getLogger().info("Successfully hooked into Vault for permissions");
		}
	}
}
