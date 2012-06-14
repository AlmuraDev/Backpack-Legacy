package com.almuramc.backpack.bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.almuramc.backpack.bukkit.api.BackpackLoadEvent;
import com.almuramc.backpack.bukkit.api.BackpackSaveEvent;

import org.apache.commons.collections.map.MultiKeyMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Serves as a handler for backpacks.
 */
public final class BackpackHandler {
	private static final File BACKPACK_ROOT = new File(BackpackPlugin.getInstance().getDataFolder(), "backpacks");
	private static final MultiKeyMap INVENTORIES = new MultiKeyMap();
	private static final YamlConfiguration parser = new YamlConfiguration();

	public BackpackHandler() {
		setup();
	}

	public void setBackpackFor(Player player, World world, Inventory inventory) {
		BackpackSaveEvent event = new BackpackSaveEvent(player, world, inventory);
		Inventory backpack = event.getBackpack();
		//Cancel saving but don't delete files
		if (event.isCancelled()) {
			return;
		}
		if (backpack == null || backpack.getContents().length <= 0) {
			INVENTORIES.remove(player, world);
		}
		//Check to see if it is SQL or flat file and call appropriate method
		if (!BackpackPlugin.getInstance().getCached().isSQLEnabled()) {
			saveToFile(player, world, inventory);
			return;
		}
		saveToSQL(player, world, inventory);
	}

	public Inventory getBackpackFor(Player player, World world) {
		Inventory currentBackpack = (Inventory) INVENTORIES.get(player, world);
		//If they have a null backpack, assume they don't have it loaded from disk and try to fetch it.
		if (currentBackpack == null || currentBackpack.getContents().length <= 0) {
			//Check here if SQL or flat file and execute relative method
			if (!BackpackPlugin.getInstance().getCached().isSQLEnabled()) {
				currentBackpack = loadFromFile(player, world);
			} else {
				currentBackpack = loadFromSQL(player, world);
			}
		}
		BackpackLoadEvent event = new BackpackLoadEvent(player, world, currentBackpack);
		Inventory backpack = event.getBackpack();
		//If they still have a null backpack by this point, assume they will not have a backpack period.
		if (backpack == null || event.isCancelled()) {
			return null;
		}
		if (!currentBackpack.equals(backpack)) {
			INVENTORIES.put(player, world, event.getBackpack());
		}
		return backpack;
	}

	private Inventory loadFromFile(Player player, World world) {
		File worldDir = new File(BACKPACK_ROOT, world.getName());
		File playerDat = null;
		for (File file : worldDir.listFiles()) {
			if (!file.getName().contains(".yml")) {
				continue;
			}
			String name = (file.getName().split(".yml"))[0];
			if (!name.equals(player.getName())) {
				continue;
			}
			playerDat = new File(worldDir, name);
		}

		//No file was found for this player, return a blank empty inventory then.
		if (playerDat == null) {
			return Bukkit.createInventory(player, 63, "Backpack"); //TODO return size based on player or global perm
		}

		//File found, lets load in contents
		try {
			parser.load(playerDat);
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ConfigurationSection parent = parser.getConfigurationSection("backpack");
			for (String key : parent.getKeys(false)) {
				ConfigurationSection sub = parent.getConfigurationSection(key);
				ItemStack item = new ItemStack(Material.getMaterial(key), sub.getInt("amount"), (Short) sub.get("durability"), (Byte) sub.get("data"));
				items.add(item);
			}

			Inventory backpack = Bukkit.createInventory(player, 63, "Backpack");
			backpack.setContents((ItemStack[]) items.toArray());
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
		File playerBackpack = new File(BACKPACK_ROOT + File.pathSeparator + world.getName(), player.getName() + ".yml");
		try {
			//Delete the current file (it saves a lot of hassle and code, just delete and remake with contents)
			if (playerBackpack.exists()) {
				playerBackpack.delete();
			}
			//Stop saving if null backpack
			if (backpack == null) {
				return;
			}
			//If creating the new file failed for some reason stop saving.
			if (!playerBackpack.createNewFile()) {
				return;
			}
			parser.load(playerBackpack);
			parser.createSection("backpack");
			ItemStack[] stacks = backpack.getContents();
			for (ItemStack stack : stacks) {
				if (stack == null) {
					continue;
				}
				ConfigurationSection section = parser.getConfigurationSection("backpack").createSection(stack.getType().name());
				section.set("amount", stack.getAmount());
				section.set("durability", stack.getDurability());
				section.set("data", stack.getData().getData());
				section.set("enchantments", stack.getEnchantments());
			}
			parser.save(playerBackpack);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Inventory loadFromSQL(Player player, World world) {
		return null;
	}

	private void saveToSQL(Player player, World world, Inventory backpack) {

	}

	/**
	 * Called to setup directory structure of backpacks.
	 */
	private void setup() {
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
}
