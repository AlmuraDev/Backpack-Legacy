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
package com.almuradev.backpack.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.almuradev.backpack.BackpackPlugin;
import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Size;
import com.almuradev.backpack.util.VaultUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BackpackExecutor implements CommandExecutor {
	private final BackpackPlugin plugin;

	public BackpackExecutor(final BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		//Only executed /backpack
		if (strings.length == 0) {
			if (!(commandSender instanceof Player)) {
				plugin.getLogger().warning("The console cannot open a backpack (did you mean to run this in-game?)");
			} else {
				final Player player = (Player) commandSender;
				final Backpack backpack = plugin.getStorage().get(player.getWorld().getName(), player);
				if (backpack == null) {
					player.sendMessage(plugin.getPrefix() + "You currently do not have a backpack (Did you forget to obtain/purchase one with /backpack create?)");
				} else {
					player.openInventory(backpack.getWrapped());
				}
			}
		} else {
			switch (strings[0].toUpperCase()) {
				case "CREATE":
					if (commandSender instanceof Player) {
						final Player player = (Player) commandSender;
						// /backpack create
						if (strings.length == 1) {
							if (plugin.getStorage().get(player.getWorld().getName(), player) != null) {
								player.sendMessage(plugin.getPrefix() + "You already have a backpack (were you wanting to get a bigger size? Try /backpack upgrade <size> instead).");
							} else {
								boolean create = true;
								if (plugin.getConfiguration().isEconomyEnabled() && VaultUtil.hasEconomy()) {
									final double cost = plugin.getConfiguration().getCostFor(Size.SMALL);
									if (!VaultUtil.hasBalance(player.getName(), cost)) {
										player.sendMessage(plugin.getPrefix() + "You do not have enough money for a backpack. They cost [" + ChatColor.RED + cost + ChatColor.RESET + "]");
										create = false;
									} else {
										player.sendMessage(plugin.getPrefix() + "You were charged [" + ChatColor.RED + cost + ChatColor.RESET + "]");
									}
								}
								if (create) {
									final Path path = Paths.get(plugin.getDataFolder().getPath() + File.separator + "backpacks" + File.separator + player.getWorld().getName());
									try {
										Files.createDirectories(path);
										Files.createFile(Paths.get(path.toString() + File.separator + player.getName() + ".yml"));
									} catch (IOException e) {
										player.sendMessage(plugin.getPrefix() + "An error occurred when creating your backpack. Report this to the server administrator.");
										e.printStackTrace();
									}
									plugin.getStorage().add(player.getWorld().getName(), player, Size.SMALL).setDirty(true);
									player.sendMessage(plugin.getPrefix() + "You have acquired a backpack");
								}
							}
						// /backpack create <player>
						}  else {

						}
					}
				case "DOWNGRADE":
					break;
				case "REMOVE":
					break;
				case "UPGRADE":
					break;
				case "VIEW":
					break;
				default:
					return false;
			}
		}
		return true;
	}
}
