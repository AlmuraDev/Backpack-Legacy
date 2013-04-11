package com.almuradev.backpack.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.almuradev.backpack.inventory.Backpack;
import com.almuradev.backpack.util.Size;

import org.bukkit.inventory.ItemStack;

public final class Backpacks {
	private final Map<String, List<Backpack>> BACKPACKS = new HashMap<>();
	private final Storage storage;

	public Backpacks(Storage storage) {
		this.storage = storage;
	}

	/**
	 * Called when the backend is initialized.
	 */
	public void onEnable() {
		storage.load();
	}

	/**
	 * @param world
	 * @param holder
	 * @param size
	 * @param title
	 * @param initial
	 * @return
	 */
	public Backpack add(String world, String holder, Size size, String title, ItemStack... initial) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty() || size == null || title == null) {
			throw new NullPointerException("Specified world, holder, title, or size is null!");
		}
		List<Backpack> entry = BACKPACKS.get(world);
		if (entry == null) {
			entry = new ArrayList<>();
			BACKPACKS.put(world, entry);
		}
		final Backpack backpack = new Backpack(holder, size, title, initial);
		backpack.setContents(initial);
		entry.add(backpack);
		return backpack;
	}

	/**
	 * @param world
	 * @param holder
	 * @return
	 */
	public Backpack get(String world, String holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			for (Backpack backpack : entry) {
				if (backpack.getHolder().equals(holder)) {
					return backpack;
				}
			}
		}
		return null;
	}

	/**
	 * @param world
	 * @param holder
	 * @return
	 */
	public Backpack remove(String world, String holder) {
		if (world == null || world.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		final List<Backpack> entry = BACKPACKS.get(world);
		if (entry != null) {
			while (entry.iterator().hasNext()) {
				final Backpack next = entry.iterator().next();
				if (next.getHolder().equals(holder)) {
					entry.iterator().remove();
					return next;
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Map<String, List<Backpack>> getAll() {
		return Collections.unmodifiableMap(BACKPACKS);
	}

	protected void add(String world, Backpack backpack) {
		List<Backpack> entry = BACKPACKS.get(world);
		if (entry == null) {
			entry = new ArrayList<>();
			BACKPACKS.put(world, entry);
		}
		entry.add(backpack);
	}
}