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
package com.almuradev.backpack.util;

public enum Permissions {
	ADMIN("backpack.admin"),
	OPEN("backpack.open"),
	VIEW("backpack.view"),
	CREATE("backpack.create"),
	CREATE_OTHER("backpack.create.other"),
	SMALL_UPGRADE("backpack.upgrade.small"),
	MEDIUM_SMALL_UPGRADE("backpack.upgrade.mediumsmall"),
	MEDIUM_UPGRADE("backpack.upgrade.medium"),
	MEDIUM_LARGE_UPGRADE("backpack.upgrade.mediumlarge"),
	LARGE_UPGRADE("backpack.upgrade.large"),
	EXTRA_LARGE_UPGRADE("backpack.upgrade.extralarge");
	private final String value;

	private Permissions(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static Permissions get(String name) {
		return valueOf(name);
	}
}
