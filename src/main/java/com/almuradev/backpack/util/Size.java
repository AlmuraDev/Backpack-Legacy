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
