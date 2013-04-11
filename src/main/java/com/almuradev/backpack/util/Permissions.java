package com.almuradev.backpack.util;

public enum Permissions {
	ADMIN("backpack.admin"),
	OPEN("backpack.open"),
	VIEW("backpack.view"),
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
