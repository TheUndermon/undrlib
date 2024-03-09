package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CommandTabExecutor implements TabExecutor {
	private static final String ARGUMENT_CANNOT_BE_NULL = "argument cannot be null";
	private static final String SUBCOMMAND_CANNOT_BE_NULL = "subcommand cannot be null";
	private Map<String, Command> commands = new HashMap<>();
	private Command defaultCommand;
	private Map<String, Function<CommandSender, Boolean>> commandAvailability = new HashMap<>();
	private static final Function<CommandSender, Boolean> DEFAULT_AVAILABILITY = sender -> true;


	protected void add(Command command, Function<CommandSender, Boolean> availability) {
		if (command == null) {
			throw new IllegalArgumentException(SUBCOMMAND_CANNOT_BE_NULL);
		}

		if (availability == null) {
			throw new IllegalArgumentException(ARGUMENT_CANNOT_BE_NULL);
		}

		String name = command.getName();

		if (this.commands.containsKey(name)) {
			throw new IllegalArgumentException("Duplicated subcommand name: " + name);
		}

		this.commands.put(name, command);
		this.commandAvailability.put(name, availability);
	}

	protected void add(Command command)  {
		this.add(command, DEFAULT_AVAILABILITY);
	}

	protected void setDefault(Command command, Function<CommandSender, Boolean> availability) {
		if (command == null) {
			throw new IllegalArgumentException(SUBCOMMAND_CANNOT_BE_NULL);
		}

		if (availability == null) {
			throw new IllegalArgumentException(ARGUMENT_CANNOT_BE_NULL);
		}

		this.defaultCommand = command;
		this.commandAvailability.put(command.getName(), availability);
	}

	protected void setDefault(Command subcommand) {
		this.setDefault(subcommand, DEFAULT_AVAILABILITY);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		String firstElement = args[0];

		if (this.commands.containsKey(firstElement) && args.length > 1 && Boolean.TRUE.equals(this.commandAvailability.get(firstElement).apply(sender))) {
			return this.commands.get(firstElement).getCompletition(sender, skipFirstElementOf(args));
		}

		if (this.defaultCommand != null) {
			List<String> completitions = new ArrayList<>();

			if (Boolean.TRUE.equals(this.commandAvailability.get(this.defaultCommand.getName()).apply(sender))) {
				completitions.addAll(this.defaultCommand.getCompletition(sender, args));
			}

			if (args.length <= 1) {
				completitions.addAll(
					this.commands.keySet().stream().
					filter(entry -> Boolean.TRUE.equals(this.commandAvailability.get(entry).apply(sender))).
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
				filter(arg -> this.commandAvailability.get(arg).apply(sender)).
				filter(arg -> arg.toLowerCase().startsWith(firstElement)).
				toList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		String subcommand = (args.length > 0) ? args[0] : "";

		boolean isSubcommandAvailable = this.commandAvailability.getOrDefault(subcommand, DEFAULT_AVAILABILITY).apply(sender);

		if (this.commands.containsKey(subcommand) && isSubcommandAvailable) {
			this.commands.get(subcommand).execute(sender, skipFirstElementOf(args));

			return true;
		}

		if (this.defaultCommand != null && this.commandAvailability.get(this.defaultCommand.getName()).apply(sender)) {
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
