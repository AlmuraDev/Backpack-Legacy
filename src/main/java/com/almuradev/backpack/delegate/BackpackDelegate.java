/*
 * This file is part of Backpack.
 *
 * © 2012-2013 AlmuraDev <http://www.almuradev.com/>
 * Backpack is licensed under the GNU General Public License.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in GNU General Public License.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuradev.backpack.delegate;

import com.almuradev.backpack.BackpackPlugin;
import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Permissions;
import com.almuradev.backpack.util.VaultUtil;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.entity.Player;

public final class BackpackDelegate implements BindingExecutionDelegate {
	private final BackpackPlugin plugin;

	public BackpackDelegate(BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		final Player player = keyBindingEvent.getPlayer();
		if (player.getOpenInventory().getTopInventory().getTitle().contains("Backpack")) {
			player.closeInventory();
		} else if (keyBindingEvent.getScreenType() == ScreenType.GAME_SCREEN) {
			if (VaultUtil.hasPermission(player.getName(), player.getWorld().getName(), Permissions.OPEN.getValue())) {
				final Backpack backpack = plugin.getStorage().get(player.getWorld().getName(), player);
				if (backpack.isCreated()) {
					player.openInventory(backpack.getWrapped());
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {

	}
}
