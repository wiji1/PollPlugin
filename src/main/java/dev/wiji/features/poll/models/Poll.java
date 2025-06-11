package dev.wiji.features.poll.models;

import dev.wiji.features.poll.controllers.PollIdManager;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.enums.PollStatus;
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

	private final Long creationTimestamp;
	private Long duration;
	private boolean isClosed = false;

	public Poll(TextComponent question, PollResponse[] responses, Long duration) {
		this(UUID.randomUUID(), question, responses, duration);
	}

	public Poll(UUID uuid, TextComponent question, PollResponse[] responses, Long duration) {
		this.uuid = uuid;
		this.question = question;
		this.responses = responses;

		this.playerResponses = new HashMap<>();
		this.creationTimestamp = System.currentTimeMillis();
		this.duration = duration;

		PollIdManager.getInstance().registerPoll(this);
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getSequentialId() {
		Integer id = PollIdManager.getInstance().getPollId(this);
		return id != null ? id : -1;
	}

	public TextComponent getQuestion() {
		return question;
	}

	public PollResponse getResponse(UUID responseUuid) {
		for(PollResponse response : responses) {
			if(response.getUuid().equals(responseUuid)) return response;
		}
		return null;
	}

	public PollResponse[] getResponses() {
		return responses;
	}

	public Map<UUID, UUID> getPlayerResponses() {
		return playerResponses;
	}

	public Long getCreationTimestamp() {
		return creationTimestamp;
	}

	public Long getDuration() {
		return duration;
	}

	public int getVotes(PollResponse response) {
		int votes = 0;
		for(UUID uuid : playerResponses.values()) {
			if(uuid.equals(response.getUuid())) votes++;
		}
		return votes;
	}

	public double getVotePercentage(PollResponse response) {
		if(playerResponses.isEmpty()) return 0.0;
		return (double) getVotes(response) / playerResponses.size() * 100.0;
	}

	public PollStatus getStatus() {
		long currentTime = System.currentTimeMillis();
		if(creationTimestamp + duration > currentTime && !isClosed) return PollStatus.ACTIVE;
		else return PollStatus.CLOSED;
	}

	public void close() {
		isClosed = true;
		duration = System.currentTimeMillis() - creationTimestamp;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public PollResponse getWinningResponse() {
		PollResponse winningResponse = null;
		int maxVotes = 0;

		for(PollResponse response : responses) {
			int votes = getVotes(response);
			if(votes > maxVotes) {
				maxVotes = votes;
				winningResponse = response;
			}
		}

		return winningResponse;
	}

	public void respond(Player player, UUID responseUuid) {
		if(playerResponses.containsKey(player.getUniqueId())) {
			throw new IllegalStateException("Player has already responded to this poll.");
		}

		PollResponse response = getResponse(responseUuid);

		if(response == null) {
			throw new IllegalArgumentException("Response UUID does not match any available responses.");
		}

		playerResponses.put(player.getUniqueId(), responseUuid);
		PollManager.getInstance().savePolls();
	}
}