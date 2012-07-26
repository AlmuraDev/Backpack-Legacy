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
package com.almuramc.backpack.bukkit.gui;

import java.util.logging.Logger;

import com.almuramc.backpack.bukkit.BackpackPlugin;
import com.almuramc.backpack.bukkit.inventory.BackpackInventory;
import com.almuramc.backpack.bukkit.storage.Storage;
import com.almuramc.backpack.bukkit.util.CachedConfiguration;
import com.almuramc.backpack.bukkit.util.MessageHelper;
import com.almuramc.backpack.bukkit.util.PermissionHelper;
import com.almuramc.backpack.bukkit.util.SafeSpout;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Material;

public class UpgradePanel extends GenericPopup {
	final GenericButton close;
	GenericButton buy18;
	GenericButton buy27;
	GenericButton buy36;
	GenericButton buy45;
	GenericButton buy54;
	private final SpoutPlayer player;
	public final Logger log = Logger.getLogger("Minecraft");
	private static final Storage STORE = BackpackPlugin.getInstance().getStore();
	private static final CachedConfiguration CONFIG = BackpackPlugin.getInstance().getCached();
	private static final Economy ECON = BackpackPlugin.getInstance().getHooks().getEconomy();
	private static final Permission PERM = BackpackPlugin.getInstance().getHooks().getPermissions();
	double cost18 = CONFIG.getUpgradeCosts().get("slot" + 18);
	double cost27 = CONFIG.getUpgradeCosts().get("slot" + 18) + CONFIG.getUpgradeCosts().get("slot" + 27);
	double cost36 = CONFIG.getUpgradeCosts().get("slot" + 18) + CONFIG.getUpgradeCosts().get("slot" + 27) + CONFIG.getUpgradeCosts().get("slot" + 36);
	double cost45 = CONFIG.getUpgradeCosts().get("slot" + 18) + CONFIG.getUpgradeCosts().get("slot" + 27) + CONFIG.getUpgradeCosts().get("slot" + 36) + CONFIG.getUpgradeCosts().get("slot" + 45);
	double cost54 = CONFIG.getUpgradeCosts().get("slot" + 18) + CONFIG.getUpgradeCosts().get("slot" + 27) + CONFIG.getUpgradeCosts().get("slot" + 36) + CONFIG.getUpgradeCosts().get("slot" + 45) + CONFIG.getUpgradeCosts().get("slot" + 54);

	public UpgradePanel(SpoutPlayer player) {
		super();
		this.player = player;
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		int curSize = backpack.getSize();
		buy18 = null;
		buy27 = null;
		buy36 = null;
		buy45 = null;
		buy54 = null;

		// Screen Title
		GenericLabel label = new GenericLabel();
		label.setText("Backpack Upgrade").setShadow(true).setScale((float) 1.25);
		label.setAnchor(WidgetAnchor.CENTER_CENTER);
		label.shiftXPos(-50).shiftYPos(-118);
		label.setPriority(RenderPriority.Lowest);
		label.setWidth(-1).setHeight(-1);

		// Current Size Text
		GenericLabel label1 = new GenericLabel();
		label1.setText("Current Size: ");
		label1.setAnchor(WidgetAnchor.CENTER_CENTER);
		label1.shiftXPos(-40).shiftYPos(-100);
		label1.setPriority(RenderPriority.Lowest);
		label1.setWidth(-1).setHeight(-1);

		// Current Backpack Size
		GenericLabel CurrentSize = new GenericLabel();
		CurrentSize.setText(Integer.toString(curSize)).setShadow(true).setTextColor(new Color(1.0F, 0.3F, 0.15F, 1.0F));
		;
		CurrentSize.setAnchor(WidgetAnchor.CENTER_CENTER);
		CurrentSize.shiftXPos(30).shiftYPos(-100);
		CurrentSize.setPriority(RenderPriority.Lowest);
		CurrentSize.setWidth(5).setHeight(18);

		// Seperator Bar
		GenericGradient spaceBar1 = new GenericGradient();
		Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);
		spaceBar1.setBottomColor(bottom);
		spaceBar1.setTopColor(bottom);
		spaceBar1.setAnchor(WidgetAnchor.CENTER_CENTER);
		spaceBar1.shiftXPos(-100).shiftYPos(-85);
		spaceBar1.setWidth(200);
		spaceBar1.setHeight(1);

		attachWidgets(BackpackPlugin.getInstance(), label, label1, spaceBar1, CurrentSize);

		int nextLoc = -70;
		int xLoc = -85;

		if (curSize <= 9 && PermissionHelper.getMaxSizeFor(player) >= 18) {
			GenericLabel Size18 = new GenericLabel();
			Size18.setText("[18] Slots for : " + cost18);
			Size18.setAnchor(WidgetAnchor.CENTER_CENTER);
			Size18.shiftXPos(xLoc).shiftYPos(nextLoc);
			Size18.setPriority(RenderPriority.Lowest);
			Size18.setWidth(5).setHeight(18);

			buy18 = new Buy18Button(this);
			buy18.setAuto(true);
			buy18.setAnchor(WidgetAnchor.CENTER_CENTER);
			buy18.setHeight(18).setWidth(40);
			buy18.shiftXPos(80).shiftYPos(nextLoc - 5);
			nextLoc = nextLoc + 20;
			attachWidgets(BackpackPlugin.getInstance(), buy18, Size18);
		}

		if (curSize <= 18 && PermissionHelper.getMaxSizeFor(player) >= 27) {
			GenericLabel Size27 = new GenericLabel();
			Size27.setText("[27] Slots for : " + cost27);
			Size27.setAnchor(WidgetAnchor.CENTER_CENTER);
			Size27.shiftXPos(xLoc).shiftYPos(nextLoc);
			Size27.setPriority(RenderPriority.Lowest);
			Size27.setWidth(5).setHeight(18);

			buy27 = new Buy27Button(this);
			buy27.setAuto(true);
			buy27.setAnchor(WidgetAnchor.CENTER_CENTER);
			buy27.setHeight(18).setWidth(40);
			buy27.shiftXPos(80).shiftYPos(nextLoc - 5);
			nextLoc = nextLoc + 20;
			attachWidgets(BackpackPlugin.getInstance(), buy27, Size27);
		}

		if (curSize <= 27 && PermissionHelper.getMaxSizeFor(player) >= 36) {
			GenericLabel Size36 = new GenericLabel();
			Size36.setText("[36] Slots for : " + cost36);
			Size36.setAnchor(WidgetAnchor.CENTER_CENTER);
			Size36.shiftXPos(xLoc).shiftYPos(nextLoc);
			Size36.setPriority(RenderPriority.Lowest);
			Size36.setWidth(5).setHeight(18);

			buy36 = new Buy36Button(this);
			buy36.setAuto(true);
			buy36.setAnchor(WidgetAnchor.CENTER_CENTER);
			buy36.setHeight(18).setWidth(40);
			buy36.shiftXPos(80).shiftYPos(nextLoc - 5);
			nextLoc = nextLoc + 20;
			attachWidgets(BackpackPlugin.getInstance(), buy36, Size36);
		}

		if (curSize <= 36 && PermissionHelper.getMaxSizeFor(player) >= 45) {
			GenericLabel Size45 = new GenericLabel();
			Size45.setText("[45] Slots for : " + cost45);
			Size45.setAnchor(WidgetAnchor.CENTER_CENTER);
			Size45.shiftXPos(xLoc).shiftYPos(nextLoc);
			Size45.setPriority(RenderPriority.Lowest);
			Size45.setWidth(5).setHeight(18);

			buy45 = new Buy45Button(this);
			buy45.setAuto(true);
			buy45.setAnchor(WidgetAnchor.CENTER_CENTER);
			buy45.setHeight(18).setWidth(40);
			buy45.shiftXPos(80).shiftYPos(nextLoc - 5);
			nextLoc = nextLoc + 20;
			attachWidgets(BackpackPlugin.getInstance(), buy45, Size45);
		}

		if (curSize <= 45 && PermissionHelper.getMaxSizeFor(player) >= 54) {
			GenericLabel Size54 = new GenericLabel();
			Size54.setText("[54] Slots for : " + cost54);
			Size54.setAnchor(WidgetAnchor.CENTER_CENTER);
			Size54.shiftXPos(xLoc).shiftYPos(nextLoc);
			Size54.setPriority(RenderPriority.Lowest);
			Size54.setWidth(5).setHeight(18);

			buy54 = new Buy54Button(this);
			buy54.setAuto(true);
			buy54.setAnchor(WidgetAnchor.CENTER_CENTER);
			buy54.setHeight(18).setWidth(40);
			buy54.shiftXPos(80).shiftYPos(nextLoc - 5);
			attachWidgets(BackpackPlugin.getInstance(), buy54, Size54);
		}

		// No Upgrades Available
		if (nextLoc == -70) {
			GenericLabel notAvailable = new GenericLabel();
			notAvailable.setText("You already have your Max Size!");
			notAvailable.setAnchor(WidgetAnchor.CENTER_CENTER);
			notAvailable.shiftXPos(xLoc + 10).shiftYPos(nextLoc);
			notAvailable.setPriority(RenderPriority.Lowest);
			notAvailable.setWidth(5).setHeight(18);
			attachWidgets(BackpackPlugin.getInstance(), notAvailable);
		}

		// Seperator Bar
		GenericGradient spaceBar2 = new GenericGradient();
		spaceBar2.setBottomColor(bottom);
		spaceBar2.setTopColor(bottom);
		spaceBar2.setAnchor(WidgetAnchor.CENTER_CENTER);
		spaceBar2.shiftXPos(-100).shiftYPos(nextLoc + 19);
		spaceBar2.setWidth(200);
		spaceBar2.setHeight(1);
		attachWidgets(BackpackPlugin.getInstance(), spaceBar2);

		nextLoc = nextLoc + 25;

		// Close Button
		close = new CloseButton(this);
		close.setAuto(true);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(18).setWidth(40);
		close.shiftXPos(80).shiftYPos(nextLoc + 2);

		// Screen Layout
		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/backpackgui.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(270).setHeight(nextLoc + 160);
		border.shiftXPos(-130).shiftYPos(-128);

		// Attach Widgets to MainScreen.
		attachWidget(BackpackPlugin.getInstance(), border);
		attachWidget(BackpackPlugin.getInstance(), close);
	}

	void onCloseClick() {
		Screen screen = ((SpoutPlayer) player).getMainScreen();
		screen.removeWidget(this);
		//player.getMainScreen().closePopup();
		player.closeActiveWindow();
	}

	void onBuy18Click() {
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
			if (!ECON.has(player.getName(), cost18)) {
				SafeSpout.sendMessage(player, "Not enough money!", "Backpack", Material.BONE);
				return;
			}
			ECON.withdrawPlayer(player.getName(), cost18);
			MessageHelper.sendMessage(player, "Your account has been deducted by: " + cost18);
		}
		backpack.setSize(player, 18);
		STORE.save(player, player.getWorld(), backpack);
		SafeSpout.sendMessage(player, "Upgraded to 18 slots", "Backpack", Material.CHEST);
		onCloseClick();
	}

	void onBuy27Click() {
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
			if (!ECON.has(player.getName(), cost27)) {
				SafeSpout.sendMessage(player, "Not enough money!", "Backpack", Material.BONE);
				return;
			}
			ECON.withdrawPlayer(player.getName(), cost27);
			MessageHelper.sendMessage(player, "Your account has been deducted by: " + cost27);
		}
		backpack.setSize(player, 27);
		STORE.save(player, player.getWorld(), backpack);
		SafeSpout.sendMessage(player, "Upgraded to 27 slots", "Backpack", Material.CHEST);
		onCloseClick();
	}

	void onBuy36Click() {
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
			if (!ECON.has(player.getName(), cost36)) {
				SafeSpout.sendMessage(player, "Not enough money!", "Backpack", Material.BONE);
				return;
			}
			ECON.withdrawPlayer(player.getName(), cost36);
			MessageHelper.sendMessage(player, "Your account has been deducted by: " + cost36);
		}
		backpack.setSize(player, 36);
		STORE.save(player, player.getWorld(), backpack);
		SafeSpout.sendMessage(player, "Upgraded to 36 slots", "Backpack", Material.CHEST);
		onCloseClick();
	}

	void onBuy45Click() {
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
			if (!ECON.has(player.getName(), cost45)) {
				SafeSpout.sendMessage(player, "Not enough money!", "Backpack", Material.BONE);
				return;
			}
			ECON.withdrawPlayer(player.getName(), cost45);
			MessageHelper.sendMessage(player, "Your account has been deducted by: " + cost45);
		}
		backpack.setSize(player, 45);
		STORE.save(player, player.getWorld(), backpack);
		SafeSpout.sendMessage(player, "Upgraded to 45 slots", "Backpack", Material.CHEST);
		onCloseClick();
	}

	void onBuy54Click() {
		BackpackInventory backpack = STORE.load(player, player.getWorld());
		if (CONFIG.useEconomy() && !PERM.has(player.getWorld().getName(), player.getName(), "backpack.noupgradecost")) {
			if (!ECON.has(player.getName(), cost54)) {
				SafeSpout.sendMessage(player, "Not enough money!", "Backpack", Material.BONE);
				return;
			}
			ECON.withdrawPlayer(player.getName(), cost54);
			MessageHelper.sendMessage(player, "Your account has been deducted by: " + cost54);
		}
		backpack.setSize(player, 54);
		STORE.save(player, player.getWorld(), backpack);
		SafeSpout.sendMessage(player, "Upgraded to 54 slots", "Backpack", Material.CHEST);
		onCloseClick();
	}
}
