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
