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
package com.almuramc.backpack.bukkit.command;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.util.StorageUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BackpackCommands implements CommandExecutor {
	private final BackpackPlugin plugin;

	public BackpackCommands(BackpackPlugin instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		//TODO cleanup
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage("Must be in-game to utilize backpack commands");
		}
		Player player = (Player) commandSender;
		if (command.getName().equalsIgnoreCase("backpack")) {
			//No additional arguments
			if (strings.length == 0) {
				if (player.hasPermission("backpack.use")) {
					Inventory backpack = BackpackPlugin.getInstance().getStore().getBackpackFor(player, world);
					if (backpack != null) {
						player.openInventory(backpack);
						return true;
					}
				}
			//Open argument
			} else if (strings[0].equalsIgnoreCase("open")) {
				if (strings.length == 1) {
					if (player.hasPermission("backpack.use")) {
						Inventory backpack = BackpackPlugin.getInstance().getStore().getBackpackFor(player, world);
						if (backpack != null) {
							player.openInventory(backpack);
							return true;
						}
					}
				} else {
					//TODO Will need to figure out a way to open and save for a player.
					return true;
				}
			}
			if (strings[0].equalsIgnoreCase("workbench")) {
				if (player.hasPermission("backpack.workbench")) {
					player.openWorkbench(null, true);
				}
			}
		}
		return false;
	}
}
