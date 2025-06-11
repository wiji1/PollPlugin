package dev.wiji.features.poll.models;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Poll {
	private final UUID uuid;
	private final TextComponent question;

	private final PollResponse[] responses;

	private Map<UUID, UUID> playerResponses;

	public Poll(TextComponent question, PollResponse[] responses) {
		this(UUID.randomUUID(), question, responses);
	}

	public Poll(UUID uuid, TextComponent question, PollResponse[] responses) {
		this.uuid = uuid;
		this.question = question;
		this.responses = responses;

		this.playerResponses = new HashMap<>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public TextComponent getQuestion() {
		return question;
	}

	public PollResponse getResponse(UUID responseUuid) {
		for (PollResponse response : responses) {
			if (response.getUuid().equals(responseUuid)) return response;
		}
		return null;
	}

	public PollResponse[] getResponses() {
		return responses;
	}

	public Map<UUID, UUID> getPlayerResponses() {
		return playerResponses;
	}

	public void respond(Player player, UUID responseUuid) {
		if (playerResponses.containsKey(player.getUniqueId())) {
			throw new IllegalStateException("Player has already responded to this poll.");
		}

		PollResponse response = getResponse(responseUuid);

		if (response == null) {
			throw new IllegalArgumentException("Response UUID does not match any available responses.");
		}

		playerResponses.put(player.getUniqueId(), responseUuid);
	}


}
