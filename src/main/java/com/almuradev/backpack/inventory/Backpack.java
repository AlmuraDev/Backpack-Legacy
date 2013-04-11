package com.almuradev.backpack.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.almuradev.backpack.util.Size;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Wrapper class that wraps an {@link Inventory} and grants it mutability.
 * <p/>
 * Assumptions are made in concern to the {@link org.bukkit.inventory.InventoryHolder} which holds an inventory. This class enforces it to a {@link Player}.
 */
public final class Backpack {
	private Inventory inventory;
	private boolean dirty = false;

	public Backpack(final String holder) {
		this(holder, Size.SMALL, "My Backpack", new ItemStack[0]);
	}

	public Backpack(final String holder, final String title) {
		this(holder, Size.SMALL, title, new ItemStack[0]);
	}

	public Backpack(final String holder, final Size size, final String title) {
		this(holder, size, title, new ItemStack[0]);
	}

	public Backpack(final String holder, final String title, final ItemStack... contents) {
		this(holder, Size.SMALL, title, contents);
	}

	public Backpack(final String holder, final Size size, final ItemStack... contents) {
		this(holder, size, "My Backpack", contents);
	}

	public Backpack(final String holder, final Size size, final String title, final ItemStack... contents) {
		if (holder == null || holder.isEmpty() || size == null || title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Holder, size, or title is invalid!");
		}
		Player player = Bukkit.getPlayerExact(holder);
		if (player == null) {
			throw new IllegalStateException("Trying to create a backpack for an offline player!");
		}
		inventory = Bukkit.createInventory(player, size.getValue(), title);
		if (contents != null) {
			inventory.setContents(contents);
		}
	}

	/**
	 * Gets the holder ({@link Player}).
	 * @return The holder of the backpack
	 */
	public Player getHolder() {
		return (Player) inventory.getHolder();
	}

	/**
	 * Sets the holder ({@link Player}).
	 * @param holder The new holder
	 * @return This object, for chaining
	 */
	public Backpack setHolder(Player holder) {
		if (!inventory.getHolder().equals(holder)) {

			final Inventory inventory = Bukkit.createInventory(holder, this.inventory.getSize(), this.inventory.getTitle());
			inventory.setContents(this.inventory.getContents());
			this.inventory = inventory;
			setDirty(true);
		}
		return this;
	}

	/**
	 * Returns the wrapped {@link Inventory} which are the raw backpack contents.
	 * <p/>
	 * Guaranteed to never be null.
	 * @return Wrapped Inventory object
	 */
	public ItemStack[] getContents() {
		return inventory.getContents();
	}

	/**
	 * Sets the contents of the internal {@link Inventory}.
	 * @param contents The new contents
	 * @return This object, for chaining
	 */
	public Backpack setContents(final ItemStack... contents) {
		inventory.setContents(contents);
		setDirty(true);
		return this;
	}

	/**
	 * Gets the title this Backpack will show to the client's screen.
	 * @return The string representing the title
	 */
	public String getTitle() {
		return inventory.getTitle();
	}

	/**
	 * Sets the title this Backpack will show to the client's screen.
	 * @param title The new title
	 * @return This object, for chaining
	 */
	public Backpack setTitle(String title) {
		if (!inventory.getTitle().equals(title)) {
			final Inventory inventory = Bukkit.createInventory(getHolder(), getSize().getValue(), title);
			inventory.setContents(getContents());
			this.inventory = inventory;
			setDirty(true);
		}
		return this;
	}

	/**
	 * Gets the size of the internal {@link Inventory}.
	 * @return
	 */
	public Size getSize() {
		return Size.get(inventory.getSize());
	}

	/**
	 * Sets the size of the inner {@link Inventory}.
	 * @param size The new size of the backpack
	 * @return This object, for chaining
	 */
	public Backpack setSize(final Size size) {
		//Trying to resize a backpack with the same current size, don't do extra work.
		if (!size.equals(getSize())) {
			final Inventory inventory = Bukkit.createInventory(getHolder(), size.getValue(), getTitle());
			inventory.setContents(getContents());
			this.inventory = inventory;
			setDirty(true);
		}
		return this;
	}

	/**
	 * Filters out all illegal {@link ItemStack}s within the {@link Inventory}.
	 * <p/>
	 * Returns a collection of the illegal item stacks found.
	 * @param blacklisted Collection of names which correspond to {@link Material} names
	 * @return A collection of the illegal item stacks found
	 */
	public ItemStack[] filterIllegalItems(Set<String> blacklisted) {
		final List<ItemStack> contentsList = Arrays.asList(getContents());
		final List<ItemStack> illegalContents = new ArrayList<>();
		while (contentsList.iterator().hasNext()) {
			final ItemStack content = contentsList.iterator().next();
			if (blacklisted.contains(content.getType().name().toLowerCase())) {
				illegalContents.add(content);
				contentsList.iterator().remove();
				dirty = true;
			}
		}
		setContents(contentsList.toArray(new ItemStack[contentsList.size()]));
		return illegalContents.toArray(new ItemStack[illegalContents.size()]);
	}

	/**
	 * Returns if this object is marked as dirty and needs to be persisted.
	 * @return True if dirty, false if not
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the dirty state of this object. INTERNAL USE ONLY.
	 * @param dirty Dirty state
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Backpack)) {
			return false;
		}

		final Backpack backpack = (Backpack) other;
		return getHolder().equals(backpack.getHolder()) && getContents().equals(backpack.getContents());
	}

	@Override
	public String toString() {
		return "Backpack{" + inventory.toString() + ", dirty= " + dirty + "}";
	}
}