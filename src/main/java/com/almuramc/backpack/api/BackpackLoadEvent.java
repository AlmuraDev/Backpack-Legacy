package com.almuramc.backpack.api;

import com.almuramc.backpack.Backpack;
import com.almuramc.backpack.BackpackHandler;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Custom event thrown when a backpack is opened. Modifications can be done
 * to the player's backpack.
 */
public class BackpackLoadEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final BackpackHandler backpackHandler;
	private boolean isCancelled;

	private final Player player;
	private final World world;
	private final Inventory inventory;

	public BackpackLoadEvent(Player player, World world) {
		backpackHandler = Backpack.getInstance().getHandler();
		this.player = player;
		this.world = world;
		inventory = backpackHandler.getBackpackFor(player, world);
	}

	/**
	 * Returns the player that opened their backpack.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the inventory of the player's backpack (for manipulation).
	 * @return
	 */
	public Inventory getBackpack() {
		return inventory;
	}

	/**
	 * Sets the inventory of the player's backpack. Note that the inventory object
	 * is not changed, this merely copies the contents of the inventory argument to the
	 * event's inventory.
	 * @param inventory
	 */
	public void setBackpack(Inventory inventory) {
		//Clear the current backpack
		this.inventory.clear();
		//Inject the inventory argument into this inventory.
		this.inventory.setContents(inventory.getContents());
	}

	/**
	 * Returns if the player has a backpack or not.
	 * @return
	 */
	public boolean hasBackpack() {
		return inventory != null;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		isCancelled = b;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
