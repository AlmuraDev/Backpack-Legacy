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
import com.almuramc.backpack.bukkit.util.PermissionUtil;

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
	public void setup() {
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

	/**
	 * Gets the inventory from the inventory memory storage or from file if not found.
	 * @param player the player to get an inventory for
	 * @param world the world to get the inventory from
	 * @return the inventory loaded from memory storage/file storage or null if the event was cancelled.
	 */
	@Override
	public Inventory getBackpackFor(Player player, World world) {
		Inventory backpack = get(player, world);
		if (backpack == null) {
			return null;
		}
		ItemStack[] contents = backpack.getContents();
		boolean newFile = true;
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null) {
				newFile = false;
				break;
			}
		}
		if (newFile) {
			backpack = loadFromFile(player, world);
		}
		BackpackLoadEvent event = new BackpackLoadEvent(player, world, backpack);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return null;
		}
		Inventory result = event.getBackpack();

		return result == null ? put(player, world) : put(player, world, result);
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
			Set<String> keys = parent.getKeys(false);
			int size = PermissionUtil.getSizeByPermFor(player);
			for (int i = 0; i < size; i++) {
				ConfigurationSection sub = parent.getConfigurationSection(keys.iterator().next());
				ItemStack item = sub.getItemStack("ItemStack", new ItemStack(Material.AIR));
				items.add(item);
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
			ConfigurationSection parent = reader.getConfigurationSection("backpack");
			for (int i = 0; i < contents.length; i++) {
				//Slot doesn't exist
				ConfigurationSection slot;
				if (!reader.isConfigurationSection("Slot " + i)) {
					slot = parent.createSection("Slot " + i);
				} else {
					slot = parent.getConfigurationSection("Slot "+ i);
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