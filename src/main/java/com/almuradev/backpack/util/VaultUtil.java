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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultUtil {
	private static final Economy economy;
	private static final Permission permission;

	static {
		final RegisteredServiceProvider<Economy> ersp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		economy = ersp == null ? null : ersp.getProvider() == null ? null : ersp.getProvider();
		final RegisteredServiceProvider<Permission> prsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		permission = prsp == null ? null : prsp.getProvider() == null ? null : prsp.getProvider();
	}

	public static double getBalance(String name) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		return economy.getBalance(name);
	}

	public static void add(String name, double amount) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		if (amount > 0) {
			economy.depositPlayer(name, amount);
		} else if (amount < 0) {
			economy.withdrawPlayer(name, Math.abs(amount));
		}
	}

	public static boolean hasBalance(String name, double balance) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		return economy.has(name, balance);
	}

	public static boolean hasPermission(String name, String world, String perm) {
		if (!hasPermissions()) {
			throw new IllegalStateException("Tried to perform permission actions but no permission service installed!");
		}
		return permission.has(world, name, perm);
	}

	public static boolean hasEconomy() {
		return economy != null;
	}

	public static boolean hasPermissions() {
		return permission != null;
	}
}