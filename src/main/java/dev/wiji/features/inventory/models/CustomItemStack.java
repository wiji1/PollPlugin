package dev.wiji.features.inventory.models;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItemStack {
	protected final Player player;
	protected ItemStack itemStack;

	public CustomItemStack(Player player) {
		this.player = player;
	}

	public abstract void build();

	public Player getPlayer() {
		return player;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
}
