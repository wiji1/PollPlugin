package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.features.poll.enums.PollStatus;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PollItemStack extends CustomItemStack {
	private final Poll poll;

	public PollItemStack(Poll poll, Player player) {
		super(player);
		this.poll = poll;

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(poll.getIcon());
		ItemMeta meta = itemStack.getItemMeta();

		meta.displayName(poll.getQuestion().decoration(TextDecoration.ITALIC, false).colorIfAbsent(NamedTextColor.YELLOW));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.empty());
		lore.add(Component.text("Status: ", NamedTextColor.GRAY)
				.append(poll.getStatus().getDisplayName()).decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());

		Component creationTime = Component.text("Created: ", NamedTextColor.GRAY);
		String formattedTime = TimeUtils.getRelativeTime(poll.getCreationTimestamp());
		creationTime = creationTime.append(Component.text(formattedTime, NamedTextColor.WHITE));
		lore.add(creationTime.decoration(TextDecoration.ITALIC, false));

		boolean isActive = poll.getStatus() == PollStatus.ACTIVE;
		Component endTime = Component.text("End" + (isActive ? "s" : "ed") + ": ", NamedTextColor.GRAY);
		String formattedEndTime = TimeUtils.getRelativeTime(poll.getCreationTimestamp() + poll.getDuration());
		endTime = endTime.append(Component.text(formattedEndTime, NamedTextColor.WHITE));
		lore.add(endTime.decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());

		boolean hasVoted = poll.getPlayerResponses().containsKey(player.getUniqueId());
		if (!hasVoted) meta.setEnchantmentGlintOverride(true);

		if (poll.getStatus() == PollStatus.CLOSED) {
			lore.add(Component.text("Click to view results", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));

		} else {
			String clickText = hasVoted ? "You already voted on this poll" : "Click to vote on this poll";
			TextColor textColor = hasVoted ? NamedTextColor.RED : NamedTextColor.GREEN;
			lore.add(Component.text(clickText, textColor).decoration(TextDecoration.ITALIC, false));
		}

		meta.lore(lore);
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.POLL_UUID.get(), PersistentDataType.STRING, poll.getUuid().toString());

		itemStack.setItemMeta(meta);
	}

	public Poll getPoll() {
		return poll;
	}
}
