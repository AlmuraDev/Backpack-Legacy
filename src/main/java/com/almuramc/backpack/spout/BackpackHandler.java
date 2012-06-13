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
