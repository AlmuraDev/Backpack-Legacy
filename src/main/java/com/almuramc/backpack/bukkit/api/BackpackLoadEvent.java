/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which reference GPL licensed code are hereby
 * licensed under the GNU General Public License, as described in the
 * Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the GNU General Public License (for classes that fulfill the exception) and the
 * Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU Lesser General Public License and
 * the GNU General Public License.
 */
package com.almuramc.backpack.bukkit.api;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Custom event thrown when a backpack is opened.
 */
public final class BackpackLoadEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled;
	private final Player player;
	private final World world;
	private final Inventory backpack;

	public BackpackLoadEvent(Player player, World world, Inventory backpack) {
		this.player = player;
		this.world = world;
		this.backpack = backpack;
	}

	/**
	 * Returns the player that opened their backpack.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the world in-which the player opened their backpack;
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Returns the inventory of the player's backpack (for manipulation).
	 * @return
	 */
	public Inventory getBackpack() {
		return backpack;
	}

	/**
	 * Sets the inventory of the player's backpack. Setting this to null will erase the player's backpack
	 * entry and persistent data.
	 * @param backpack
	 */
	public void setBackpack(Inventory backpack) {
		if (backpack == null) {
			this.backpack.clear();
			return;
		}
		this.backpack.setContents(backpack.getContents());
	}

	/**
	 * Returns if the player has a backpack or not.
	 * @return
	 */
	public boolean hasBackpack() {
		return backpack != null;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		isCancelled = b;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
