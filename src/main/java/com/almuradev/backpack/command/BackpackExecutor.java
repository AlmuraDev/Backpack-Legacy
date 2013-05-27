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
				plugin.getLogger().info("The console cannot open a backpack (did you mean to run this in-game?)");
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
									plugin.getLogger().info(target.getName() + " already has a backpack");
								} else {
									if (performTransaction(target, commandSender)) {
										createBackpackFile(target, commandSender);
									}
								}
							}
						}
					}
					break;
				case "DOWNGRADE":
					break;
				case "REMOVE":
					// Console -> /backpack remove <world> <player>
					if (!(commandSender instanceof Player)) {
						if (strings.length != 3) {
							plugin.getLogger().info("The console cannot remove a backpack without specifying a world and player's name");
						} else {
							final Backpack removed = plugin.getStorage().remove(strings[1], strings[2]);
							if (removed != null) {
								plugin.getLogger().info(strings[2].endsWith("s") ? strings[2] + "' backpack has been removed" : strings[2] + "'s backpack has been removed");

								final Player target = Bukkit.getPlayerExact(strings[2]);
								if (target != null) {
									if (target.getOpenInventory().getTopInventory().getTitle().contains("My Backpack")) {
										target.closeInventory();
									}
								}
							}
						}
					} else {
						final Player player = (Player) commandSender;
						// Player -> /backpack remove
						if (strings.length == 1) {
							if (!VaultUtil.hasPermission(player.getName(), player.getWorld().getName(), Permissions.REMOVE.getValue())) {
								player.sendMessage(ChatColor.RED + "You do not have permission.");
							} else {
								final Backpack backpack = plugin.getStorage().remove(player.getWorld().getName(), player.getName());
								if (backpack == null) {
									player.sendMessage(plugin.getPrefix() + "You have no backpack to remove");
								} else {
									player.sendMessage(plugin.getPrefix() + "You removed your backpack");
								}
							}
							// Player -> /backpack remove <world> <player>
						} else if (strings.length == 3) {
							if (!VaultUtil.hasPermission(player.getName(), player.getWorld().getName(), Permissions.REMOVE_OTHER.getValue())) {
								player.sendMessage(ChatColor.RED + "You do not have permission.");
							} else {
								final Backpack backpack = plugin.getStorage().remove(strings[1], strings[2]);
								if (backpack == null) {
									player.sendMessage(plugin.getPrefix() + strings[2] + " had no backpack to remove");
								} else {
									player.sendMessage(plugin.getPrefix() + (strings[2].endsWith("s") ? strings[2] + "' backpack has been removed" : strings[2] + "'s backpack has been removed"));

									final Player target = Bukkit.getPlayerExact(strings[2]);
									if (target != null) {
										if (target.getOpenInventory().getTopInventory().getTitle().contains("My Backpack")) {
											target.closeInventory();
										}
									}
								}
							}
						} else {
							player.sendMessage(plugin.getPrefix() + "You must provide no arguments (to remove your own backpack) or the target player's world and name (ex /backpack remove world notch)");
						}
					}
					break;
				case "UPGRADE":
					break;
				case "VIEW":
					if (!(commandSender instanceof Player)) {
						plugin.getLogger().info("The console cannot view a backpack (did you mean to execute this in-game?)");
					} else {
						final Player watcher = (Player) commandSender;
						if (!VaultUtil.hasPermission(watcher.getName(), watcher.getWorld().getName(), Permissions.VIEW.getValue())) {
							watcher.sendMessage(ChatColor.RED + "You do not have permission.");
						} else {
							if (strings.length == 3) {
								final Backpack backpack = plugin.getStorage().get(strings[1], strings[2]);
								if (backpack == null) {
									watcher.sendMessage(plugin.getPrefix() + strings[2] + " does not have a backpack");
								} else if (!backpack.isCreated()) {
									watcher.sendMessage(plugin.getPrefix() + strings[2] + " has not logged-in within this session and must do so at leat once due to Bukkit restrictions");
								} else {
									watcher.openInventory(backpack.getWrapped());
								}
							} else {
								watcher.sendMessage(plugin.getPrefix() + "You need to provide a world and player's name to view a backpack");
							}
						}
					}
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
		if (messageRecipient != null) {
			if (messageRecipient instanceof Player) {
				messageRecipient.sendMessage(plugin.getPrefix() + player.getName() + " has been given a backpack");
			} else {
				plugin.getLogger().info(player.getName() + " has been given a backpack");
			}
		}
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