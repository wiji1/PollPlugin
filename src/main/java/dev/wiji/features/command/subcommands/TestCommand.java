package dev.wiji.features.command.subcommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.wiji.features.command.enums.Permission;
import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.inventory.controllers.InventoryManager;
import dev.wiji.features.inventory.inventories.ItemSelectInventory;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.utils.TimeUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class TestCommand extends SubCommand {

	public TestCommand() {
		super("test", Permission.ADMIN.get());

		addOptionalArgument("number", IntegerArgumentType.integer());
	}

	@Override
	public int execute(CommandSourceStack source, CommandContext<?> context) {

		Player player = (Player) source.getSender();

		int number = context.getArgument("number", Integer.class);
		player.sendMessage(Component.text(TimeUtils.formatDuration(number * 1000L)));
		player.sendMessage(Component.text(TimeUtils.getRelativeTime(number * 1000L)));


		return 1;
	}
}
