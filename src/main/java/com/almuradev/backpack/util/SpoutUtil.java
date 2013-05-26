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
package com.almuradev.backpack.util;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Utility class that enables optional Spout support for Bukkit plugins.
 */
public final class SpoutUtil {
	public static boolean isSpoutEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("Spout");
	}

	public static void bind(String ident, Keyboard key, String description, BindingExecutionDelegate delegate, Plugin plugin) {
		SpoutManager.getKeyBindingManager().registerBinding(ident, key, description, delegate, plugin);
	}

	public static void message(Player player, String title, String message, Material icon) {
		SpoutManager.getPlayer(player).sendNotification(title, message, icon);
	}
}
