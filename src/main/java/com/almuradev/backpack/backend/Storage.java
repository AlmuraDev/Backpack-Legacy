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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.almuradev.backpack.BackpackPlugin;
import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Size;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public final class Storage {
	private final BackpackPlugin plugin;
	private final File dir;

	public Storage(BackpackPlugin plugin) {
		this.plugin = plugin;
		dir = new File(plugin.getDataFolder(), "backpacks");
	}

	public void onEnable() {
		try {
			Files.createDirectories(dir.toPath());
		} catch (FileAlreadyExistsException fafe) {
			;
		} catch (IOException e) {
			plugin.getLogger().severe("Could not create " + dir.getPath() + "! Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	protected void remove(String world, String holder) {
        if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
            throw new IllegalArgumentException("world or holder is null!");
        }
		try {
			Files.walkFileTree(new File(dir, world + File.separator + holder + ".yml").toPath(), new FileCleaningVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to traverse " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	protected void load(String world, String holder) {
        if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
            throw new IllegalArgumentException("world or holder is null!");
        }
		try {
			Files.walkFileTree(new File(dir, world + File.separator + holder + ".yml").toPath(), new FileLoadingVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to find " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	public Storage save(String world, Backpack backpack) {
		if (world == null || world.isEmpty() || backpack == null) {
			throw new NullPointerException("world or backpack is null!");
		}
		final Path backpackPath;
		try {
			backpackPath = new File(dir, world + File.separator + backpack.getHolder().getName() + ".yml").toPath();
			Files.deleteIfExists(backpackPath);
            Files.createDirectories(backpackPath);
			Files.createFile(backpackPath);
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save " + backpack.toString() + ". Skipping...");
			ioe.printStackTrace();
			return this;
		}
		final YamlConfiguration reader = new YamlConfiguration();
		reader.set("title", backpack.getTitle());
		reader.set("size", backpack.getSize().name());
		final ConfigurationSection contentsSection = reader.createSection("contents");
		for (int i = 0; i < Size.EXTRA_LARGE.getValue(); i++) {
			ItemStack stack;
			try {
				stack = backpack.getContents()[i];
			} catch (ArrayIndexOutOfBoundsException ignore) {
				stack = null;
			}
			contentsSection.set("slot " + i, stack);
		}
		try {
			reader.save(backpackPath.toFile());
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save " + backpack.toString());
			ioe.printStackTrace();
		}
		return this;
	}
}

class FileCleaningVisitor extends SimpleFileVisitor<Path> {
	private final BackpackPlugin plugin;

	public FileCleaningVisitor(BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		plugin.getLogger().severe("Could not find: " + path.getFileName() + ".");
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml")) {
			if (plugin.getBackend().get(path.getName(3).toString(), path.getName(4).toString().split(".yml")[0]) == null) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return FileVisitResult.TERMINATE;
	}
}

class FileLoadingVisitor extends SimpleFileVisitor<Path> {
	private final BackpackPlugin plugin;

	public FileLoadingVisitor(BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml")) {
			final Backpack toInject = createBackpack(path.toFile());
			if (toInject == null) {
				plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
				return FileVisitResult.TERMINATE;
			}
			plugin.getBackend().add(path.getName(3).toString(), toInject);
		}
		return FileVisitResult.TERMINATE;
	}

	private Backpack createBackpack(File yml) {
		final YamlConfiguration reader = YamlConfiguration.loadConfiguration(yml);
		final String title = reader.getString("title", "");
		final ConfigurationSection contentsSection = reader.getConfigurationSection("contents");
		final ItemStack[] contents = new ItemStack[Size.EXTRA_LARGE.getValue()];
		for (int i = 0; i < Size.EXTRA_LARGE.getValue(); i++) {
			contents[i] = contentsSection.getItemStack("slot " + i, null);
		}
		return new Backpack(yml.toPath().getName(4).toString().split(".yml")[0], Size.get(reader.getInt("size", Size.SMALL.getValue())), title, contents);
	}
}