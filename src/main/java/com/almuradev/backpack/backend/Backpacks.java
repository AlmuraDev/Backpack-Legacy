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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Size;

import org.bukkit.entity.Player;

public final class Backpacks {
	private final Map<String, List<Backpack>> BACKPACKS = new HashMap<>();

	public Backpack add(String world, Player holder, Size size) {
		if (world == null || world.isEmpty() || holder == null || size == null) {
			throw new NullPointerException("Specified world, holder, or size is null!");
		}
		List<Backpack> entry = BACKPACKS.get(world);
		if (entry == null) {
			entry = new ArrayList<>();
			BACKPACKS.put(world, entry);
		}
		final Backpack backpack = new Backpack(holder, size);
		entry.add(backpack);
		return backpack;
	}

	public Backpack get(String world, Player holder) {
		if (world == null || world.isEmpty() || holder == null) {
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

	public Backpack remove(String world, String holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			final Iterator<Backpack> iterator = entry.iterator();
			while (iterator.hasNext()) {
				final Backpack next = iterator.next();
				if (next.getRawHolder().equals(holder)) {
					iterator.remove();
					return next;
				}
			}
		}
		return null;
	}

	public boolean contains(String world, Player holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			for (Backpack backpack : entry) {
				if (backpack.getHolder().equals(holder)) {
					return true;
				}
			}
		}
		return false;
	}

	//INTERNAL USE ONLY

	public void add(final String world, final Backpack backpack) {
		List<Backpack> backpacks = BACKPACKS.get(world);
		if (backpacks == null) {
			backpacks = new ArrayList<>();
			BACKPACKS.put(world, backpacks);
		}
		backpacks.add(backpack);
	}

	public Backpack get(String world, String holder) {
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			for (Backpack backpack : entry) {
				if (backpack.getRawHolder().equals(holder)) {
					return backpack;
				}
			}
		}
		return null;
	}
}