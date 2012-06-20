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

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.input.BackpackInputHandler;
import com.almuramc.backpack.bukkit.input.PanelInputHandler;
import com.almuramc.backpack.bukkit.input.WorkbenchInputHandler;
import com.almuramc.backpack.bukkit.util.exception.InvalidDependencyException;

import net.milkbowl.vault.economy.Economy;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Simple class to handle dependencies
 */
public class DependencyUtil {
	private static CachedConfigurationUtil cached;
	private static boolean spoutWasSetup = false;
	private static boolean vaultWasSetup = false;
	private Economy econ;
	private PluginManager pm;

	public DependencyUtil() {
		cached = BackpackPlugin.getInstance().getCached();
		pm = BackpackPlugin.getInstance().getServer().getPluginManager();
	}

	public boolean isSpoutEnabled() {
		return pm.isPluginEnabled("Spout");
	}

	public boolean isVaultEnabled() {
		return pm.isPluginEnabled("Vault");
	}

	public Economy getEconHook() throws InvalidDependencyException {
		if (econ == null) {
			throw new InvalidDependencyException("Attempted to access Vault economy hook but Vault was null!");
		}
		return econ;
	}

	/**
	 * Spout adds keybinding support so we set that up here
	 */
	public void setupSpout() {
		//SpoutPlugin has an instance in this plugin so return as that means this has been ran before.
		if (spoutWasSetup) {
			return;
		}
		if (cached.useSpout() && isSpoutEnabled()) {
			spoutWasSetup = true;
			SpoutManager.getKeyBindingManager().registerBinding("Backpack", Keyboard.valueOf(cached.getBackpackHotkey()), "Opens the backpack", new BackpackInputHandler(), BackpackPlugin.getInstance());
			SpoutManager.getKeyBindingManager().registerBinding("Portable Workbench", Keyboard.valueOf(cached.getWorkbenchHotkey()), "Opens portable workbench", new WorkbenchInputHandler(), BackpackPlugin.getInstance());
			SpoutManager.getKeyBindingManager().registerBinding("Backpack Panel", Keyboard.valueOf(cached.getPanelHotkey()), "Opens Backpack Panel", new PanelInputHandler(), BackpackPlugin.getInstance());
			BackpackPlugin.getInstance().getLogger().info("Sucessfully hooked into SpoutPlugin for keybindings");
		}
	}

	public void setupVault() {
		if (vaultWasSetup) {
			return;
		}
		if (cached.useEconomy() && isVaultEnabled()) {
			RegisteredServiceProvider<Economy> economyProvider = BackpackPlugin.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				vaultWasSetup = true;
				econ = economyProvider.getProvider();
				BackpackPlugin.getInstance().getLogger().info("Sucessfully hooked into Vault for economy transactions");
			}
		}
	}

	/**
	 * General dependency setup method
	 */
	public void setup() {
		setupSpout();
		setupVault();
	}
}
