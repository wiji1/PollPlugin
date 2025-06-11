package dev.wiji.features.command.models;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.wiji.features.command.controllers.CommandManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {
	private final String name;
	private final String permission;
	private LiteralCommandNode<CommandSourceStack> command;
	private List<ArgumentBuilder<CommandSourceStack, ?>> arguments;

	public SubCommand(String name, String permission) {
		this.name = name;
		this.permission = permission;
		this.arguments = new ArrayList<>();
	}

	public abstract int execute(CommandSourceStack source, CommandContext<?> context);

	public SubCommand addArgument(String name, ArgumentType<?> type) {
		RequiredArgumentBuilder<CommandSourceStack, ?> argument = Commands.argument(name, type);
		this.arguments.add(argument);
		return this;
	}

	public SubCommand addOptionalArgument(String name, ArgumentType<?> type) {
		ArgumentBuilder<CommandSourceStack, ?> argument = Commands.argument(name, type);
		this.arguments.add(argument);
		return this;
	}

	public SubCommand addArgument(String name, ArgumentType<?> type, SuggestionProvider<CommandSourceStack> suggestionProvider) {
		RequiredArgumentBuilder<CommandSourceStack, ?> argument = Commands.argument(name, type)
				.suggests(suggestionProvider);
		this.arguments.add(argument);
		return this;
	}

	public void build() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(name);

		if (arguments.isEmpty()) {
			builder.executes(context -> {
				CommandSender sender = context.getSource().getSender();
				if (sender instanceof Player player) {
					if (permission != null && !player.hasPermission(permission)) {
						player.sendMessage(CommandManager.NO_PERMISSION_MESSAGE);
						return 0;
					}
				}
				return execute(context.getSource(), context);
			});
		} else {
			ArgumentBuilder<CommandSourceStack, ?> current = null;

			for (int i = arguments.size() - 1; i >= 0; i--) {
				ArgumentBuilder<CommandSourceStack, ?> arg = arguments.get(i);

				if (current == null) {
					current = arg.executes(context -> {
						CommandSender sender = context.getSource().getSender();
						if (sender instanceof Player player) {
							if (permission != null && !player.hasPermission(permission)) {
								player.sendMessage(CommandManager.NO_PERMISSION_MESSAGE);
								return 0;
							}
						}
						return execute(context.getSource(), context);
					});
				} else current = arg.then(current);
			}

			if (current != null) builder.then(current);
		}

		this.command = builder.build();
	}

	public String getName() {
		return name;
	}

	public LiteralCommandNode<CommandSourceStack> getBrigadierCommand() {
		return command;
	}
}