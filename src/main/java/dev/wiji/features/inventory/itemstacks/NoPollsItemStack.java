package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.models.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class NoPollsItemStack extends CustomItemStack {

	public NoPollsItemStack(Player player) {
		super(player);

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(Material.BARREL);
		ItemMeta meta = itemStack.getItemMeta();

		meta.displayName(Component.text("No Polls Available")
				.color(NamedTextColor.RED)
				.decoration(TextDecoration.ITALIC, false));

		meta.lore(List.of(Component.text("Come back later when polls are active")
				.decoration(TextDecoration.ITALIC, false)));

		itemStack.setItemMeta(meta);
	}
}
