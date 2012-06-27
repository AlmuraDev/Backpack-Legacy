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
package com.almuramc.backpack.bukkit.command;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.util.CachedConfiguration;

import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackpackCommands implements CommandExecutor {
	private static final CachedConfiguration CONFIG = BackpackPlugin.getInstance().getCached();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (command.getName().equalsIgnoreCase("backpack")) {
			Player player = null;
			if (commandSender instanceof Player) {
				player = (Player) commandSender;
			}

			if (strings.length == 0 && player != null) {
				return openBackpack(player);
			} else if (strings.length > 0 && strings[0].equalsIgnoreCase("reload")) {
				CONFIG.reload();
				if (player != null) {
					if (CONFIG.useSpout()) {
						((SpoutPlayer) player).sendNotification("Backpack", "Configuration reloaded", Material.CAKE);
					}
				} else {
					commandSender.sendMessage("[Backpack] Configuration reloaded");
				}
				return true;
			} else if (strings[0].equalsIgnoreCase("workbench") && player != null) {
				return openWorkbench(player);
			} else {
				commandSender.sendMessage("[Backpack] Must be in-game to utilize player-only commands!");
			}
		}
		return false;
	}

	private boolean openBackpack(Player player) {
		if (BackpackPlugin.getInstance().getHooks().getPermHook().has(player.getWorld().getName(), player.getName(), "backpack.use")) {
			player.openInventory(BackpackPlugin.getInstance().getStore().load(player, player.getWorld()));
			return true;
		}
		return false;
	}

	private boolean openWorkbench(Player player) {
		if (BackpackPlugin.getInstance().getHooks().getPermHook().has(player.getWorld().getName(), player.getName(), "backpack.workbench")) {
			player.openWorkbench(null, true);
			return true;
		}
		return false;
	}
}
