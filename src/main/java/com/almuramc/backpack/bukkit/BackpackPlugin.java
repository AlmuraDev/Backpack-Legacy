package com.almuramc.backpack.bukkit;

import java.util.logging.Logger;

import com.almuramc.backpack.bukkit.input.BackpackInputHandler;
import com.almuramc.backpack.bukkit.listener.BackpackListener;
import com.almuramc.backpack.bukkit.util.CachedConfigurationUtil;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.KeyBindingManager;
import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {
	private static final Logger log = Logger.getLogger("minecraft");
	private static BackpackPlugin instance;
	private static BackpackHandler handler;
	private static CachedConfigurationUtil cached;
	private Economy econ = null;

	@Override
	public void onDisable() {
		if (cached != null) {
			cached.save();
			cached = null;
		}
		instance = null;
		handler = null;
	}

	@Override
	public void onEnable() {
		//Assign plugin instance
		instance = this;
		//Setup config
		cached = new CachedConfigurationUtil();
		//Setup handler
		handler = new BackpackHandler();
		//Set keybinding
		
		PluginManager pm = this.getServer().getPluginManager();
		if (pm.isPluginEnabled("Spout") && cached.spoutguiEnabled()) {
			log.info("[Backpack] - Spout plugin detected and Enabled!!");			
			KeyBindingManager kbm = SpoutManager.getKeyBindingManager();
			try {
				kbm.registerBinding("Backpack", Keyboard.valueOf(cached.bpHotkey()), "Opens the backpack", new BackpackInputHandler(), this);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} else {
			log.info("[Backpack} - Spoutplugin was not detected or is disabled in the config.yml!");
		}
		
		if (pm.isPluginEnabled("Vault") && cached.useEconomy()) {
			RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);			
			if (economyProvider != null) {
				log.info("[Backpack] - Vault Economy system detected and Enabled!");
				econ = economyProvider.getProvider();
			} else {
				log.info("[Backpack] - Vault Economy system was not detected or is disabled in the config.yml!");
			}
		}
		
		//Register events
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackListener(), this);
	}

	public static final BackpackPlugin getInstance() {
		return instance;
	}

	public final BackpackHandler getHandler() {
		return handler;
	}

	public final CachedConfigurationUtil getCached() {
		return cached;
	}
}