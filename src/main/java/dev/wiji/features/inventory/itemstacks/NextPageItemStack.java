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

public class NextPageItemStack extends CustomItemStack {
	private final int currentPage;
	private final int totalPages;

	public NextPageItemStack(Player player, int currentPage, int totalPages) {
		super(player);

		this.currentPage = currentPage;
		this.totalPages = totalPages;

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(Material.ARROW);
		ItemMeta prevMeta = itemStack.getItemMeta();
		NamedTextColor color = currentPage < totalPages - 1 ? NamedTextColor.GREEN : NamedTextColor.GRAY;
		prevMeta.displayName(Component.text("Next Page", color).decoration(TextDecoration.ITALIC, false));
		prevMeta.getPersistentDataContainer().set(ItemKey.NEXT_PAGE.get(), PersistentDataType.BYTE, (byte) 1);
		itemStack.setItemMeta(prevMeta);
	}
}
