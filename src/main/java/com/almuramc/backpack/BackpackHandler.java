package com.almuramc.backpack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class BackpackHandler {
	private static final YamlConfiguration parser = new YamlConfiguration();
	private final Backpack PLUGIN;
	//In-Memory store of paired player and world keys with inventories that are backpacks.
	private final MultiKeyMap INVENTORIES;
	private final File BACKPACK_ROOT;

	/**
	 * Constructs the handler to handle backpacks.
	 * @param instance
	 */
	public BackpackHandler(Backpack instance) {
		PLUGIN = instance;
		INVENTORIES = new MultiKeyMap();
		BACKPACK_ROOT = new File(PLUGIN.getDataFolder(), "backpack");
		setup();
	}

	/**
	 * Sets a backpack for a player in a world.
	 * @param player Player that will have backpack set.
	 * @param world  World the player is in.
	 * @param inventory the backpack's inventory.
	 */
	public void setBackpackFor(Player player, World world, Inventory inventory) {
		INVENTORIES.put(player, world, inventory);
	}

	/**
	 * Gets a backpack for a player in a world.
	 * @param player Player that is accessing their backpack.
	 * @param world World the player is in.
	 * @return
	 */
	public Inventory getBackpackFor(Player player, World world) {
		return (Inventory) INVENTORIES.get(player, world);
	}

	/**
	 * Loads all the backpacks from disk.
	 */
	public void loadBackpacks() {
		loadFor(null, Bukkit.getWorlds());
	}

	/**
	 * Loads a backpack for a particular player.
	 * @param player
	 */
	public void loadBackpackFor(Player player, World world) {
		//Dirty but this stops the duplication of a lot of code.
		Player[] players = {player};
		ArrayList<World> worlds = new ArrayList<World>();
		worlds.add(world);
		loadFor(players, worlds);
	}

	/**
	 * Saves all the backpacks of the online players to disk. This will not cause an issue
	 * with players that logged in and then left before this method is called as a backpack
	 * is saved right when the backpack inventory is closed.
	 */
	public void saveBackpacks() {
		saveFor(Bukkit.getOnlinePlayers(), Bukkit.getWorlds());
	}

	/**
	 * Saves a backpack for a particular player.
	 * @param player
	 */
	public void saveBackpackFor(Player player, World world) {
		//Dirty but this stops the duplication of a lot of code.
		Player[] players = {player};
		ArrayList<World> worlds = new ArrayList<World>();
		worlds.add(world);
		saveFor(players, worlds);
	}

	/**
	 * Called to setup directory structure of backpacks.
	 */
	private final void setup() {
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
	 * Handles loading from flat file backpacks. The construct of each backpack file should happen like this.
	 *
	 * Example:
	 *
	 * /backpacks/world/NinjaZidane.yml
	 *
	 * player: NinjaZidane
	 * inventory:
	 * 		WOODEN_PICKAXE:
	 * 			amount: 1
	 * 			durability: 100
	 * 			data: Some byte...
	 * 			enchantments: list of them
	 *
	 * @param players
	 * @param worlds
	 */
	private void loadFor(Player[] players, List<World> worlds) {
		for (World world : worlds) {
			File worldDir = new File(BACKPACK_ROOT, world.getName());
			ArrayList<File> playerFiles = new ArrayList<File>();
			for (File file : worldDir.listFiles()) {
				if (!file.getName().contains(".yml")) {
					continue;
				}
				//Just add any file encountered if players are null
				if (players == null) {
					playerFiles.add(file);
				} else {
					String name = (file.getName().split(".yml"))[0];
					if (Bukkit.getPlayer(name) != null) {
						playerFiles.add(file);
					}
				}
			}
			for (File file : playerFiles) {
				try {
					parser.load(file);
					if (parser != null) {
						Player player = Bukkit.getPlayer((String) parser.get("player"));
						Inventory inv = Bukkit.createInventory(player, 64, "Backpack"); //TODO This makes me sad
						ConfigurationSection parent = parser.getConfigurationSection("inventory");
						for (String key : parent.getKeys(false)) {
							ConfigurationSection sub = parent.getConfigurationSection(key);
							ItemStack item = new ItemStack(Material.getMaterial(key), sub.getInt("amount"), (Short) sub.get("durability"), (Byte) sub.get("data"));
							inv.addItem(item);
						}
						if (player != null && inv != null) {
							INVENTORIES.put(player, player.getWorld(), inv);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Handles saving to flat file for backpacks. The construct of each backpack file should happen like this.
	 *
	 * Example:
	 *
	 * /backpacks/world/NinjaZidane.yml
	 *
	 * player: NinjaZidane
	 * inventory:
	 * 		WOODEN_PICKAXE:
	 * 			amount: 1
	 * 			durability: 100
	 * 			data: Some byte...
	 * 			enchantments: list of them
	 *
	 * Note: When the backpack is saved to file it is destroyed and then re-recreated. TODO Evaluate the performance impact of this.
	 *
	 * @param players
	 * @param worlds
	 */
	private void saveFor(Player[] players, List<World> worlds) {
		for (Player plr : players) {
			for (World world : worlds) {
				File playerBackpack = new File(BACKPACK_ROOT, world.getName() + File.pathSeparator + plr.getName() + ".yml");
				if (INVENTORIES.get(plr, world) != null) {
					try {
						if (!playerBackpack.exists()) {
							playerBackpack.delete();
							playerBackpack.createNewFile();
						}
						parser.load(playerBackpack);
						parser.set("player", plr.getName());
						parser.createSection("inventory");
						ItemStack[] stacks = ((Inventory) INVENTORIES.get(plr, world)).getContents();
						for (ItemStack stack : stacks) {
							ConfigurationSection section = parser.getConfigurationSection("inventory").createSection(stack.getType().name());
							section.set("amount", stack.getAmount());
							section.set("durability", stack.getDurability());
							section.set("data", stack.getData());
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
			}
		}
	}
}
