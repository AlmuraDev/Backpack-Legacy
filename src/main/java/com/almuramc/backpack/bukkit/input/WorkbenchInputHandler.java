package com.almuramc.backpack.bukkit.input;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;


public class WorkbenchInputHandler implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		Player player = keyBindingEvent.getPlayer();
		World world = player.getWorld();
		if (player.hasPermission("backpack.workbench")) {
			//Check if workbench is open, close if so.
			if (keyBindingEvent.getScreenType().equals(ScreenType.WORKBENCH_INVENTORY)) {
				player.closeInventory();
			}
			if (!keyBindingEvent.getScreenType().equals(ScreenType.GAME_SCREEN)) {
				return;
			}
			Inventory workbench = Bukkit.createInventory(player, InventoryType.CRAFTING);		
			player.openWorkbench(null, true);
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {
		//Do nothing, handled in onInventoryClose within BackpackListener
	}
}
