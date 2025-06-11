package dev.wiji.features.command.models;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public abstract class SubCommand {
	private final String name;
	private LiteralCommandNode<CommandSourceStack> command;

	public SubCommand(String name) {
		this.name = name;
	}

	public abstract int execute(CommandSourceStack source);

	public void build() {
		this.command = Commands.literal(name).executes(context -> execute(context.getSource())).build();
	}

	public String getName() {
		return name;
	}

	public LiteralCommandNode<CommandSourceStack> getBrigadierCommand() {
		return command;
	}
}
