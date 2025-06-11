package dev.wiji.features.command.subcommands;

import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.inventories.PollListInventory;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends SubCommand {
	public ListCommand() {
		super("list");
	}

	@Override
	public int execute(CommandSourceStack source) {
		CommandSender sender = source.getSender();

		if(sender instanceof Player player) {
			PollListInventory pollInventory = new PollListInventory(player);
			InventoryManager.getInstance().openInventory(pollInventory);
		} else {
			sender.sendMessage(Component.text("Only players can use this command to view the poll list.", NamedTextColor.RED));
			return 0;
		}

		return 1;
	}
}
