package dev.wiji.features.command.controllers;

import dev.wiji.features.command.models.Command;
import dev.wiji.features.command.subcommands.CreateCommand;
import dev.wiji.features.command.subcommands.ListCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	private static CommandManager instance;
	private List<Command> commands;
	private Commands registrar;

	public CommandManager() {
		instance = this;
	}

	public void init(Commands registrar) {
		this.commands = new ArrayList<>();
		this.registrar = registrar;

		registerCommand(new Command("poll")
			.registerSubCommand(new CreateCommand())
			.registerSubCommand(new ListCommand())
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
