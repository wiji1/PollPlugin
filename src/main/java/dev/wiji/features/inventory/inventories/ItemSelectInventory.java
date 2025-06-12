package dev.wiji.features.inventory.inventories;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.itemstacks.NextPageItemStack;
import dev.wiji.features.inventory.itemstacks.PreviousPageItemStack;
import dev.wiji.features.inventory.models.CustomInventory;
import dev.wiji.features.sound.PluginSound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemSelectInventory extends CustomInventory {
	private final List<Material> materials;
	private final Consumer<Material> callback;
	private int page = 0;
	private static final int MATERIALS_PER_PAGE = 28;

	public ItemSelectInventory(Player player, Consumer<Material> callback) {
		super(player);
		this.materials = getAllMaterials();
		this.callback = callback;
		init();
	}

	@Override
	public void build() {
		addPaginationControls();

		int startIndex = page * MATERIALS_PER_PAGE;
		int endIndex = Math.min(startIndex + MATERIALS_PER_PAGE, materials.size());

		if (startIndex >= materials.size()) return;

		List<Material> materialsOnPage = materials.subList(startIndex, endIndex);

		int[] availableSlots = {
				10, 11, 12, 13, 14, 15, 16,
				19, 20, 21, 22, 23, 24, 25,
				28, 29, 30, 31, 32, 33, 34,
				37, 38, 39, 40, 41, 42, 43
		};

		int slotIndex = 0;
		for (Material material : materialsOnPage) {
			if (slotIndex >= availableSlots.length) break;

			ItemStack materialItem = createMaterialItem(material);
			inventory.setItem(availableSlots[slotIndex], materialItem);
			slotIndex++;
		}
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public Component getName() {
		int totalPages = (int) Math.ceil((double) materials.size() / MATERIALS_PER_PAGE);
		if (totalPages < 2) return Component.text("Select Material");

		return Component.text("Select Material (Page " + (page + 1) + "/" + totalPages + ")");
	}

	@Override
	public boolean hasBorder() {
		return true;
	}

	private void addPaginationControls() {
		int totalPages = (int) Math.ceil((double) materials.size() / MATERIALS_PER_PAGE);
		if (totalPages < 2) return;

		ItemStack prevButton = new PreviousPageItemStack(player, page, totalPages).getItemStack();
		inventory.setItem(48, prevButton);

		ItemStack nextButton = new NextPageItemStack(player, page, totalPages).getItemStack();
		inventory.setItem(50, nextButton);
	}

	private List<Material> getAllMaterials() {
		List<Material> allMaterials = Arrays.asList(Material.values());
		List<Material> filteredMaterials = new ArrayList<>();

		allMaterials = allMaterials.stream()
				.filter(material -> !material.name().contains("LEGACY"))
				.collect(Collectors.toList());


		allMaterials.forEach(material -> {
			if (material.name().contains("LEGACY") || material == Material.AIR) return;

			ItemStack testItem = null;

			try {
				testItem = new ItemStack(material);
			} catch (IllegalArgumentException ignored) { }

			if (testItem != null) filteredMaterials.add(material);
		});

		return filteredMaterials;
	}

	private ItemStack createMaterialItem(Material material) {

		ItemStack item = new ItemStack(material);;
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			String displayName = Arrays.stream(material.name().toLowerCase().split("_"))
					.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
					.collect(Collectors.joining(" "));


			meta.displayName(Component.text(displayName, NamedTextColor.GRAY)
					.decoration(TextDecoration.ITALIC, false));

			meta.lore(List.of(Component.empty(),
					Component.text("Click to select this material", NamedTextColor.YELLOW)
							.decoration(TextDecoration.ITALIC, false)));

			meta.addItemFlags(ItemFlag.values());

			PersistentDataContainer container = meta.getPersistentDataContainer();
			container.set(ItemKey.SELECTED_BLOCK.get(), PersistentDataType.STRING, material.name());

			item.setItemMeta(meta);
		}

		return item;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {

	}

	@Override
	public void onClose(InventoryCloseEvent event) {

	}

	@Override
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(true);

		ItemStack clickedItem = event.getCurrentItem();
		if (clickedItem == null || clickedItem.getItemMeta() == null) return;

		PersistentDataContainer container = clickedItem.getItemMeta().getPersistentDataContainer();
		Player player = (Player) event.getWhoClicked();

		if (container.has(ItemKey.SELECTED_BLOCK.get(), PersistentDataType.STRING)) {
			String materialName = container.get(ItemKey.SELECTED_BLOCK.get(), PersistentDataType.STRING);
			if (materialName == null) return;

			try {
				Material selectedMaterial = Material.valueOf(materialName);
				onMaterialSelected(player, selectedMaterial);
			} catch (IllegalArgumentException e) {
				return;
			}

		} else if (container.has(ItemKey.PREVIOUS_PAGE.get(), PersistentDataType.BYTE)) {
			if (page > 0) {
				PluginSound.CLICK.play(player);
				page--;
				rebuild();
			} else PluginSound.ERROR.play(player);

		} else if (container.has(ItemKey.NEXT_PAGE.get(), PersistentDataType.BYTE)) {
			int totalPages = (int) Math.ceil((double) materials.size() / MATERIALS_PER_PAGE);
			if (page < totalPages - 1) {
				PluginSound.CLICK.play(player);
				page++;
				rebuild();
			} else PluginSound.ERROR.play(player);
		}
	}

	protected void onMaterialSelected(Player player, Material selectedMaterial) {
		if (callback != null) callback.accept(selectedMaterial);
	}
}