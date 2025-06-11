package dev.wiji;

import dev.wiji.features.command.controllers.CommandManager;
import dev.wiji.features.config.controllers.ConfigManager;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.storage.controllers.StorageManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class PollPlugin extends JavaPlugin {

	private static PollPlugin instance;

	@Override
	public void onEnable() {
		instance = this;

		getLogger().info("PollPlugin has been enabled!");

		ConfigManager.getInstance().init(this);
		StorageManager.getInstance().init();
		PollManager.getInstance().init();

		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			Commands registrar = commands.registrar();
			CommandManager.getInstance().init(registrar);
		});
	}

	@Override
	public void onDisable() {
		getLogger().info("PollPlugin has been disabled!");
	}

	public static PollPlugin getInstance() {
		return instance;
	}
}