/*
 * This file is part of Backpack.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuramc.com/>
 * Backpack is licensed under the Almura Development License version 1.
 *
 * Backpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an exception, all classes which reference GPL licensed code are hereby
 * licensed under the GNU General Public License, as described in the
 * Almura Development License version 1.
 *
 * Backpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the GNU General Public License (for classes that fulfill the exception) and the
 * Almura Development License version 1 along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU Lesser General Public License and
 * the GNU General Public License.
 */
package com.almuramc.backpack.bukkit.util.exception;

public class InvalidDependencyException extends Exception {
	public InvalidDependencyException() {
		super("Invalid dependency encountered!");
	}

	public InvalidDependencyException(String message) {
		super(message);
	}
}
