/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuramc.backpack.bukkit.util;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.input.BackpackInputHandler;
import com.almuramc.backpack.bukkit.input.PanelInputHandler;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;

/**
 * Safely handles registering SpoutPlugin's bindings when SpoutPlugin may not be existent.
 */
public class SpoutSafeBindings {
	public static void registerSpoutBindings() {
		SpoutManager.getKeyBindingManager().registerBinding("Backpack", Keyboard.valueOf(BackpackPlugin.getInstance().getCached().getBackpackHotkey()), "Opens the backpack", new BackpackInputHandler(), BackpackPlugin.getInstance());
		SpoutManager.getKeyBindingManager().registerBinding("Backpack Panel", Keyboard.valueOf(BackpackPlugin.getInstance().getCached().getPanelHotkey()), "Opens Backpack Panel", new PanelInputHandler(), BackpackPlugin.getInstance());
		BackpackPlugin.getInstance().getLogger().info("Successfully hooked into SpoutPlugin for keybindings");
	}
}
