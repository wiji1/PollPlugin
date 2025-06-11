package dev.wiji.features.redis.controllers;

import dev.wiji.PollPlugin;
import dev.wiji.features.config.controllers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

public class RedisManager {

	private static RedisManager instance;
	private Jedis publisherJedis;
	private Jedis subscriberJedis;
	private Jedis storageJedis;

	private RedisManager() {
		instance = this;
	}

	public void init() {
		FileConfiguration config = ConfigManager.getInstance().getConfig();
		Logger logger = PollPlugin.getInstance().getLogger();

		boolean enabled = config.getBoolean("Redis.enabled", false);

		if (!enabled) {
			PollPlugin.getInstance().getLogger().info("Redis is disabled in config.yml; skipping initialization.");
			return;
		}

		String host = config.getString("redis.host", "localhost");
		int port = config.getInt("redis.port", 6379);
		String password = config.getString("redis.password", "");

		try {
			publisherJedis = new Jedis(host, port);
			if (!password.isEmpty()) {
				publisherJedis.auth(password);
			}

			subscriberJedis = new Jedis(host, port);
			if (!password.isEmpty()) {
				subscriberJedis.auth(password);
			}

			storageJedis = new Jedis(host, port);
			if (!password.isEmpty()) {
				storageJedis.auth(password);
			}

			publisherJedis.ping();
			logger.info("Successfully connected to Redis for PubSub and Storage!");
		} catch (Exception e) {
			logger.severe("Failed to connect to Redis: " + e.getMessage());
		}


	}

	public static RedisManager getInstance() {
		if (instance == null) instance = new RedisManager();
		return instance;
	}
}
