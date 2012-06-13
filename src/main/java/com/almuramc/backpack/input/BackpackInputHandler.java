package com.almuramc.backpack.input;

import com.almuramc.backpack.BackpackPlugin;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BackpackInputHandler implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		Player player = keyBindingEvent.getPlayer();
		World world = player.getWorld();
		Inventory backpack = BackpackPlugin.getInstance().getHandler().getBackpackFor(player, world);
		if (backpack == null) {
			return;
		}

		player.openInventory(backpack);
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {
		//Do nothing, handled in onInventoryClose within BackpackListener
	}
}
