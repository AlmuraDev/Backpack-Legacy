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
 * in themAlmura Development License version 1.
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
package com.almuramc.backpack.spout;

import java.io.File;

import org.apache.commons.collections.map.MultiKeyMap;

import org.spout.api.Spout;
import org.spout.api.geo.World;
import org.spout.api.inventory.Inventory;
import org.spout.api.player.Player;

public final class BackpackHandler {
	private static final File BACKPACK_ROOT = new File(BackpackPlugin.getInstance().getDataFolder(), "backpacks");
	private static final MultiKeyMap BACKPACKS = new MultiKeyMap();

	public BackpackHandler() {
		setup();
	}

	public Inventory getBackpackFor(Player player, World world, Inventory backpack) {
		return null;
	}

	public void setBackpackFor(Player player, World world, Inventory backpack) {

	}

	private Inventory loadBackpackFromFile(Player player, World world, Inventory backpack) {
		return null;
	}

	private void saveBackpackToFile(Player player, World world, Inventory backpack) {

	}

	private Inventory loadBackpackFromSQL(Player player, World world, Inventory backpack) {
		return null;
	}

	private void saveBackpackToSQL(Player player, World world, Inventory backpack) {

	}

	private void setup() {
		if (!BACKPACK_ROOT.exists()) {
			BACKPACK_ROOT.mkdirs();
		}
		for (World world : Spout.getEngine().getWorlds()) {
			File worldStor = new File(BACKPACK_ROOT, world.getName());
			if (!worldStor.exists()) {
				worldStor.mkdirs();
			}
		}
	}
}
