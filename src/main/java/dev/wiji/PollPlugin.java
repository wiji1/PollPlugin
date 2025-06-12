package dev.wiji;

import dev.wiji.features.chat.controllers.ChatManager;
import dev.wiji.features.command.controllers.CommandManager;
import dev.wiji.features.config.controllers.ConfigManager;
import dev.wiji.features.inventory.controllers.InventoryManager;
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
		ConfigManager.getInstance().init(this);
		StorageManager.getInstance().init();
		PollManager.getInstance().init();
		InventoryManager.getInstance().init(this);
		ChatManager.getInstance().init(this);

		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			Commands registrar = commands.registrar();
			CommandManager.getInstance().init(registrar);
		});

		getLogger().info("PollPlugin has been enabled!");
	}

	@Override
	public void onDisable() {
		StorageManager.getInstance().getStorage().close();

		getLogger().info("PollPlugin has been disabled!");
	}

	public static PollPlugin getInstance() {
		return instance;
	}
}