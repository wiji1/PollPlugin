package dev.wiji.features.command.subcommands;

import com.mojang.brigadier.context.CommandContext;
import dev.wiji.features.command.enums.Permission;
import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.inventories.PollCreateInventory;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends SubCommand {
	public CreateCommand() {
		super("create", Permission.ADMIN.get());
	}

	@Override
	public int execute(CommandSourceStack source, CommandContext<?> context) {


		CommandSender sender = source.getSender();
		if (!(sender instanceof Player player)) {
			sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
			return 0;
		}

		PollCreateInventory inventory = new PollCreateInventory(player);
		InventoryManager.getInstance().openInventory(inventory);

		return 1;
	}
}
