package dev.wiji.features.inventory.inventories;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.itemstacks.BackItemStack;
import dev.wiji.features.inventory.models.CustomInventory;
import dev.wiji.features.inventory.itemstacks.QuestionItemStack;
import dev.wiji.features.inventory.itemstacks.ResponseItemStack;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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

		int responseIndex = 0;
		for(int row = 2; row < getRows(); row++) {
			int responsesLeft = responses.size() - responseIndex;
			if(responsesLeft <= 0) {
				break;
			}

			int itemsInThisRow = Math.min(responsesLeft, 5);

			int totalWidth = (itemsInThisRow * 2) - 1;

			int startCol = (9 - totalWidth) / 2;
			int rowBaseSlot = row * 9;

			for(int i = 0; i < itemsInThisRow; i++) {
				ResponseItemStack responseItem = new ResponseItemStack(responses.get(responseIndex + i), player);
				inventory.setItem(rowBaseSlot + startCol + (i * 2), responseItem.getItemStack());
			}

			responseIndex += itemsInThisRow;
		}

		ItemStack backButton = new BackItemStack(player).getItemStack();
		inventory.setItem((getRows() * 9) - 5, backButton);
	}

	@Override
	public int getRows() {
		int responseCount = poll.getResponses().length;
		int responseRows = (int) Math.ceil(responseCount / 5.0);
		return Math.min(6, 4 + responseRows);
	}

	@Override
	public Component getName() {
		return poll.getQuestion();
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

		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getItemMeta() == null) return;

		ItemStack clickedItem = event.getCurrentItem();
		PersistentDataContainer container = clickedItem.getItemMeta().getPersistentDataContainer();

		if (container.has(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING)) {
			String value = container.get(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING);

			if (value == null) return;

			poll.respond((Player) event.getWhoClicked(),
					UUID.fromString(value)
			);

			//TODO: play sound and send message
			inventory.close();
		}

	}
}
