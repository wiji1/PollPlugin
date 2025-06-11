package dev.wiji.features.command.enums;

public enum Permission {
	ADMIN("poll.admin"),
	;

	private final String permission;

	Permission(String permission) {
		this.permission = permission;
	}

	public String get() {
		return permission;
	}
}
