package com.almuramc.backpack.bukkit.util;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.input.BackpackInputHandler;
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
