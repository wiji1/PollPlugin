package dev.wiji.features.storage.implementations;

import com.google.gson.Gson;
import dev.wiji.PollPlugin;
import dev.wiji.features.config.controllers.ConfigManager;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.storage.controllers.StorageManager;
import dev.wiji.features.storage.models.PluginStorage;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public
class RedisStorage extends PluginStorage {
	private static final String STORAGE_KEY = "polls";
	private Jedis storageJedis;

	@Override
	public void init() {
		FileConfiguration config = ConfigManager.getInstance().getConfig();
		Logger logger = PollPlugin.getInstance().getLogger();

		String host = config.getString("redis.host", "localhost");
		int port = config.getInt("redis.port", 6379);
		String password = config.getString("redis.password", "");

		try {
			storageJedis = new Jedis(host, port);
			if (!password.isEmpty()) storageJedis.auth(password);

			storageJedis.ping();

			logger.info("Successfully connected to Redis Storage!");
		} catch (Exception e) {
			logger.severe("Failed to connect to Redis: " + e.getMessage());
		}
	}

	@Override
	public void loadPolls(Consumer<List<Poll>> pollConsumer) {
		Gson gson = StorageManager.getInstance().getGson();
		List<Poll> polls = storageJedis.hvals(STORAGE_KEY).stream()
				.map(json -> gson.fromJson(json, Poll.class))
				.toList();

		pollConsumer.accept(polls);
	}

	@Override
	public void savePolls(Supplier<List<Poll>> pollSupplier) {
		List<Poll> polls = pollSupplier.get();
		Gson gson = StorageManager.getInstance().getGson();

		polls.forEach(poll -> {
			String json = gson.toJson(poll);
			storageJedis.hset(STORAGE_KEY, poll.getUuid().toString(), json);
		});
	}

	@Override
	public void close() {
		if (storageJedis != null && storageJedis.isConnected()) {
			storageJedis.close();
			PollPlugin.getInstance().getLogger().info("Redis Storage connection closed.");
		} else {
			PollPlugin.getInstance().getLogger().warning("Redis Storage connection was not open.");
		}
	}
}
