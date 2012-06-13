package com.almuramc.backpack.api;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Custom event thrown when a backpack is opened.
 */
public class BackpackLoadEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled;
	private final Player player;
	private final World world;
	private Inventory backpack;

	public BackpackLoadEvent(Player player, World world, Inventory backpack) {
		this.player = player;
		this.world = world;
		this.backpack = backpack;
	}

	/**
	 * Returns the player that opened their backpack.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the world in-which the player opened their backpack;
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Returns the inventory of the player's backpack (for manipulation).
	 * @return
	 */
	public Inventory getBackpack() {
		return backpack;
	}

	/**
	 * Sets the inventory of the player's backpack. Setting this to null will erase the player's backpack
	 * entry and persistent data.
	 * @param backpack
	 */
	public void setBackpack(Inventory backpack) {
		this.backpack = backpack;
	}

	/**
	 * Returns if the player has a backpack or not.
	 * @return
	 */
	public boolean hasBackpack() {
		return backpack != null;
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
