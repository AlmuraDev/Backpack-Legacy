package com.almuramc.backpack.bukkit.storage;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * FileStorage modes for backpacks
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