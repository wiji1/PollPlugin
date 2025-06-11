package dev.wiji.features.command.models;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.ArrayList;
import java.util.List;

public class Command {
	private final String name;
	private List<SubCommand> subCommands;
	private LiteralCommandNode<CommandSourceStack> command;

	public Command(String name) {
		this.name = name;
		this.subCommands = new ArrayList<>();
	}

	public Command build() {
		this.command = Commands.literal(name).build();

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
