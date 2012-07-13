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
import com.almuramc.backpack.bukkit.inventory.BackpackInventory;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.util.CachedConfiguration;
import com.almuramc.backpack.bukkit.util.PermissionHelper;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackpackCommands implements CommandExecutor {
	private static final Storage STORE = BackpackPlugin.getInstance().getStore();
	private static final CachedConfiguration CONFIG = BackpackPlugin.getInstance().getCached();
	private static final Economy ECON = BackpackPlugin.getInstance().getHooks().getEconHook();
	private static final Permission PERM = BackpackPlugin.getInstance().getHooks().getPermHook();

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
			} else if (strings.length > 0 && strings[0].equalsIgnoreCase("upgrade") && player != null) {
				if (!PERM.has(player.getWorld().getName(), player.getName(), "backpack.upgrade")) {
					return true;
				}
				BackpackInventory backpack = STORE.fetch(player, player.getWorld());
				if (backpack.getSize() >= 54) {
					commandSender.sendMessage("{Backpack] You already have the maximun size of backpack allowed!");
					return true;
				}
				int newSize = backpack.getSize() + 9;
				if (newSize > PermissionHelper.getSizeByPermFor(player)) {
					commandSender.sendMessage("{Backpack] You already have the maximun size of backpack allowed for your server's permissions!");
					return true;
				}
				if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
					double cost = CONFIG.getUpgradeCosts().get("Slot" + newSize);
					if (!ECON.has(player.getName(), cost)) {
						commandSender.sendMessage("{Backpack] You do not have enough money!");
						return true;
					}
					ECON.withdrawPlayer(player.getName(), cost);
				}
				backpack.setSize(player, newSize);
				STORE.save(player, player.getWorld(), backpack);
				return true;
			} else {
				commandSender.sendMessage("[Backpack] Must be in-game to utilize player-only commands!");
			}
		}
		return false;
	}

	private boolean openBackpack(Player player) {
		if (PERM.has(player.getWorld().getName(), player.getName(), "backpack.use")) {
			player.openInventory(STORE.load(player, player.getWorld()).getInventory());
			return true;
		}
		return false;
	}
}
