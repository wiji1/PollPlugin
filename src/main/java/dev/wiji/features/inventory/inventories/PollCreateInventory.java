package dev.wiji.features.inventory.inventories;

import dev.wiji.features.chat.controllers.ChatManager;
import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.itemstacks.PollCreateConfirmItemStack;
import dev.wiji.features.inventory.itemstacks.PollCreateDurationItemStack;
import dev.wiji.features.inventory.itemstacks.PollCreateQuestionItemStack;
import dev.wiji.features.inventory.itemstacks.PollCreateResponseItemStack;
import dev.wiji.features.inventory.models.CustomInventory;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import dev.wiji.features.sound.PluginSound;
import dev.wiji.utils.ColorUtils;
import dev.wiji.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PollCreateInventory extends CustomInventory {

	private final PollCreateQuestionItemStack questionItem;
	private final PollCreateDurationItemStack durationItem;
	private final PollCreateResponseItemStack[] responseItems;
	private PollCreateConfirmItemStack confirmItem;

	private final int[] responseSlots = {12, 14, 19, 25, 30, 32};
	private final int questionSlot = 2;
	private final int durationSlot = 4;
	private final int createSlot = 6;

	public PollCreateInventory(Player player) {
		super(player);

		questionItem = new PollCreateQuestionItemStack(player);
		durationItem = new PollCreateDurationItemStack(player);
		responseItems = new PollCreateResponseItemStack[responseSlots.length];

		for (int i = 0; i < responseItems.length; i++) {
			responseItems[i] = new PollCreateResponseItemStack(player, i);
		}

		init();
	}

	@Override
	public void build() {
		inventory.setItem(questionSlot, questionItem.getItemStack());
		inventory.setItem(durationSlot, durationItem.getItemStack());

		for (int i = 0; i < responseItems.length; i++) {
			inventory.setItem(responseSlots[i], responseItems[i].getItemStack());
		}

		confirmItem = new PollCreateConfirmItemStack(player, questionItem, responseItems);
		inventory.setItem(createSlot, confirmItem.getItemStack());
	}

	@Override
	public int getRows() {
		return 5;
	}

	@Override
	public Component getName() {
		return Component.text("Create Poll");
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

		ItemStack clickedItem = event.getCurrentItem();
		if (clickedItem == null || clickedItem.getItemMeta() == null) return;

		PersistentDataContainer container = clickedItem.getItemMeta().getPersistentDataContainer();
		Player player = (Player) event.getWhoClicked();

		if (container.has(ItemKey.CREATE_POLL_QUESTION.get())) {
			if (event.isRightClick()) {
				if (questionItem.getQuestion() == null) return;

				PluginSound.CLICK.play(player);
				ItemSelectInventory itemSelectInventory = new ItemSelectInventory(player, (material) -> {
					questionItem.updateMaterial(material);
					PluginSound.SUCCESS.play(player);

					InventoryManager.getInstance().openInventory(this);
					build();
				});

				InventoryManager.getInstance().openInventory(itemSelectInventory);
			} else if (event.isLeftClick()) {
				Component prompt = Component.text("Please enter the poll question:", NamedTextColor.GOLD);
				player.closeInventory();

				PluginSound.CLICK.play(player);
				ChatManager.getInstance().promptChat(player, prompt, (response) -> {
					TextComponent colored = ColorUtils.colorize(response);

					if (response != null) {
						PluginSound.SUCCESS.play(player);
						questionItem.updateQuestion(colored);
						build();
					}

					InventoryManager.getInstance().openInventory(this);
				});
			}
		} else if (container.has(ItemKey.CREATE_POLL_RESPONSE.get())) {
			int responseIndex = container.get(ItemKey.CREATE_POLL_RESPONSE.get(), PersistentDataType.INTEGER);
			if (responseIndex < 0 || responseIndex >= responseItems.length) return;

			PollCreateResponseItemStack responseItem = responseItems[responseIndex];

			if (event.isRightClick()) {
				if(responseItem.getResponse() == null) return;

				PluginSound.CLICK.play(player);
				ItemSelectInventory itemSelectInventory = new ItemSelectInventory(player, (material) -> {
					PluginSound.SUCCESS.play(player);
					responseItem.updateMaterial(material);

					InventoryManager.getInstance().openInventory(this);
					build();
				});

				InventoryManager.getInstance().openInventory(itemSelectInventory);
			} else if (event.isLeftClick()) {
				Component prompt = Component.text("Please enter the poll response:", NamedTextColor.GOLD);
				player.closeInventory();

				PluginSound.CLICK.play(player);
				ChatManager.getInstance().promptChat(player, prompt, (response) -> {
					TextComponent colored = ColorUtils.colorize(response);

					if (response != null) {
						PluginSound.SUCCESS.play(player);
						if (responseItem.getResponse() == null) responseItem.updateMaterial(Material.PAPER);
						responseItem.updateResponse(colored);
						build();
					}

					InventoryManager.getInstance().openInventory(this);
				});
			}
		} else if (container.has(ItemKey.CREATE_POLL_DURATION.get())) {
			promptDuration();
		} else if (container.has(ItemKey.CREATE_POLL_CONFIRM.get())) {
			if (confirmItem.canConfirm()) {
				List<PollResponse> responses = new ArrayList<>();

				for(PollCreateResponseItemStack responseItem : responseItems) {
					if(responseItem.getResponse() == null) continue;
					responses.add(new PollResponse(responseItem.getResponse(), responseItem.getItemStack().getType()));
				}

				long duration = durationItem.getDuration();
				TextComponent question = questionItem.getQuestion();
				Material icon = questionItem.getItemStack().getType();

				Poll poll = new Poll(question, responses.toArray(new PollResponse[0]), duration, icon);
				PollManager.getInstance().addPoll(poll);
				player.closeInventory();

				player.sendMessage(Component.text("Poll created successfully!", NamedTextColor.GREEN));
				PluginSound.SUCCESS.play(player);

			} else PluginSound.ERROR.play(player);
		}
	}

	public void promptDuration() {
		PluginSound.CLICK.play(player);
		Component prompt = Component.text("Please enter the poll duration:", NamedTextColor.GOLD);
		player.closeInventory();

		ChatManager.getInstance().promptChat(player, prompt, (response) -> {

			if (response != null) {

				long time;

				try {
					time = TimeUtils.durationStringToMillis(response);
				} catch(IllegalArgumentException e) {
					player.sendMessage(Component.text("Invalid duration format.", NamedTextColor.RED));
					PluginSound.ERROR.play(player);
					promptDuration();
					return;
				}

				if (time > 0) {
					PluginSound.SUCCESS.play(player);
					durationItem.updateDuration(time);
					build();
				}
			}

			InventoryManager.getInstance().openInventory(this);
		});
	}
}
