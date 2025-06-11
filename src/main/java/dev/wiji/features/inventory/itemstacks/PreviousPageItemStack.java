package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PreviousPageItemStack extends CustomItemStack {
	private final int currentPage;
	private final int totalPages;

	public PreviousPageItemStack(Player player, int currentPage, int totalPages) {
		super(player);

		this.currentPage = currentPage;
		this.totalPages = totalPages;

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(Material.ARROW);
		ItemMeta nextMeta = itemStack.getItemMeta();
		NamedTextColor color = currentPage > 0 ? NamedTextColor.GREEN : NamedTextColor.GRAY;
		nextMeta.displayName(Component.text("Previous Page", color).decoration(TextDecoration.ITALIC, false));
		nextMeta.getPersistentDataContainer().set(ItemKey.PREVIOUS_PAGE.get(), PersistentDataType.BYTE, (byte) 1);
		itemStack.setItemMeta(nextMeta);
	}
}
