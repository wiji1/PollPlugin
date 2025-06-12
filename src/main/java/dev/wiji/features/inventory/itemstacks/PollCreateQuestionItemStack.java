package dev.wiji.features.inventory.itemstacks;

import dev.wiji.features.inventory.enums.ItemKey;
import dev.wiji.features.inventory.models.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PollCreateQuestionItemStack extends CustomItemStack {

	private TextComponent question;
	
	public PollCreateQuestionItemStack(Player player) {
		super(player);

		question = null;
		itemStack = new ItemStack(Material.OAK_SIGN);
		build();
	}

	@Override
	public void build() {

		ItemMeta meta = itemStack.getItemMeta();
		meta.displayName(Component.text("Poll Question", NamedTextColor.YELLOW)
				.decoration(TextDecoration.ITALIC, false));

		List<Component> lore = new ArrayList<>();
		lore.add(Component.empty());

		if (question != null) {
			lore.add(Component.text("\"", NamedTextColor.GRAY)
					.append(question)
					.append(Component.text("\"", NamedTextColor.GRAY)
					).decoration(TextDecoration.ITALIC, false));

		} else lore.add(Component.text("None!", NamedTextColor.RED)
				.decoration(TextDecoration.ITALIC, false));

		lore.add(Component.empty());
		lore.add(Component.text("Left-click to change question", NamedTextColor.GREEN)
				.decoration(TextDecoration.ITALIC, false));

		if (question != null) {
			lore.add(Component.text("Right-click to change icon", NamedTextColor.YELLOW)
					.decoration(TextDecoration.ITALIC, false));
		}

		meta.lore(lore);

		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ItemKey.CREATE_POLL_QUESTION.get(), PersistentDataType.BYTE, (byte) 1);

		itemStack.setItemMeta(meta);
	}

	public void updateMaterial(Material material) {
		itemStack = new ItemStack(material);
		build();
	}

	public void updateQuestion(TextComponent question) {
		this.question = question.decoration(TextDecoration.ITALIC, false);
		build();
	}

	public TextComponent getQuestion() {
		return question;
	}
}
