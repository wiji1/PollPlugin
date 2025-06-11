package dev.wiji.features.inventory.listeners;

import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.models.CustomInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {

	private static final InventoryManager manager = InventoryManager.getInstance();

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		CustomInventory inventory = manager.getInventory(event.getInventory());
		if(inventory == null) return;

		inventory.onOpen(event);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		CustomInventory inventory = manager.getInventory(event.getInventory());
		if(inventory == null) return;

		inventory.onClose(event);
		manager.unregisterInventory(inventory);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		CustomInventory inventory = manager.getInventory(event.getInventory());
		if(inventory == null) return;

		inventory.onClick(event);
	}

}
