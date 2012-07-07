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
package com.almuramc.backpack.bukkit.storage.type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.almuramc.backpack.bukkit.inventory.BackpackInventory;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.util.PermissionHelper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YamlStorage extends Storage {
	private static File STORAGE_DIR;
	private static final YamlConfiguration READER = new YamlConfiguration();

	public YamlStorage(File parentDir) {
		STORAGE_DIR = new File(parentDir, "backpacks");
	}

	@Override
	public void initWorld(World world) {
		File worldDir = new File(STORAGE_DIR, world.getName());
		if (!worldDir.exists()) {
			worldDir.mkdirs();
		}
	}

	@Override
	public BackpackInventory load(Player player, World world) {

	}

	@Override
	public void save(Player player, World world, BackpackInventory backpack) {

	}

	private BackpackInventory loadFromFile(Player player, World world) {
		File worldDir = new File(STORAGE_DIR, world.getName());
		File playerDat = null;
		for (File file : worldDir.listFiles()) {
			if (file.getName().contains(".yml") && file.getName().startsWith(player.getName())) {
				playerDat = new File(worldDir, file.getName());
				break;
			}
		}

		//File found, lets load in contents
		if (playerDat == null) {
			return null;
		}

		try {
			READER.load(playerDat);
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ConfigurationSection parent;
			if (READER.getConfigurationSection("backpack") == null) {
				parent = READER.getDefaultSection();
			} else {
				parent = READER.getConfigurationSection("backpack");
			}
			Set<String> temp = parent.getKeys(false);
			String[] keys = temp.toArray(new String[temp.size()]);
			int psize = PermissionHelper.getSizeByPermFor(player);
			int size = READER.getInt("contents-amount", psize);
			if (size > psize) {
				size = psize;
			}
			for (int i = 0; i < size; i++) {
				if (i >= keys.length) {
					break;
				} else {
					items.add(parent.getConfigurationSection(keys[i]).getItemStack("ItemStack", new ItemStack(Material.AIR)));
				}
			}
			BackpackInventory backpack = new BackpackInventory(player, Bukkit.createInventory(player, size, "Backpack"));
			backpack.setContents(items.toArray(new ItemStack[items.size()]));
			return backpack;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveToFile(Player player, World world, BackpackInventory backpack) {
		File playerBackpack = new File(store_dir + File.separator + world.getName(), player.getName() + ".yml");
		try {
			if (!playerBackpack.exists()) {
				playerBackpack.createNewFile();
			} else {
				READER.load(playerBackpack);
			}
			if (READER.getConfigurationSection()) {
				ItemStack[] contents = backpack.getContents();
			}
			READER.set("contents-amount", contents.length);
			for (int i = 0; i < 54; i++) {
				ConfigurationSection slot;
				if (!READER.isConfigurationSection("Slot " + i)) {
					slot = READER.createSection("Slot " + i);
				} else {
					slot = READER.getConfigurationSection("Slot " + i);
				}
				if (i >= contents.length) {
					continue;
				}
				slot.set("ItemStack", contents[i]);
			}
			READER.save(playerBackpack);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public File getStorageDirectory() {
		return STORAGE_DIR;
	}
}