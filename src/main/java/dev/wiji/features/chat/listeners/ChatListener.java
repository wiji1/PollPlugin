package dev.wiji.features.chat.listeners;

import dev.wiji.PollPlugin;
import dev.wiji.features.chat.controllers.ChatManager;
import dev.wiji.features.inventory.inventories.PollCreateInventory;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncChatEvent event) {
		Player player = event.getPlayer();
		ChatManager.ChatPrompt prompt = ChatManager.getInstance().getChatPrompt(player);

		if (prompt != null) {
			event.setCancelled(true);
			String message = ((TextComponent) event.message()).content();

			Bukkit.getScheduler().runTask(PollPlugin.getInstance(), () -> prompt.responseConsumer().accept(message));

			prompt.timeoutTask().cancel();
			ChatManager.getInstance().removeChatPrompt(player);
		}
	}
}
