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
package com.almuradev.backpack.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.almuradev.backpack.util.Size;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Wrapper class that wraps an {@link Inventory} and grants it mutability.
 * <p/>
 * Assumptions are made in concern to the {@link org.bukkit.inventory.InventoryHolder} which holds an inventory. This class enforces it to a {@link Player}.
 */
public final class Backpack {
	//Initial state
	private final String INITIAL_HOLDER;
	private final Size INITIAL_SIZE;
	private final String INITIAL_TITLE;
	private final ItemStack[] INITIAL_CONTENTS;
	//Mutability
	private Inventory inventory;
	private boolean dirty = false;

	public Backpack(final Player holder, final Size size) {
		if (holder == null || size == null) {
			throw new IllegalArgumentException("Holder or size is invalid!");
		}

		INITIAL_HOLDER = holder.getName();
		INITIAL_SIZE = size;
		INITIAL_TITLE = holder.getName().endsWith("s") ? ChatColor.BLUE + holder.getName() + "' " + ChatColor.RESET + "Backpack" : ChatColor.BLUE + holder.getName() + "'s " + ChatColor.RESET + "Backpack";
		INITIAL_CONTENTS = null;

		inventory = Bukkit.createInventory(holder, INITIAL_SIZE.getValue(), INITIAL_TITLE);
	}

	public Backpack(final String holder, final Size size, final ItemStack... contents) {
		if (holder == null || holder.isEmpty() || size == null) {
			throw new IllegalArgumentException("Holder or size is invalid!");
		}

		INITIAL_HOLDER = holder;
		INITIAL_SIZE = size;
		INITIAL_TITLE = holder.endsWith("s") ? ChatColor.BLUE + holder + "' " + ChatColor.RESET + "Backpack" : ChatColor.BLUE + holder + "'s"  + ChatColor.RESET + "Backpack";
		INITIAL_CONTENTS = contents;
	}

	/**
	 * Gets the holder ({@link Player}).
	 * @return The holder of the backpack
	 */
	public Player getHolder() {
		return (Player) inventory.getHolder();
	}

	/**
	 * Returns the wrapped {@link Inventory}'s contents.
	 * <p/>
	 * Guaranteed to never be null.
	 * @return The inner contents
	 */
	public ItemStack[] getContents() {
		return inventory.getContents();
	}

	/**
	 * Sets the contents of the internal {@link Inventory}.
	 * @param contents The new contents
	 * @return This object, for chaining
	 */
	public Backpack setContents(final ItemStack... contents) {
		inventory.setContents(contents);
		setDirty(true);
		return this;
	}

	/**
	 * Gets the title this Backpack will show to the client's screen.
	 * @return The string representing the title
	 */
	public String getTitle() {
		return inventory.getTitle();
	}

	/**
	 * Gets the size of the internal {@link Inventory}.
	 * @return The size
	 */
	public Size getSize() {
		return Size.get(inventory.getSize());
	}

	/**
	 * Sets the size of the inner {@link Inventory}.
	 * @param size The new size of the backpack
	 * @return This object, for chaining
	 */
	public Backpack setSize(final Size size) {
		if (!size.equals(getSize())) {
			final Inventory inventory = Bukkit.createInventory(getHolder(), size.getValue(), getTitle());
			inventory.setContents(getContents());
			this.inventory = inventory;
			setDirty(true);
		}
		return this;
	}

	/**
	 * Filters out all illegal {@link ItemStack}s within the {@link Inventory}.
	 * <p/>
	 * Returns a collection of the illegal item stacks found.
	 * @param blacklisted Collection of names which correspond to {@link Material} names
	 * @return A collection of the illegal item stacks found
	 */
	public ItemStack[] filterIllegalItems(Set<String> blacklisted) {
		final List<ItemStack> contentsList = Arrays.asList(getContents());
		final List<ItemStack> illegalContents = new ArrayList<>();
		while (contentsList.iterator().hasNext()) {
			final ItemStack content = contentsList.iterator().next();
			if (blacklisted.contains(content.getType().name().toLowerCase())) {
				illegalContents.add(content);
				contentsList.iterator().remove();
				dirty = true;
			}
		}
		setContents(contentsList.toArray(new ItemStack[contentsList.size()]));
		return illegalContents.toArray(new ItemStack[illegalContents.size()]);
	}

	/**
	 * Returns if this object is marked as dirty and needs to be persisted.
	 * @return True if dirty, false if not
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the dirty state of this object. INTERNAL USE ONLY.
	 * @param dirty Dirty state
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Backpack)) {
			return false;
		}

		final Backpack backpack = (Backpack) other;
		return getHolder().equals(backpack.getHolder()) && getContents().equals(backpack.getContents());
	}

	@Override
	public String toString() {
		return "Backpack{" + inventory.toString() + ", dirty= " + dirty + "}";
	}

	//INTERNAL USE ONLY

	/**
	 * Called when the player logs in. The goal is to hot inject the {@link Inventory} with the loaded filesystem values.
	 * <p/>
	 * This overcomes Bukkit's restrictions on {@link org.bukkit.inventory.InventoryHolder}s not being {@link org.bukkit.OfflinePlayer}s.
	 */
	public void create() {
		final Player player = Bukkit.getPlayerExact(INITIAL_HOLDER);
		if (player == null) {
			throw new IllegalStateException("Attempting to create a Backpack for a player who is offline!");
		}
		inventory = Bukkit.createInventory(player, INITIAL_SIZE.getValue(), INITIAL_TITLE);
		inventory.setContents(INITIAL_CONTENTS);
	}

	public boolean isCreated() {
		return inventory != null;
	}

	public Inventory getWrapped() {
		return inventory;
	}

	public String getRawHolder() {
		return INITIAL_HOLDER;
	}
}