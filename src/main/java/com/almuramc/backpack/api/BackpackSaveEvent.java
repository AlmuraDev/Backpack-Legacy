package com.almuramc.backpack.api;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Custom event thrown when a player closes their backpack.
 */
public class BackpackSaveEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled = false;
	private final Player player;
	private final World world;
	private final Inventory backpack;

	public BackpackSaveEvent(Player player, World world, Inventory backpack) {
		this.player = player;
		this.world = world;
		this.backpack = backpack;
	}

	public final Player getPlayer() {
		return player;
	}

	public final World getWorld() {
		return world;
	}

	/**
	 * Returns the saved backpack after the player has closed it.
	 * @return
	 */
	public final Inventory getBackpack() {
		return backpack;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		isCancelled = b;
	}
}
