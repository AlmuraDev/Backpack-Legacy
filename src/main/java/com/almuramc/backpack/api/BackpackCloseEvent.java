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
 * Custom event thrown when a player closes their backpack. No modifications can be done
 * to prevent duping (event is called after save).
 */
public class BackpackCloseEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final BackpackHandler backpackHandler;
	private boolean isCancelled = false;

	private final Player player;
	private final World world;
	private final Inventory inventory;

	public BackpackCloseEvent(Player player) {
		backpackHandler = Backpack.getInstance().getBackpackHandler();
		this.player = player;
		world = player.getWorld();
		//Grab the manipulated backpack left from the player (or dev in BackpackOpenEvent)
		inventory = backpackHandler.getBackpackFor(player, world);
	}

	/**
	 * Returns the saved backpack after the player has closed it.
	 * @return
	 */
	public Inventory getBackpack() {
		return inventory;
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
