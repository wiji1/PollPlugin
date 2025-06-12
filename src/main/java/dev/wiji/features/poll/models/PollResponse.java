package dev.wiji.features.poll.models;


import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;

import java.util.UUID;

public class PollResponse {
	private final UUID uuid;
	private final TextComponent text;
	private final Material icon;

	public PollResponse(TextComponent text, Material icon) {
		this(UUID.randomUUID(), text, icon);
	}

	public PollResponse(UUID uuid, TextComponent text, Material icon) {
		this.uuid = uuid;
		this.text = text;
		this.icon = icon;
	}

	public UUID getUuid() {
		return uuid;
	}

	public TextComponent getText() {
		return text;
	}

	public Material getIcon() {
		return icon;
	}
}
