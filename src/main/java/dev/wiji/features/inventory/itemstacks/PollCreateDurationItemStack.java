package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import dev.wiji.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PollCreateDurationItemStack extends CustomItemStack {

	private long duration;

	public PollCreateDurationItemStack(Player player) {
		super(player);

		duration = 1000 * 60 * 60;
		itemStack = new ItemStack(Material.CLOCK);
		build();
	}

	@Override
	public void build() {

		ItemMeta meta = itemStack.getItemMeta();
		meta.displayName(Component.text("Poll Duration", NamedTextColor.YELLOW)
				.decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.empty());

		String formattedDuration = Arrays.stream(TimeUtils.formatDuration(duration).split(" "))
				.map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
				.collect(Collectors.joining(" "));

		lore.add(Component.text(formattedDuration, NamedTextColor.WHITE)
				.decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());
		lore.add(Component.text("Left-click to change duration", NamedTextColor.GREEN)
				.decoration(TextDecoration.ITALIC, false));

		meta.lore(lore);

		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.CREATE_POLL_DURATION.get(), PersistentDataType.BYTE, (byte) 1);

		itemStack.setItemMeta(meta);
	}

	public void updateDuration(long duration) {
		this.duration = duration;
		build();
	}

	public long getDuration() {
		return duration;
	}
}
