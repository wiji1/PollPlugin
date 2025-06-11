package dev.wiji.features.poll.controllers;

import dev.wiji.PollPlugin;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.storage.controllers.StorageManager;
import dev.wiji.features.storage.models.PluginStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PollManager {
	private static PollManager instance;
	private List<Poll> polls;

	private PollManager() {
		instance = this;
	}

	public void init() {
		this.polls = new ArrayList<>();

		loadPolls();
	}

	public void loadPolls() {
		new Thread(() -> {
			PluginStorage storage = StorageManager.getInstance().getStorage();
			Logger logger = PollPlugin.getInstance().getLogger();

			storage.loadPolls(loadedPolls -> {
				this.polls = new ArrayList<>(loadedPolls);
				logger.info("Loaded " + polls.size() + " polls from storage.");

				initializePollIds();
			});
		}).start();
	}

	public void savePolls() {
		new Thread(() -> {
			PluginStorage storage = StorageManager.getInstance().getStorage();
			Logger logger = PollPlugin.getInstance().getLogger();

			storage.savePolls(() -> this.polls);
			logger.info("Saved " + polls.size() + " polls to storage.");
		}).start();
	}

	public void removePoll(int pollId) {
		new Thread(() -> {
			Poll poll = getPollById(pollId);
			if (poll != null) {
				StorageManager.getInstance().getStorage().deletePoll(() -> poll);
				PollIdManager.getInstance().removePoll(poll);

				polls.remove(poll);
			}
		}).start();
	}

	public void closePoll(int pollId) {
		Poll poll = getPollById(pollId);
		if (poll != null) {
			poll.close();
			savePolls();
		}
	}

	public List<Poll> getPolls() {

		List<Poll> sortedPolls = PollIdManager.getInstance().getAllPollsSorted();
		return polls;
	}

	public void addPoll(Poll poll) {
		this.polls.add(poll);
		savePolls();
	}

	public Poll getPollById(int id) {
		return PollIdManager.getInstance().getPollById(id);
	}

	public List<Poll> getAllPollsSorted() {
		return PollIdManager.getInstance().getAllPollsSorted();
	}


	public void initializePollIds() {
		PollIdManager.getInstance().registerPolls(polls);
	}

	public static PollManager getInstance() {
		if (instance == null) instance = new PollManager();
		return instance;
	}
}
