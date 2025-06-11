package dev.wiji.features.config.controllers;

import dev.wiji.PollPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

	private static ConfigManager instance;
	private PollPlugin plugin;

	public ConfigManager() {
		instance = this;
	}

	public void init(PollPlugin plugin) {
		this.plugin = plugin;

		plugin.saveDefaultConfig();

		plugin.getConfig().options().copyDefaults(true);
		plugin.reloadConfig();
	}

	public FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	public void saveConfig() {
		plugin.saveConfig();
	}

	public void reloadConfig() {
		plugin.reloadConfig();
	}

	public static ConfigManager getInstance() {
		if (instance == null) instance = new ConfigManager();
		return instance;
	}
}
