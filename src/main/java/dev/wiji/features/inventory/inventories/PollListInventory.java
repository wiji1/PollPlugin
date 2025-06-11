package dev.wiji.features.inventory.inventories;
import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.itemstacks.NextPageItemStack;
import dev.wiji.features.inventory.itemstacks.PreviousPageItemStack;
import dev.wiji.features.inventory.models.CustomInventory;
import dev.wiji.features.inventory.itemstacks.PollItemStack;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

public class PollListInventory extends CustomInventory {
	private final List<Poll> polls;
	private int page = 0;
	private static final int POLLS_PER_PAGE = 5;

	public PollListInventory(Player player) {
		super(player);
		this.polls = PollManager.getInstance().getPolls();
		init();
	}

	@Override
	public void build() {
		addPaginationControls();

		int startIndex = page * POLLS_PER_PAGE;
		int endIndex = Math.min(startIndex + POLLS_PER_PAGE, polls.size());

		if (startIndex >= polls.size()) return;

		List<Poll> pollsOnPage = polls.subList(startIndex, endIndex);

		int[] availableSlots = {20, 21, 22, 23, 24};

		int slotIndex = 0;
		for (Poll poll : pollsOnPage) {
			if (slotIndex >= availableSlots.length) break;

			ItemStack pollItem = new PollItemStack(poll, player).getItemStack();


			inventory.setItem(availableSlots[slotIndex], pollItem);
			slotIndex++;
		}
	}

	@Override
	public int getRows() {
		return 5;
	}

	@Override
	public Component getName() {
		int totalPages = (int) Math.ceil((double) polls.size() / POLLS_PER_PAGE);
		if (totalPages < 2) return Component.text("Polls");

		return Component.text("Polls (Page " + (page + 1) + "/" + totalPages + ")");
	}


	@Override
	public boolean hasBorder() {
		return true;
	}

	private void addPaginationControls() {
		int totalPages = (int) Math.ceil((double) polls.size() / POLLS_PER_PAGE);
		if(totalPages < 2) return;

		ItemStack prevButton = new PreviousPageItemStack(player, page, totalPages).getItemStack();
		inventory.setItem(39, prevButton);

		ItemStack nextButton = new NextPageItemStack(player, page, totalPages).getItemStack();
		inventory.setItem(41, nextButton);
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

		if (container.has(ItemKey.POLL_UUID.get(), PersistentDataType.STRING)) {
			String value = container.get(ItemKey.POLL_UUID.get(), PersistentDataType.STRING);
			if (value == null) return;

			Poll poll = polls.stream()
					.filter(p -> p.getUuid().toString().equals(value))
					.findFirst()
					.orElse(null);

			if (poll == null) return;

			if(poll.getPlayerResponses().containsKey(player.getUniqueId())) {
				//TODO: Play noise
				return;
			}

			PollInventory pollInventory = new PollInventory(poll, player);
			InventoryManager.getInstance().openInventory(pollInventory);

		} else if (container.has(ItemKey.PREVIOUS_PAGE.get(), PersistentDataType.BYTE)) {
			if (page > 0) {
				page--;
				rebuild();
			} else {
				//TODO: Play noise for no previous page
			}
		} else if (container.has(ItemKey.NEXT_PAGE.get(), PersistentDataType.BYTE)) {
			int totalPages = (int) Math.ceil((double) polls.size() / POLLS_PER_PAGE);
			if (page < totalPages - 1) {
				page++;
				rebuild();
			} else {
				//TODO: Play noise for no next page
			}
		}
	}
}