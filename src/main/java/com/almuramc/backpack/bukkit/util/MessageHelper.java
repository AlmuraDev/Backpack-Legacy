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
package com.almuramc.backpack.bukkit.util;

import org.getspout.spoutapi.SpoutManager;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageHelper {
	public static void sendMessage(CommandSender sender, String message) {
		sendMessage(sender, message, null, null);
	}

	public static void sendMessage(CommandSender sender, String message, String title, Material material) {
		if (!(sender instanceof Player) || title == null || material == null) {
			sender.sendMessage(message);
			return;
		}
		SpoutManager.getPlayer((Player) sender).sendNotification(title, message, material);
	}

	public static void sendMessage(Player player, String message) {
		sendMessage(player, message, null, null);
	}

	public static void sendMessage(Player player, String message, String title, Material material) {
		if (title == null || material == null) {
			player.sendMessage(message);
			return;
		}
		SpoutManager.getPlayer(player).sendNotification(title, message, material);
	}
}
