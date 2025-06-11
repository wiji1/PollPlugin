package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.features.poll.models.Poll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
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
		itemStack = new ItemStack(Material.PAPER);
		ItemMeta meta = itemStack.getItemMeta();

		meta.displayName(poll.getQuestion().decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.text("Click to view this poll.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
		meta.lore(lore);

		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.POLL_UUID.get(), PersistentDataType.STRING, poll.getUuid().toString());

		itemStack.setItemMeta(meta);
	}

	public Poll getPoll() {
		return poll;
	}
}
