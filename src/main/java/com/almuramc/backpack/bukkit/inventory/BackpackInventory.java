package com.almuramc.backpack.bukkit.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BackpackInventory implements Inventory {
	private Inventory inventory;

	public BackpackInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public int getSize() {
		return inventory.getSize();
	}

	@Override
	public int getMaxStackSize() {
		return inventory.getMaxStackSize();
	}

	@Override
	public void setMaxStackSize(int i) {
		inventory.setMaxStackSize(i);
	}

	@Override
	public String getName() {
		return "BackpackInventory";
	}

	@Override
	public ItemStack getItem(int i) {
		return inventory.getItem(i);
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		inventory.setItem(i, itemStack);
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
		return inventory.addItem(itemStacks);
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) {
		return inventory.removeItem(itemStacks);
	}

	@Override
	public ItemStack[] getContents() {
		return inventory.getContents();
	}

	@Override
	public void setContents(ItemStack[] itemStacks) {
		inventory.setContents(itemStacks);
	}

	@Override
	public boolean contains(int i) {
		return inventory.contains(i);
	}

	@Override
	public boolean contains(Material material) {
		return inventory.contains(material);
	}

	@Override
	public boolean contains(ItemStack itemStack) {
		return inventory.contains(itemStack);
	}

	@Override
	public boolean contains(int i, int i1) {
		return inventory.contains(i, i1);
	}

	@Override
	public boolean contains(Material material, int i) {
		return inventory.contains(material, i);
	}

	@Override
	public boolean contains(ItemStack itemStack, int i) {
		return inventory.contains(itemStack, i);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int i) {
		return inventory.all(i);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) {
		return inventory.all(material);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
		return inventory.all(itemStack);
	}

	@Override
	public int first(int i) {
		return inventory.first(i);
	}

	@Override
	public int first(Material material) {
		return inventory.first(material);
	}

	@Override
	public int first(ItemStack itemStack) {
		return inventory.first(itemStack);
	}

	@Override
	public int firstEmpty() {
		return inventory.firstEmpty();
	}

	@Override
	public void remove(int i) {
		inventory.remove(i);
	}

	@Override
	public void remove(Material material) {
		inventory.remove(material);
	}

	@Override
	public void remove(ItemStack itemStack) {
		inventory.remove(itemStack);
	}

	@Override
	public void clear(int i) {
		inventory.clear(i);
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public List<HumanEntity> getViewers() {
		return inventory.getViewers();
	}

	@Override
	public String getTitle() {
		return "Backpack";
	}

	@Override
	public InventoryType getType() {
		return inventory.getType();
	}

	@Override
	public InventoryHolder getHolder() {
		return inventory.getHolder();
	}

	@Override
	public ListIterator<ItemStack> iterator() {
		return inventory.iterator();
	}

	@Override
	public ListIterator<ItemStack> iterator(int i) {
		return inventory.iterator(i);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BackpackInventory)) {
			return false;
		}
		if (((BackpackInventory) obj).getInventory() == null && inventory == null) {
			return true;
		}
		if (inventory == null) {
			return false;
		}
		if (getSize() != ((BackpackInventory) obj).getSize()) {
			return false;
		}
		ItemStack[] contents = inventory.getContents();
		ItemStack[] otherContents = ((BackpackInventory) obj).getContents();
		for (int i = 0; i < otherContents.length; i++) {
			if (i > contents.length) {
				return false;
			}
			if (!contents[i].equals(otherContents[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return inventory.toString();
	}

	public void setSize(Player player, int newSize) {
		if (getSize() == newSize || newSize > 54) {
			return;
		}
		int size = newSize;
		if (!isValidSize(size)) {
			size = getNextMultiple(size, 9);
		}
		Inventory newInventory = Bukkit.createInventory(player, size, getTitle());
		newInventory.setContents(Arrays.copyOf(inventory.getContents(), size));
		inventory = newInventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public boolean hasValidSize() {
		return isValidSize(getSize());
	}

	public boolean hasVisibleContents() {
		boolean hasContents = false;
		for (ItemStack is : inventory.getContents()) {
			if (!is.equals(Material.AIR)) {
				hasContents = true;
				break;
			}
		}
		return hasContents;
	}

	public ItemStack[] getVisibleContents() {
		ArrayList<ItemStack> visibleContents = new ArrayList<ItemStack>();
		for (ItemStack is : inventory.getContents()) {
			if (is.equals(Material.AIR)) {
				continue;
			}
			visibleContents.add(is);
		}
		return visibleContents.toArray(new ItemStack[visibleContents.size()]);
	}

	/**
	 * Backpack-specific static helpers
	 */
	public static boolean isValidSize(int size) {
		if (size > 54 || size < 9 || size % 9 != 0) {
			return false;
		}

		return true;
	}

	public static int getNextMultiple(int num, int multiple) {
		return ((num / multiple + 1)) * multiple;
	}
}
