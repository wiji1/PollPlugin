package dev.wiji.features.storage.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.wiji.features.config.controllers.ConfigManager;
import dev.wiji.features.storage.implementations.MySQLStorage;
import dev.wiji.features.storage.implementations.SQLiteStorage;
import dev.wiji.features.storage.models.PluginStorage;
import dev.wiji.features.storage.models.TextComponentAdapter;
import dev.wiji.features.storage.implementations.RedisStorage;
import net.kyori.adventure.text.TextComponent;

public class StorageManager {

	private static StorageManager instance;
	private Gson gson;
	private PluginStorage storage;

	private StorageManager() {
		instance = this;
	}

	public void init() {
		this.gson = new GsonBuilder()
				.registerTypeAdapter(TextComponent.class, new TextComponentAdapter())
				.create();

		String storageType = ConfigManager.getInstance().getConfig().getString("storage", "sqlite");

		switch (storageType.toLowerCase()) {
			case "sqlite" -> this.storage = new SQLiteStorage();
			case "mysql" -> this.storage = new MySQLStorage();
			case "redis" -> this.storage = new RedisStorage();
			default -> throw new IllegalArgumentException("Unsupported storage type: " + storageType);
		}

		storage.init();
	}

	public Gson getGson() {
		return gson;
	}

	public PluginStorage getStorage() {
		return storage;
	}

	public static StorageManager getInstance() {
		if (instance == null) instance = new StorageManager();
		return instance;
	}
}
