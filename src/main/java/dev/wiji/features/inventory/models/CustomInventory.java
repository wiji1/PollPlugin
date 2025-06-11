package dev.wiji.features.inventory.models;

import dev.wiji.PollPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomInventory implements InventoryHolder {
	protected Inventory inventory;
	protected Player player;

	public CustomInventory(Player player) {
		this.player = player;
	}

	public void init() {

		this.inventory = PollPlugin.getInstance().getServer().createInventory(this,
				getRows() * 9,
				getName());

		if (hasBorder()) {
			List<Integer> borderSlots = new ArrayList<>();
			for (int i = 0; i < getRows() * 9; i++) {
				if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= (getRows() - 1) * 9) {
					borderSlots.add(i);
				}
			}

			ItemStack borderItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta meta = borderItem.getItemMeta();
			meta.displayName(Component.empty());
			borderItem.setItemMeta(meta);

			for (int slot : borderSlots) inventory.setItem(slot, borderItem);
		}

		build();
	}

	public void rebuild() {
		init();
		player.updateInventory();
		player.openInventory(inventory);
	}

	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public abstract void build();

	public abstract int getRows();

	public abstract Component getName();

	public abstract boolean hasBorder();

	public abstract void onOpen(InventoryOpenEvent event);

	public abstract void onClose(InventoryCloseEvent event);

	public abstract void onClick(InventoryClickEvent event);
}


