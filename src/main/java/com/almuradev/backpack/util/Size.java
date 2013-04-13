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

public enum Size {
	SMALL(9),
	MEDIUM_SMALL(18),
	MEDIUM(27),
	MEDIUM_LARGE(36),
	LARGE(45),
	EXTRA_LARGE(54);
	private final int value;

	private Size(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Size get(String name) {
		return valueOf(name.toUpperCase());
	}

	public static Size get(int num) {
		switch (num) {
			case 9:
				return SMALL;
			case 18:
				return MEDIUM_SMALL;
			case 27:
				return MEDIUM;
			case 36:
				return MEDIUM_LARGE;
			case 45:
				return LARGE;
			case 54:
				return EXTRA_LARGE;
			default:
				throw new IllegalArgumentException("The value: " + num + " is not valid for a Size!");
		}
	}
}
