/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

public abstract class Command {
	private static final String ARGUMENTS_ARRAY_CANNOT_BE_NULL = "arguments array cannot be null";
	private static final String COMMAND_SENDER_CANNOT_BE_NULL = "CommandSender cannot be null";
	private static final String PARAMETER_NAME_MUST_BE_UNIQUE = "parameter name must be unique";
	private static final String PARAMETER_SUGGESTER_CANNOT_BE_NULL = "parameter suggester cannot be null";
	private static final String PARAMETER_AVAILABILITY_CANNOT_BE_NULL = "parameter availability cannot be null";
	private static final String PARAMETER_NAME_CANNOT_BE_NULL = "parameter name cannot be null";

	private String name;
	private Parameters parameters = new Parameters();
	private final Parser parser = Parser.with(parameters);
	private Map<String, BiFunction<CommandSender, String, List<String>>> suggesters = new HashMap<>();
	private Map<String, Function<CommandSender, Boolean>> availabilities = new HashMap<>();
	private static final BiFunction<CommandSender, String, List<String>> DEFAULT_AUTOCOMPLETER = (sender, arg) -> List.of();
	private static final Function<CommandSender, Boolean> DEFAULT_AVAILABILITY = sender -> true;

	protected Command(String name) {
		if (name == null) {
			throw new IllegalArgumentException("subcommand name cannot be null");
		}
		this.name = name;
	}

	protected final String getName() {
		return this.name;
	}

	protected final boolean hasParameter(String name) {
		return this.parameters.has(name);
	}

	protected final void addPositional(String name, BiFunction<CommandSender, String, List<String>> suggester) {

		if (name == null) {
			throw new IllegalArgumentException(PARAMETER_NAME_CANNOT_BE_NULL);
		}

		if (suggester == null) {
			throw new IllegalArgumentException(PARAMETER_SUGGESTER_CANNOT_BE_NULL);
		}

		if (this.parameters.has(name)) {
			throw new IllegalArgumentException(PARAMETER_NAME_MUST_BE_UNIQUE);
		}

		this.parameters.addPositional(name);
		this.suggesters.put(name, suggester);
		this.availabilities.put(name, DEFAULT_AVAILABILITY);
	}

	protected final void addPositional(String name) {
		this.addPositional(name, DEFAULT_AUTOCOMPLETER);
	}

	protected final void addFlag(String name, BiFunction<CommandSender, String, List<String>> suggester,
		Function<CommandSender, Boolean> availablity) {

		if (name == null) {
			throw new IllegalArgumentException(PARAMETER_NAME_CANNOT_BE_NULL);
		}

		if (availablity == null) {
			throw new IllegalArgumentException(PARAMETER_AVAILABILITY_CANNOT_BE_NULL);
		}

		if (suggester == null) {
			throw new IllegalArgumentException(PARAMETER_SUGGESTER_CANNOT_BE_NULL);
		}

		if (this.parameters.has(name)) {
			throw new IllegalArgumentException(PARAMETER_NAME_MUST_BE_UNIQUE);
		}

		this.parameters.addFlag(name);
		this.suggesters.put(name, suggester);
		this.availabilities.put(name, availablity);
	}

	protected final void addFlag(String name, Function<CommandSender, Boolean> availablity) {
		this.addFlag(name, DEFAULT_AUTOCOMPLETER, availablity);
	}

	protected final void addFlag(String name, BiFunction<CommandSender, String, List<String>> suggester) {
		this.addFlag(name, suggester, DEFAULT_AVAILABILITY);
	}

	protected final void addFlag(String name) {
		this.addFlag(name, DEFAULT_AUTOCOMPLETER, DEFAULT_AVAILABILITY);
	}

	protected final void addSwitch(String name, Function<CommandSender, Boolean> availablity) {
		if (name == null) {
			throw new IllegalArgumentException(PARAMETER_NAME_CANNOT_BE_NULL);
		}

		if (availablity == null) {
			throw new IllegalArgumentException(PARAMETER_AVAILABILITY_CANNOT_BE_NULL);
		}

		if (this.parameters.has(name)) {
			throw new IllegalArgumentException(PARAMETER_NAME_MUST_BE_UNIQUE);
		}

		this.parameters.addSwitch(name);
		this.availabilities.put(name, availablity);
	}

	protected final void addSwitch(String name) {
		this.addSwitch(name, DEFAULT_AVAILABILITY);
	}

	final List<String> getCompletition(CommandSender sender, String[] inputs) {
		
		if (sender == null) {
			throw new IllegalArgumentException(COMMAND_SENDER_CANNOT_BE_NULL);
		}
		
		if (inputs == null) {
			throw new IllegalArgumentException(ARGUMENTS_ARRAY_CANNOT_BE_NULL);
		}

		Parsed parsed = this.parser.parse(Stream.of(inputs).toList());

		Optional<String> possibleActiveParameter = parsed.getActiveArgument();

		if (possibleActiveParameter.isPresent() && shouldSuggestParameter(sender, possibleActiveParameter.get())) {
			String activeParameter = possibleActiveParameter.get();

			String argument = parsed.getPositionalOrFlag(activeParameter).orElse("");

			List<String> suggestions = this.suggesters.get(activeParameter).apply(sender, argument);
			
			return suggestions.stream().
			filter(entry -> entry.toLowerCase().startsWith(argument.toLowerCase())).
			toList();

		} else {
			return getFlagsAndSwitchsCompletitions(sender, inputs, parsed);
		}
	}

	private final List<String> getFlagsAndSwitchsCompletitions(CommandSender sender, String[] inputs, Parsed parsed) {
		List<String> completitions = new ArrayList<>();

		completitions.addAll(parameters.getFlags().stream().
			filter(param -> shouldSuggestParameter(sender, param)).
			toList()
		);

		completitions.addAll(parameters.getSwitchs().stream().
			filter(param -> shouldSuggestParameter(sender, param)).
			toList()
		);

		completitions.removeAll(parsed.getPresent());

		return completitions.stream().
			map(entry -> parameters.prefix() + entry).
			filter(entry -> entry.startsWith((inputs.length - 1 >= 0) ? inputs[inputs.length - 1] : "")).
			toList();
	}

	private final boolean shouldSuggestParameter(CommandSender sender, String name) {
		return this.availabilities.get(name).apply(sender);
	}

	final void execute(CommandSender sender, String[] arguments) {
		if (sender == null) {
			throw new IllegalArgumentException(COMMAND_SENDER_CANNOT_BE_NULL);
		}

		if (arguments == null) {
			throw new IllegalArgumentException(ARGUMENTS_ARRAY_CANNOT_BE_NULL);
		}

		this.onCommand(sender, this.parser.parse(Arrays.asList(arguments)));
	}

	protected abstract void onCommand(CommandSender sender, Inputs arguments);

}
