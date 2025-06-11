package dev.wiji.features.poll.models;


import net.kyori.adventure.text.TextComponent;

import java.util.UUID;

public class PollResponse {
	private final UUID uuid;
	private final TextComponent text;

	public PollResponse(TextComponent text) {
		this(UUID.randomUUID(), text);
	}

	public PollResponse(UUID uuid, TextComponent text) {
		this.uuid = uuid;
		this.text = text;
	}

	public UUID getUuid() {
		return uuid;
	}

	public TextComponent getText() {
		return text;
	}
}
