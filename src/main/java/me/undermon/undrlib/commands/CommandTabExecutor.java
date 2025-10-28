/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CommandTabExecutor implements TabExecutor {
	private static final String SUBCOMMAND_CANNOT_BE_NULL = "subcommand cannot be null";
	private Map<String, Command> commands = new HashMap<>();
	private Command defaultCommand;

	protected void add(Command command) {
		if (command == null) {
			throw new IllegalArgumentException(SUBCOMMAND_CANNOT_BE_NULL);
		}

		String name = command.getName();

		if (this.commands.containsKey(name)) {
			throw new IllegalArgumentException("Duplicated subcommand name: " + name);
		}

		this.commands.put(name, command);
	}

	protected void setDefault(Command command) {
		if (command == null) {
			throw new IllegalArgumentException(SUBCOMMAND_CANNOT_BE_NULL);
		}

		this.defaultCommand = command;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		String firstElement = args[0];

		if (this.commands.containsKey(firstElement) && args.length > 1 && this.commands.get(firstElement).predicate(sender)) {
			return this.commands.get(firstElement).getCompletition(sender, skipFirstElementOf(args));
		}

		if (this.defaultCommand != null) {
			List<String> completitions = new ArrayList<>();

			if (this.defaultCommand.predicate(sender)) {
				completitions.addAll(this.defaultCommand.getCompletition(sender, args));
			}

			if (args.length <= 1) {
				completitions.addAll(
					this.commands.keySet().stream().
					filter(entry -> this.commands.get(entry).predicate(sender)).
					toList()
				);
			}

			completitions.removeIf(entry -> !entry.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));

			return completitions; 
		}

		if (args.length > 1) {
			return List.of();
		} else {
			return this.commands.keySet().stream().
				filter(arg -> this.commands.get(arg).predicate(sender)).
				filter(arg -> arg.toLowerCase().startsWith(firstElement)).
				toList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		String subcommand = (args.length > 0) ? args[0] : "";

		boolean isSubcommandAvailable = this.commands.get(subcommand).predicate(sender);

		if (this.commands.containsKey(subcommand) && isSubcommandAvailable) {
			this.commands.get(subcommand).execute(sender, skipFirstElementOf(args));

			return true;
		}

		if (this.defaultCommand != null && this.defaultCommand.predicate(sender)) {
			this.defaultCommand.execute(sender, args);
			
			return true;
		}

		this.onFailure(sender);

		return true;
	}

	private static String[] skipFirstElementOf(String[] args) {
		return Stream.of(args).skip(1).toArray(String[]::new);
	}

	protected abstract void onFailure(CommandSender sender);
}
