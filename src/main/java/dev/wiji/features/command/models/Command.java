package dev.wiji.features.command.models;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.wiji.features.command.controllers.CommandManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
	private final String name;
	private final String permission;
	private List<SubCommand> subCommands;
	private LiteralCommandNode<CommandSourceStack> command;

	public Command(String name, String permission) {
		this.name = name;
		this.permission = permission;
		this.subCommands = new ArrayList<>();
	}

	public abstract int execute(CommandSourceStack source);

	public Command build() {
		this.command = Commands.literal(name)
			.executes(context -> {
				CommandSender sender = context.getSource().getSender();

				if (sender instanceof Player player) {
					if (permission != null && !player.hasPermission(permission)) {
						player.sendMessage(CommandManager.NO_PERMISSION_MESSAGE);
						return 0;
					}
				}

				return execute(context.getSource());
			}).build();

		subCommands.forEach(subCommand -> {
			subCommand.build();
			command.addChild(subCommand.getBrigadierCommand());
		});

		return this;
	}

	public Command registerSubCommand(SubCommand subCommand) {
		this.subCommands.add(subCommand);
		return this;
	}



	public String getName() {
		return name;
	}

	public LiteralCommandNode<CommandSourceStack> getBrigadierCommand() {
		return command;
	}
}
