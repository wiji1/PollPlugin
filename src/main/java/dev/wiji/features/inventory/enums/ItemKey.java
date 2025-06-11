package dev.wiji.features.inventory.enums;

import org.bukkit.NamespacedKey;

public enum ItemKey {
	POLL_UUID,
	POLL_RESPONSE_UUID,
	NEXT_PAGE,
	PREVIOUS_PAGE,
	BACK,
	;

	public NamespacedKey get() {
		return new NamespacedKey("pollplugin", this.name().toLowerCase());
	}
}

