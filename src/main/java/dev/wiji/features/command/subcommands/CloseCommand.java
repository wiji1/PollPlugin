package dev.wiji.features.command.subcommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.wiji.features.command.enums.Permission;
import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.enums.PollStatus;
import dev.wiji.features.poll.models.Poll;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CloseCommand extends SubCommand {

	public CloseCommand() {
		super("close", Permission.ADMIN.get());
		addOptionalArgument("pollId", IntegerArgumentType.integer());
	}

	@Override
	public int execute(CommandSourceStack source, CommandContext<?> context) {

		int pollId = -1;
		try {
			pollId = IntegerArgumentType.getInteger(context, "pollId");
		} catch (IllegalArgumentException ignored) { }

		if (pollId < 1 || pollId > getTotalPolls()) {
			source.getSender().sendMessage(Component.text("Invalid poll ID. Please provide a valid ID.", NamedTextColor.RED));
			return 0;
		}

		Poll poll = PollManager.getInstance().getPollById(pollId);
		if (poll == null) {
			source.getSender().sendMessage(Component.text("Poll with ID " + pollId + " does not exist.", NamedTextColor.RED));
			return 0;
		}

		if (poll.getStatus() != PollStatus.ACTIVE) {
			source.getSender().sendMessage(Component.text("Poll with ID " + pollId + " is already closed.", NamedTextColor.RED));
			return 0;
		}

		PollManager.getInstance().closePoll(pollId);

		source.getSender().sendMessage(Component.text("Poll with ID " + pollId + " has been closed successfully.", NamedTextColor.GREEN));

		return 1;
	}

	public int getTotalPolls() {
		return PollManager.getInstance().getPolls().size();
	}
}
