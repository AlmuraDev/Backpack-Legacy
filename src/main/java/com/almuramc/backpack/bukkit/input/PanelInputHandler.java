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
package com.almuramc.backpack.bukkit.input;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.gui.UpgradePanel;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class PanelInputHandler implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		Player player = keyBindingEvent.getPlayer();
		World world = player.getWorld();
		SpoutPlayer sPlayer = keyBindingEvent.getPlayer();
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
			
			sPlayer.getMainScreen().attachPopupScreen(new UpgradePanel(sPlayer));	
			// Call Panel GUI

		}
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {
		//Do nothing, handled in onInventoryClose within BackpackListener
	}
}
