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

public class BackItemStack extends CustomItemStack {

	public BackItemStack(Player player) {
		super(player);

		build();
	}

	@Override
	public void build() {
		itemStack = new ItemStack(Material.ARROW);
		ItemMeta nextMeta = itemStack.getItemMeta();
		nextMeta.displayName(Component.text("Back", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
		nextMeta.getPersistentDataContainer().set(ItemKey.BACK.get(), PersistentDataType.BYTE, (byte) 1);
		itemStack.setItemMeta(nextMeta);
	}
}
