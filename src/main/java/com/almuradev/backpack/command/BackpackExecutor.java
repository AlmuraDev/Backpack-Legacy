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
import com.almuradev.backpack.util.Permissions;
import com.almuradev.backpack.util.Size;
import com.almuradev.backpack.util.VaultUtil;

import org.bukkit.Bukkit;
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
						// player -> /backpack create
						if (strings.length == 1) {
							if (!VaultUtil.hasPermission(player.getName(), player.getWorld().getName(), Permissions.CREATE.getValue())) {
								player.sendMessage(ChatColor.RED + "You do not have permission.");
							} else {
								if (plugin.getStorage().get(player.getWorld().getName(), player) != null) {
									player.sendMessage(plugin.getPrefix() + "You already have a backpack (were you wanting to get a bigger size? Try /backpack upgrade <size> instead).");
								} else {
									if (performTransaction(player)) {
										createBackpackFile(player);
									}
								}
							}
							// player -> /backpack create <player>
						} else {
							if (!VaultUtil.hasPermission(player.getName(), player.getWorld().getName(), Permissions.CREATE_OTHER.getValue())) {
								player.sendMessage(ChatColor.RED + "You do not have permission.");
							} else {
								final Player target = Bukkit.getPlayerExact(strings[1]);
								if (target == null) {
									player.sendMessage(plugin.getPrefix() + strings[1] + " is offline");
								} else {
									if (plugin.getStorage().get(target.getWorld().getName(), target) != null) {
										player.sendMessage(plugin.getPrefix() + target.getName() + " already has a backpack.");
									} else {
										if (performTransaction(target, player)) {
											createBackpackFile(target, player);
										}
									}
								}
							}
						}
						// console -> /backpack create
					} else {
						if (strings.length == 1) {
							plugin.getLogger().info("The console cannot create a backpack (did you mean to specify a player's name?");
							// console -> /backpack create <player>
						} else {
							final Player target = Bukkit.getPlayerExact(strings[1]);
							if (target == null) {
								plugin.getLogger().info(strings[1] + " is offline");
							} else {
								if (plugin.getStorage().get(target.getWorld().getName(), target) != null) {
									plugin.getLogger().info(target.getName() + " already has a backpack.");
								} else {
									if (performTransaction(target, commandSender)) {
										createBackpackFile(target, commandSender);
									}
								}
							}
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

	private void createBackpackFile(final Player player) {
		createBackpackFile(player, null);
	}

	private void createBackpackFile(final Player player, final CommandSender messageRecipient) {
		final Path path = Paths.get(plugin.getDataFolder().getPath() + File.separator + "backpacks" + File.separator + player.getWorld().getName());
		try {
			Files.createDirectories(path);
			Files.createFile(Paths.get(path.toString() + File.separator + player.getName() + ".yml"));
		} catch (IOException e) {
			if (messageRecipient == null) {
				player.sendMessage(plugin.getPrefix() + "An error occurred while creating your backpack. Report this to the server administrator.");
			} else {
				if (messageRecipient instanceof Player) {
					messageRecipient.sendMessage(plugin.getPrefix() + "An error occurred when creating " + player.getName() + "'s backpack. Report this to the server administrator.");
				} else {
					plugin.getLogger().severe("An error occurred when creating " + player.getName() + "'s backpack.");
				}
			}
			e.printStackTrace();
		}
		plugin.getStorage().add(player.getWorld().getName(), player, Size.SMALL).setDirty(true);
		player.sendMessage(plugin.getPrefix() + "You have acquired a backpack");
	}

	private boolean performTransaction(final Player player) {
		return performTransaction(player, null);
	}

	private boolean performTransaction(final Player player, final CommandSender messageRecipient) {
		if (plugin.getConfiguration().isEconomyEnabled() && VaultUtil.hasEconomy()) {
			final double cost = plugin.getConfiguration().getCostFor(Size.SMALL);
			if (!VaultUtil.hasBalance(player.getName(), cost)) {
				if (messageRecipient == null) {
					player.sendMessage(plugin.getPrefix() + "You do not have enough money");
				} else {
					messageRecipient.sendMessage(plugin.getPrefix() + player.getName() + " did not have enough money");
				}
				return false;
			} else {
				if (messageRecipient == null) {
					player.sendMessage(plugin.getPrefix() + "You were charged [" + ChatColor.RED + cost + ChatColor.RESET + "]");
				} else {
					messageRecipient.sendMessage(plugin.getPrefix() + player.getName() + " was charged [" + ChatColor.RED + cost + ChatColor.RESET + "]");
				}
			}
		}
		return true;
	}
}