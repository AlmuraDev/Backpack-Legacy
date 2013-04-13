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
package com.almuradev.backpack.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Size;

import org.bukkit.inventory.ItemStack;

public final class Backpacks {
	private final Map<String, List<Backpack>> BACKPACKS = new HashMap<>();

	/**
	 * @param world
	 * @param holder
	 * @param size
	 * @param title
	 * @param initial
	 * @return
	 */
	public Backpack add(String world, String holder, Size size, String title, ItemStack... initial) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty() || size == null || title == null) {
			throw new NullPointerException("Specified world, holder, title, or size is null!");
		}
		List<Backpack> entry = BACKPACKS.get(world);
		if (entry == null) {
			entry = new ArrayList<>();
			BACKPACKS.put(world, entry);
		}
		final Backpack backpack = new Backpack(holder, size, title, initial);
		backpack.setContents(initial);
		entry.add(backpack);
		return backpack;
	}

	/**
	 * @param world
	 * @param holder
	 * @return
	 */
	public Backpack get(String world, String holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			for (Backpack backpack : entry) {
				if (backpack.getHolder().equals(holder)) {
					return backpack;
				}
			}
		}
		return null;
	}

	/**
	 * @param world
	 * @param holder
	 * @return
	 */
	public Backpack remove(String world, String holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			while (entry.iterator().hasNext()) {
				final Backpack next = entry.iterator().next();
				if (next.getHolder().equals(holder)) {
					entry.iterator().remove();
					return next;
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Map<String, List<Backpack>> getAll() {
		return Collections.unmodifiableMap(BACKPACKS);
	}

	protected void add(String world, Backpack backpack) {
		List<Backpack> entry = BACKPACKS.get(world);
		if (entry == null) {
			entry = new ArrayList<>();
			BACKPACKS.put(world, entry);
		}
		entry.add(backpack);
	}
}