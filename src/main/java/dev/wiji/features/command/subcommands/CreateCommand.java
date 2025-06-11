package dev.wiji.features.command.subcommands;

import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class CreateCommand extends SubCommand {
	public CreateCommand() {
		super("create");
	}

	@Override
	public int execute(CommandSourceStack source) {

		source.getSender().sendMessage(Component.text("Creating a new poll..."));

		TextComponent question = Component.text("Whats your favorite color?").color(TextColor.color(0, 0, 100));

		PollResponse response1 = new PollResponse(
				Component.text("Red").color(TextColor.color(255, 255, 255))
		);

		PollResponse response2 = new PollResponse(
				Component.text("Green").color(TextColor.color(0, 255, 0))
		);

		PollResponse response3 = new PollResponse(
				Component.text("Blue").color(TextColor.color(0, 0, 255))
		);

		Poll poll = new Poll(
				question,
				new PollResponse[]{response1, response2, response3}
		);

		PollManager.getInstance().addPoll(poll);

		return 1;
	}
}
