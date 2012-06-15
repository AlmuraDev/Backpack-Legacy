package com.almuramc.backpack.bukkit.util.exception;

public class InvalidDependencyException extends Exception {
	public InvalidDependencyException() {
		super("Invalid dependency encountered!");
	}

	public InvalidDependencyException(String message) {
		super(message);
	}
}
