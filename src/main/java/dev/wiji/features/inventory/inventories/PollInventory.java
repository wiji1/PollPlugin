package dev.wiji.features.inventory.inventories;

import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.itemstacks.BackItemStack;
import dev.wiji.features.inventory.models.CustomInventory;
import dev.wiji.features.inventory.itemstacks.QuestionItemStack;
import dev.wiji.features.inventory.itemstacks.ResponseItemStack;
import dev.wiji.features.poll.enums.PollStatus;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import dev.wiji.features.sound.PluginSound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class PollInventory extends CustomInventory {
	public Poll poll;

	public PollInventory(Poll poll, Player player) {
		super(player);
		this.poll = poll;
		init();
	}

	public void build() {
		QuestionItemStack questionItem = new QuestionItemStack(poll, player);
		inventory.setItem(4, questionItem.getItemStack());

		List<PollResponse> responses = List.of(poll.getResponses());
		if(responses.isEmpty()) {
			return;
		}

		Integer[] slots = getSlots(responses.size());

		for (int i = 0; i < responses.size(); i++) {
			PollResponse response = responses.get(i);
			ResponseItemStack responseItem = new ResponseItemStack(poll, response, player);
			ItemStack itemStack = responseItem.getItemStack();
			inventory.setItem(slots[i], itemStack);
		}

		ItemStack backButton = new BackItemStack(player).getItemStack();
		inventory.setItem((getRows() * 9) - 5, backButton);
	}

	@Override
	public int getRows() {
		return 5;
	}

	@Override
	public Component getName() {
		return poll.getQuestion().color(NamedTextColor.DARK_GRAY);
	}

	@Override
	public boolean hasBorder() {
		return true;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {

	}

	@Override
	public void onClose(InventoryCloseEvent event) {

	}

	@Override
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(true);

		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;

		ItemStack clickedItem = event.getCurrentItem();
		PersistentDataContainer container = clickedItem.getItemMeta().getPersistentDataContainer();

		if (container.has(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING)) {
			if (poll.getStatus() == PollStatus.CLOSED) {
				PluginSound.ERROR.play(player);
				return;
			}

			String value = container.get(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING);

			if (value == null) return;

			poll.respond((Player) event.getWhoClicked(), UUID.fromString(value));

			PluginSound.SUCCESS.play(player);
			player.sendMessage(Component.text("Your vote has been recorded!", NamedTextColor.GREEN));
			inventory.close();
		} else if (container.has(ItemKey.BACK.get())) {
			PluginSound.CLICK.play(player);
			PollListInventory pollListInventory = new PollListInventory(player);
			InventoryManager.getInstance().openInventory(pollListInventory);
		}

	}

	private Integer[] getSlots(int choices) {
		switch(choices) {
			case 1: return new Integer[]{22};
			case 2: return new Integer[]{21, 23};
			case 3: return new Integer[]{20, 22, 24};
			case 4: return new Integer[]{19, 21, 23, 25};
			case 5: return new Integer[]{11, 15, 22, 29, 33};
			case 6: return new Integer[]{12, 14, 19, 25, 30, 32};
			default: return new Integer[0];
		}
	}
}
