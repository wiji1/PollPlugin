package dev.wiji.features.inventory.enums;

import org.bukkit.NamespacedKey;

public enum ItemKey {
	POLL_UUID,
	POLL_RESPONSE_UUID,
	NEXT_PAGE,
	PREVIOUS_PAGE,
	BACK,
	SELECTED_BLOCK,
	CREATE_POLL_QUESTION,
	CREATE_POLL_RESPONSE,
	CREATE_POLL_DURATION,
	CREATE_POLL_CONFIRM,
	;

	public NamespacedKey get() {
		return new NamespacedKey("pollplugin", this.name().toLowerCase());
	}
}

