package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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

public class PollCreateResponseItemStack extends CustomItemStack {
	private final int index;
	private TextComponent response;

	public PollCreateResponseItemStack(Player player, int index) {
		super(player);

		this.index = index;
		this.response = null;

		this.itemStack = new ItemStack(org.bukkit.Material.BARRIER);
		build();
	}

	@Override
	public void build() {

		ItemMeta meta = itemStack.getItemMeta();
		meta.displayName(Component.text("Poll Response #" + (index + 1), NamedTextColor.YELLOW)
				.decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.empty());

		lore.add(response != null ? response : Component.text("None!", NamedTextColor.RED)
				.decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());
		lore.add(Component.text("Left-click to change response", NamedTextColor.GREEN)
				.decoration(TextDecoration.ITALIC, false));

		if (response != null) {
			lore.add(Component.text("Right-click to change icon", NamedTextColor.YELLOW)
					.decoration(TextDecoration.ITALIC, false));

		}

		meta.lore(lore);

		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.CREATE_POLL_RESPONSE.get(), PersistentDataType.INTEGER, index);

		itemStack.setItemMeta(meta);
	}

	public void updateMaterial(Material material) {
		itemStack = new ItemStack(material);
		build();
	}

	public void updateResponse(Component response) {
		this.response = Component.text("\"", NamedTextColor.GRAY)
				.append(response)
				.append(Component.text("\"", NamedTextColor.GRAY))
				.decoration(TextDecoration.ITALIC, false);
		build();
	}

	public TextComponent getResponse() {
		return response;
	}
}
