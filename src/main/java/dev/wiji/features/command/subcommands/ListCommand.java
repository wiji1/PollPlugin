package dev.wiji.features.command.subcommands;

import dev.wiji.features.command.models.SubCommand;
import dev.wiji.features.poll.controllers.PollManager;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.poll.models.PollResponse;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends SubCommand {
	public ListCommand() {
		super("list");
	}

	@Override
	public int execute(CommandSourceStack source) {
		List<Poll> polls = PollManager.getInstance().getPolls();

		CommandSender sender = source.getSender();

		polls.forEach(poll -> {
			sender.sendMessage("---------------------------------------------");
			sender.sendMessage(poll.getQuestion());
			for(PollResponse response : poll.getResponses()) {
				sender.sendMessage(response.getText());
			}
			sender.sendMessage("---------------------------------------------");
		});

		return 1;
	}
}
