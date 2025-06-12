package dev.wiji.features.command.controllers;

import dev.wiji.features.command.commands.PollCommand;
import dev.wiji.features.command.models.Command;
import dev.wiji.features.command.subcommands.*;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	private static CommandManager instance;
	private List<Command> commands;
	private Commands registrar;

	public static final TextComponent NO_PERMISSION_MESSAGE =
			Component.text("You do not have permission to use this command.",
					NamedTextColor.RED);

	public CommandManager() {
		instance = this;
	}

	public void init(Commands registrar) {
		this.commands = new ArrayList<>();
		this.registrar = registrar;

		registerCommand(new PollCommand()
			.registerSubCommand(new CreateCommand())
			.registerSubCommand(new ListCommand())
			.registerSubCommand(new RemoveCommand())
			.registerSubCommand(new CloseCommand())
			.registerSubCommand(new TestCommand())
			.build()
		);
	}

	public void registerCommand(Command command) {
		this.commands.add(command);
		registrar.register(command.getBrigadierCommand());
	}

	public static CommandManager getInstance() {
		if (instance == null) instance = new CommandManager();
		return instance;
	}

	public List<Command> getCommands() {
		return commands;
	}
}
