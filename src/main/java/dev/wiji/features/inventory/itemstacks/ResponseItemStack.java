package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.features.poll.models.PollResponse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ResponseItemStack extends CustomItemStack {
	private final PollResponse response;

	public ResponseItemStack(PollResponse response, Player player) {
		super(player);
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
		lore.add(Component.text("Click to vote for this response")
				.color(TextColor.color(0xFFFE39))
				.decoration(TextDecoration.ITALIC, false));
		meta.lore(lore);

		meta.getPersistentDataContainer().set(ItemKey.POLL_RESPONSE_UUID.get(), PersistentDataType.STRING, response.getUuid().toString());
		itemStack.setItemMeta(meta);
	}

	public PollResponse getResponse() {
		return response;
	}
}
