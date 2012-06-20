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
package com.almuramc.backpack.bukkit.input;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.entity.Player;

public class WorkbenchInputHandler implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		//Check if workbench is open, close if so.
		Player player = keyBindingEvent.getPlayer();
		if (keyBindingEvent.getScreenType().equals(ScreenType.WORKBENCH_INVENTORY)) {
			player.closeInventory();
		}
		if (!keyBindingEvent.getScreenType().equals(ScreenType.GAME_SCREEN)) {
			return;
		}
		if (player.hasPermission("backpack.workbench")) {
			player.openWorkbench(null, true);
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {

	}
}
