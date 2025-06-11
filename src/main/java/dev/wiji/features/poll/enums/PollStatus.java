package dev.wiji.features.poll.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public enum PollStatus {
	ACTIVE(Component.text("Active", NamedTextColor.GREEN)),
	CLOSED(Component.text("Closed", NamedTextColor.RED)),
	;


	private final TextComponent displayName;

	PollStatus(TextComponent displayName) {
		this.displayName = displayName;
	}

	public TextComponent getDisplayName() {
		return displayName;
	}
}
