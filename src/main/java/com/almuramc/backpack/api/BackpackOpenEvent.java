package com.almuramc.backpack.api;

import com.almuramc.backpack.Backpack;
import com.almuramc.backpack.core.BackpackHandler;

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
public class BackpackOpenEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final BackpackHandler backpackHandler;
	private boolean isCancelled;

	private final Player player;
	private final World world;
	private final Inventory inventory;

	public BackpackOpenEvent(Player player) {
		backpackHandler = Backpack.getInstance().getBackpackHandler();
		this.player = player;
		world = player.getWorld();
		//Grab the prior backpack from the last time backpacks were accessed.
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
