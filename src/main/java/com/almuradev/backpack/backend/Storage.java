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

import org.bukkit.Bukkit;
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
			Files.createDirectory(dir.toPath());
		} catch (FileAlreadyExistsException fafe) {
			;
		} catch (IOException e) {
			plugin.getLogger().severe("Could not create " + dir.getPath() + "! Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	protected void cleanup() {
		try {
			Files.walkFileTree(dir.toPath(), new FileCleaningVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to traverse " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	protected void load() {
		try {
			Files.walkFileTree(dir.toPath(), new FileLoadingVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to traverse " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	public Storage save(String world, Backpack backpack) {
		if (world == null || world.isEmpty() || backpack == null) {
			throw new NullPointerException("Trying to save a null world or backpack to the storage backend!");
		}
		Path worldDir;
		final File worldRaw = new File(dir, world);
		try {
			worldDir = Files.createDirectory(worldRaw.toPath());
		} catch (FileAlreadyExistsException fafe) {
			worldDir = worldRaw.toPath();
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save " + backpack.toString() + ". Skipping...");
			ioe.printStackTrace();
			return this;
		}
		final Path backpackPath;
		try {
			backpackPath = new File(worldDir.toFile(), backpack.getHolder().getName() + ".yml").toPath();
			Files.deleteIfExists(backpackPath);
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
		plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml") && path.getNameCount() == 5) {
			if (plugin.getBackend().get(path.getName(3).toString(), path.getName(4).toString().split(".yml")[0]) == null) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return FileVisitResult.CONTINUE;
	}
}

class FileLoadingVisitor extends SimpleFileVisitor<Path> {
	private final BackpackPlugin plugin;

	public FileLoadingVisitor(BackpackPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml") && path.getNameCount() == 5) {
			final Backpack toInject = createBackpack(path.getName(4).toString().split(".yml")[0], path.toFile());
			if (toInject == null) {
				plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
				return FileVisitResult.CONTINUE;
			}
			plugin.getBackend().add(path.getName(3).toString(), toInject);
		}
		return FileVisitResult.CONTINUE;
	}

	private Backpack createBackpack(String holder, File yml) {
		final YamlConfiguration reader = YamlConfiguration.loadConfiguration(yml);
		final String title = reader.getString("title", "");
		final int size = Size.valueOf(reader.getString("size", Size.SMALL.name())).getValue();
		final ConfigurationSection contentsSection = reader.getConfigurationSection("contents");
		final ItemStack[] contents = new ItemStack[Size.EXTRA_LARGE.getValue()];
		for (int i = 0; i < Size.EXTRA_LARGE.getValue(); i++) {
			contents[i] = contentsSection.getItemStack("slot " + i, null);
		}
		return new Backpack(holder, Size.get(size), title, contents);
	}
}