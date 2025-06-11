package dev.wiji.features.command.subcommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.wiji.features.command.enums.Permission;
import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Collections;
import java.util.List;

public class ListCommand extends SubCommand {

	private static final int POLLS_PER_PAGE = 5;
	private static final int SEPARATOR_LENGTH = 60;
	private static final Component SEPARATOR_LINE = Component.text(String.join("",
			Collections.nCopies(SEPARATOR_LENGTH, " ")),
			NamedTextColor.GRAY).decorate(TextDecoration.STRIKETHROUGH);


	public ListCommand() {
		super("list", Permission.ADMIN.get());
		addOptionalArgument("page", IntegerArgumentType.integer());
	}

	@Override
	public int execute(CommandSourceStack source, CommandContext<?> context) {
		List<Poll> polls = PollManager.getInstance().getAllPollsSorted();

		if (polls.isEmpty()) {
			source.getSender().sendMessage(Component.text("No polls currently exist.", NamedTextColor.RED));
			return 0;
		}

		int page = 1;
		try {
			page = IntegerArgumentType.getInteger(context, "page");
			if (page < 1) page = 1;
		} catch (IllegalArgumentException ignored) { }

		int totalPages = (int) Math.ceil((double) polls.size() / POLLS_PER_PAGE);

		if (page > totalPages) page = totalPages;

		int startIndex = (page - 1) * POLLS_PER_PAGE;
		int endIndex = Math.min(startIndex + POLLS_PER_PAGE, polls.size());

		List<Poll> pollsOnPage = polls.subList(startIndex, endIndex);

		source.getSender().sendMessage(SEPARATOR_LINE);
		source.getSender().sendMessage(Component.text("Polls List (Page ", NamedTextColor.GRAY)
				.append(Component.text(page, NamedTextColor.AQUA))
				.append(Component.text(" / ", NamedTextColor.GRAY))
				.append(Component.text(totalPages, NamedTextColor.AQUA))
				.append(Component.text(")", NamedTextColor.GRAY)));
		source.getSender().sendMessage(SEPARATOR_LINE);

		for (Poll poll : pollsOnPage) {
			int id = poll.getSequentialId();
			Component question = poll.getQuestion();
			Component status = poll.getStatus().getDisplayName();

			source.getSender().sendMessage(Component.text("ID: ", NamedTextColor.GOLD)
					.append(Component.text(id, NamedTextColor.YELLOW))
					.append(Component.text(" | Question: ", NamedTextColor.GOLD))
					.append(question.color(NamedTextColor.WHITE))
					.append(Component.text(" | Status: ", NamedTextColor.GOLD))
					.append(status));
		}

		source.getSender().sendMessage(SEPARATOR_LINE);

		return 1;
	}
}
