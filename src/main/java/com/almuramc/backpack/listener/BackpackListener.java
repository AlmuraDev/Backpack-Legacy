package com.almuramc.backpack.listener;

import com.almuramc.backpack.Backpack;
import com.almuramc.backpack.api.BackpackCloseEvent;
import com.almuramc.backpack.api.BackpackOpenEvent;
import com.almuramc.backpack.core.BackpackHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.event.screen.ScreenOpenEvent;
import org.getspout.spoutapi.gui.ScreenType;

public class BackpackListener implements Listener {
	private final BackpackHandler backpackHandler;

	public BackpackListener() {
		backpackHandler = Backpack.getInstance().getBackpackHandler();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onScreenClose(ScreenCloseEvent event) {
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		if (event.getScreenType() == ScreenType.CUSTOM_SCREEN) {
			//Backpack menu has closed...save inventory first.
			backpackHandler.saveBackpackFor(event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(new BackpackCloseEvent(event.getPlayer()));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onScreenOpen(ScreenOpenEvent event) {
		if (event.isCancelled())  {
			event.setCancelled(true);
			return;
		}
		BackpackOpenEvent backpack = new BackpackOpenEvent(event.getPlayer());
		if (event.getScreenType() == ScreenType.CUSTOM_SCREEN) {
			//Backpack menu is opened...call BackpackOpenEvent and let developers manipulate it before the player does.
			Bukkit.getServer().getPluginManager().callEvent(backpack);
			if (backpack.isCancelled() || !backpack.hasBackpack()) {
				event.setCancelled(true);
				return;
			}
			backpackHandler.loadBackpackFor(event.getPlayer());
		}
	}
}
