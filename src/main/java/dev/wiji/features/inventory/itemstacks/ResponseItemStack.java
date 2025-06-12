package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.features.poll.enums.PollStatus;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import dev.wiji.utils.PercentageBarUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ResponseItemStack extends CustomItemStack {
	private final Poll poll;
	private final PollResponse response;

	public ResponseItemStack(Poll poll, PollResponse response, Player player) {
		super(player);
		this.poll = poll;
		this.response = response;

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(Material.SLIME_BALL);

		ItemMeta meta = itemStack.getItemMeta();
		meta.customName(response.getText().decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.text(""));

		if (poll.getStatus() == PollStatus.CLOSED) {
			PollResponse winningResponse = poll.getWinningResponse();
			if (winningResponse == response) meta.setEnchantmentGlintOverride(true);

			lore.add(Component.text("Votes: ", NamedTextColor.GRAY)
					.append(Component.text(poll.getVotes(response), NamedTextColor.YELLOW))
					.decoration(TextDecoration.ITALIC, false));

			lore.add(Component.empty());

			Component percentageBar = PercentageBarUtils.createGradientPercentageBar(poll.getVotePercentage(response));
			lore.add(percentageBar.decoration(TextDecoration.ITALIC, false));

			lore.add(Component.empty());
			lore.add(Component.text("Voting has ended", NamedTextColor.RED)
					.decoration(TextDecoration.ITALIC, false));
		} else {
			lore.add(Component.text("Click to vote for this response", NamedTextColor.YELLOW)
					.decoration(TextDecoration.ITALIC, false));
		}

		meta.lore(lore);

		meta.getPersistentDataContainer().set(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING, response.getUuid().toString());
		itemStack.setItemMeta(meta);
	}
}
