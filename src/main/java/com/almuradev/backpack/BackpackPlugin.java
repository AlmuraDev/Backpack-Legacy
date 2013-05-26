/*
 * This file is part of Backpack.
 *
 * © 2012-2013 AlmuraDev <http://www.almuradev.com/>
 * Backpack is licensed under the GNU General Public License.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which do not reference GPL licensed code
 * are hereby licensed under the GNU Lesser Public License, as described
 * in GNU General Public License.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * the GNU Lesser Public License (for classes that fulfill the exception)
 * and the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU General Public License and
 * the GNU Lesser Public License.
 */
package com.almuradev.backpack;

import com.almuradev.backpack.backend.Backend;
import com.almuradev.backpack.backend.Backpacks;
import com.almuradev.backpack.delegate.BackpackDelegate;
import com.almuradev.backpack.task.SaveTask;
import com.almuradev.backpack.util.SpoutUtil;

import org.getspout.spoutapi.keyboard.Keyboard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class BackpackPlugin extends JavaPlugin {
	private Backend backend;
	private Backpacks storage;
	private int save_task_id;

	@Override
	public void onEnable() {
		backend = new Backend(this);
		backend.onEnable();
		storage = new Backpacks();
		backend.load();
		if (SpoutUtil.isSpoutEnabled()) {
			SpoutUtil.bind("Open Backpack", Keyboard.KEY_B, "Opens the backpack", new BackpackDelegate(this), this);
		}
		save_task_id = getServer().getScheduler().scheduleSyncRepeatingTask(this, new SaveTask(this), 100, 100);
		getServer().getPluginManager().registerEvents(new BackpackListener(this), this);
	}

	@Override
	public void onDisable() {
		final BukkitScheduler scheduler = getServer().getScheduler();
		if (!scheduler.isCurrentlyRunning(save_task_id)) {
			scheduler.runTask(this, new SaveTask(this));
		}
		scheduler.cancelTasks(this);
	}

	public Backend getBackend() {
		return backend;
	}

	public Backpacks getStorage() {
		return storage;
	}
}
