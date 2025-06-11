package dev.wiji.features.inventory.controllers;

import dev.wiji.PollPlugin;
import dev.wiji.features.inventory.listeners.InventoryListener;
import dev.wiji.features.inventory.models.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
	private static InventoryManager instance;
	private List<CustomInventory> activeInventories;

	public InventoryManager() {
		instance = this;
	}

	public void init(PollPlugin plugin) {
		activeInventories = new ArrayList<>();
		plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
	}

	public void registerInventory(CustomInventory inventory) {
		activeInventories.add(inventory);
	}

	public void unregisterInventory(CustomInventory inventory) {
		activeInventories.remove(inventory);
	}

	public CustomInventory getInventory(Inventory inventory) {
		for (CustomInventory customInventory : activeInventories) {
			if (customInventory.getInventory().equals(inventory)) {
				return customInventory;
			}
		}
		return null;
	}

	public void openInventory(CustomInventory inventory) {
		registerInventory(inventory);
		inventory.getPlayer().openInventory(inventory.getInventory());
	}

	public static InventoryManager getInstance() {
		if (instance == null) instance = new InventoryManager();
		return instance;
	}
}
