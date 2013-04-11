package com.almuradev.backpack.delegate;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import org.bukkit.entity.Player;

public final class BackpackDelegate implements BindingExecutionDelegate {
	@Override
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
		final Player player = keyBindingEvent.getPlayer();
	}

	@Override
	public void keyReleased(KeyBindingEvent keyBindingEvent) {

	}
}
