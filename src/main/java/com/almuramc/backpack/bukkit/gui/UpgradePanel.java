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

import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class UpgradePanel extends GenericPopup {
	final GenericButton close;
	private final SpoutPlayer player;
	public final Logger log = Logger.getLogger("Minecraft");

	public UpgradePanel(SpoutPlayer player) {
		super();
		this.player = player;

		// Screen Layout
		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/backpack.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(626).setHeight(240);
		border.shiftXPos(-220).shiftYPos(-128);

		// Screen Title
		GenericLabel label = new GenericLabel();
		label.setText("Backpack Upgrade");
		label.setAnchor(WidgetAnchor.CENTER_CENTER);
		label.shiftXPos(-35).shiftYPos(-122);
		label.setPriority(RenderPriority.Lowest);
		label.setWidth(-1).setHeight(-1);

		// Close Button
		close = new CloseButton(this);
		close.setAuto(true);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(18).setWidth(40);
		close.shiftXPos(142).shiftYPos(87);

		// Attach Widgets to MainScreen.
		attachWidget(BackpackPlugin.getInstance(), label);
		attachWidget(BackpackPlugin.getInstance(), border);
		attachWidget(BackpackPlugin.getInstance(), close);
	}

	void onCloseClick() {
		Screen screen = ((SpoutPlayer) player).getMainScreen();
		screen.removeWidget(this);
		//player.getMainScreen().closePopup();
		player.closeActiveWindow();
	}
}
