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
 * in Almura Development License version 1.
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
package com.almuramc.backpack.bukkit.storage.mode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.almuramc.backpack.bukkit.api.BackpackLoadEvent;
import com.almuramc.backpack.bukkit.api.BackpackSaveEvent;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.storage.StorageMode;
import com.almuramc.backpack.bukkit.util.InventoryUtil;
import com.almuramc.backpack.bukkit.util.PermissionUtil;
import com.almuramc.backpack.bukkit.util.StorageUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YamlStorage implements Storage {
	private static File BACKPACK_ROOT;
	private static final YamlConfiguration reader = new YamlConfiguration();

	public YamlStorage(File parentDir) {
		BACKPACK_ROOT = new File(parentDir, "backpacks");
	}

	@Override
	public StorageMode getMode() {
		return StorageMode.FILE;
	}

	@Override
	public void onLoad() {
		if (!BACKPACK_ROOT.exists()) {
			BACKPACK_ROOT.mkdir();
		}
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			File subdir;
			try {
				subdir = new File(BACKPACK_ROOT.getCanonicalPath(), world.getName());
				if (!subdir.exists()) {
					subdir.mkdir();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUnload() {
		BACKPACK_ROOT = null;
	}

	/**
	 * Gets the inventory from the inventory memory storage or from file if not found.
	 * @param player the player to get an inventory for
	 * @param world the world to get the inventory from
	 * @return the inventory loaded from memory storage/file storage or null if the event was cancelled.
	 */
	@Override
	public Inventory getBackpackFor(Player player, World world) {
		Inventory backpack = StorageUtil.get(player, world);
		if (backpack == null) {
			return null;
		}
		if (!InventoryUtil.hasActualContents(backpack)) {
			backpack = loadFromFile(player, world);
		}
		BackpackLoadEvent event = new BackpackLoadEvent(player, world, backpack);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return null;
		}
		Inventory result = event.getBackpack();

		return result == null ? StorageUtil.put(player, world) : StorageUtil.put(player, world, result);
	}

	@Override
	public void setBackpackFor(Player player, World world, Inventory inventory) {
		BackpackSaveEvent event = new BackpackSaveEvent(player, world, inventory);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		Inventory backpack = event.getBackpack();
		if (backpack == null) {
			return;
		}
		saveToFile(player, world, backpack);
	}

	private Inventory loadFromFile(Player player, World world) {
		File worldDir = new File(BACKPACK_ROOT, world.getName());
		File playerDat = null;
		if (worldDir == null) {
			return null;
		}
		for (File file : worldDir.listFiles()) {
			if (!file.getName().contains(".yml")) {
				continue;
			}
			String name = (file.getName().split(".yml"))[0];
			if (!name.equals(player.getName())) {
				continue;
			}
			playerDat = new File(worldDir, file.getName());
		}

		//No file was found for this player.
		if (playerDat == null) {
			return null;
		}

		//File found, lets load in contents
		try {
			reader.load(playerDat);
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ConfigurationSection parent = reader.getConfigurationSection("backpack");
			Set<String> temp = parent.getKeys(false);
			String[] keys = temp.toArray(new String[temp.size()]);
			int psize = PermissionUtil.getSizeByPermFor(player);
			int size = reader.getInt("contents-amount", psize);
			if (size != psize) {
				size = psize;
			}
			for (int i = 0; i < size; i++) {
				if (i >= keys.length) {
					items.add(new ItemStack(Material.AIR));
				} else {
					ConfigurationSection sub = parent.getConfigurationSection(keys[i]);
					ItemStack item = sub.getItemStack("ItemStack", new ItemStack(Material.AIR));
					items.add(item);
				}
			}
			Inventory backpack = Bukkit.createInventory(player, size, "Backpack");
			backpack.setContents(items.toArray(new ItemStack[items.size()]));
			return backpack;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void saveToFile(Player player, World world, Inventory backpack) {
		File playerBackpack = new File(BACKPACK_ROOT + File.separator + world.getName(), player.getName() + ".yml");
		try {
			if (!playerBackpack.exists()) {
				playerBackpack.createNewFile();
				reader.load(playerBackpack);
				reader.createSection("backpack");
			} else {
				reader.load(playerBackpack);
			}
			ItemStack[] contents = backpack.getContents();
			reader.set("contents-amount", contents.length);
			ConfigurationSection parent = reader.getConfigurationSection("backpack");
			for (int i = 0; i < 54; i++) {
				//Slot doesn't exist
				ConfigurationSection slot;
				if (!reader.isConfigurationSection("Slot " + i)) {
					slot = parent.createSection("Slot " + i);
				} else {
					slot = parent.getConfigurationSection("Slot " + i);
				}
				if (i >= contents.length) {
					continue;
				}
				slot.set("ItemStack", contents[i]);
			}
			reader.save(playerBackpack);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}