package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PollCreateConfirmItemStack extends CustomItemStack {

	private final PollCreateQuestionItemStack questionItemStack;
	private final PollCreateResponseItemStack[] responseItemStacks;

	public PollCreateConfirmItemStack(Player player,
		  PollCreateQuestionItemStack questionItemStack,
		  PollCreateResponseItemStack[] responseItemStacks) {
		super(player);

		this.questionItemStack = questionItemStack;
		this.responseItemStacks = responseItemStacks;

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(canConfirm() ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA);
		TextColor color = canConfirm() ? NamedTextColor.GREEN : NamedTextColor.RED;

		ItemMeta meta = itemStack.getItemMeta();
		meta.displayName(Component.text("Confirm Creation", color)
				.decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.empty());

		lore.add(Component.text("Confirming will create and open this poll", NamedTextColor.GRAY)
				.decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());

		String text = "Click to confirm";
		if (!canConfirm()) {
			if (questionItemStack.getQuestion() == null) {
				text = "You must set a question";
			} else {
				text = "You must set at least one response";
			}
		}

		lore.add(Component.text(text, color)
				.decoration(TextDecoration.ITALIC, false));

		meta.lore(lore);

		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.CREATE_POLL_CONFIRM.get(), PersistentDataType.BYTE, (byte) 1);

		itemStack.setItemMeta(meta);
	}

	public boolean canConfirm() {
		if(questionItemStack.getQuestion() == null) return false;

		for(PollCreateResponseItemStack responseItemStack : responseItemStacks) {
			if(responseItemStack.getResponse() != null) return true;
		}

		return false;
	}
}
