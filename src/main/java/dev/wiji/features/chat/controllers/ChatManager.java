package dev.wiji.features.chat.controllers;

import dev.wiji.PollPlugin;
import dev.wiji.features.chat.listeners.ChatListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatManager {

	private static ChatManager instance;

	private Map<UUID, ChatPrompt> chatPrompts;

	public ChatManager() {
		instance = this;
		chatPrompts = new HashMap<>();
	}

	public void init(PollPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(new ChatListener(), plugin);
	}

	public void promptChat(Player player, Component message, Consumer<String> playerResponseConsumer) {

		if (chatPrompts.containsKey(player.getUniqueId())) chatPrompts.get(player.getUniqueId()).cancel();

		player.sendMessage(message);
		chatPrompts.put(player.getUniqueId(), new ChatPrompt(playerResponseConsumer,
			PollPlugin.getInstance().getServer().getScheduler().runTaskLater(PollPlugin.getInstance(), () -> {
				if (chatPrompts.containsKey(player.getUniqueId())) {
					ChatPrompt prompt = chatPrompts.remove(player.getUniqueId());
					prompt.cancel();
				}
			}, 20L * 30)
		));

	}

	public static ChatManager getInstance() {
		if (instance == null) instance = new ChatManager();
		return instance;
	}

	public ChatPrompt getChatPrompt(Player player) {
		return chatPrompts.get(player.getUniqueId());
	}

	public void removeChatPrompt(Player player) {
		chatPrompts.remove(player.getUniqueId());
	}

	public record ChatPrompt(Consumer<String> responseConsumer, BukkitTask timeoutTask) {

		public void cancel() {
			if(timeoutTask != null && !timeoutTask.isCancelled()) timeoutTask.cancel();
			responseConsumer.accept(null);
		}
	}
}
