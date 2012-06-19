/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the GNU Public License version 3.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Backpack.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.almuramc.backpack.bukkit.storage;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Enum of storage modes
 */
public enum StorageMode {
	FILE(0),
	SQL(1);
	final int id;
	private final static TIntObjectMap<StorageMode> handlers = new TIntObjectHashMap<StorageMode>();

	private StorageMode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static StorageMode get(int id) {
		return StorageMode.get(id);
	}

	static {
		for (StorageMode sm : StorageMode.values()) {
			handlers.put(sm.getId(), sm);
		}
	}
}