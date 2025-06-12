package dev.wiji.features.command.enums;

public enum Permission {
	ADMIN("poll.admin"),
	VIEW_RESULTS("poll.results"),
	;

	private final String permission;

	Permission(String permission) {
		this.permission = permission;
	}

	public String get() {
		return permission;
	}
}
