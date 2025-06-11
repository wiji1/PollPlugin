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
			Logger logger = Logger.getLogger(PollManager.class.getName());

			storage.loadPolls(loadedPolls -> {
				this.polls = new ArrayList<>(loadedPolls);
				logger.info("Loaded " + polls.size() + " polls from storage.");
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

	public List<Poll> getPolls() {
		return polls;
	}

	public void addPoll(Poll poll) {
		this.polls.add(poll);
		savePolls();
	}

	public static PollManager getInstance() {
		if (instance == null) instance = new PollManager();
		return instance;
	}
}
