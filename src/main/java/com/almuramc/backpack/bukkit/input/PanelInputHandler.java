package com.almuramc.backpack.bukkit.input;

import com.almuramc.backpack.bukkit.BackpackPlugin;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class PanelInputHandler implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		Player player = keyBindingEvent.getPlayer();
		World world = player.getWorld();
		if (player.hasPermission("backpack.admin")) {
			//Check if backpack is open, close if so.
			InventoryView inventory = player.getOpenInventory();
			if (inventory.getTopInventory().getTitle().equals("Backpack")) {
				BackpackPlugin.getInstance().getStore().setBackpackFor(player, world, inventory.getTopInventory());
				player.closeInventory();
			}
			//Only open backpack on game screen
			if (!keyBindingEvent.getScreenType().equals(ScreenType.GAME_SCREEN)) {
				return;
			}
			
			// Call Panel GUI

		}
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {
		//Do nothing, handled in onInventoryClose within BackpackListener
	}
}
