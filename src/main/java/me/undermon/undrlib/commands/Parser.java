package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.List;

final class Parser {

	private Parameters parameters;

	private Parser(Parameters parameters) {
		this.parameters = parameters;
	}

	public static Parser with(Parameters parameters) {
		if (parameters == null) {
			throw new IllegalArgumentException();
		}

		return new Parser(parameters);
	}

	public Parsed parse(List<String> inputs) {
		if (inputs == null || parameters == null) {
			throw new IllegalArgumentException();
		}

		List<Argument> parsed = getArguments(inputs.toArray(String[]::new), parameters);

		return new Parsed(parameters, parsed);
	}

	private List<Argument> getArguments(String[] inputs, Parameters parameters) {
		List<Argument> arguments = new ArrayList<>();

		int currentPositional = 0;

		for (int i = 0; i < inputs.length; i++) {
			String current = inputs[i];
			boolean hasNext = i + 1 < inputs.length;
			boolean hasPrevious = i - 1 >= 0;
			String next = (hasNext) ? inputs[i + 1] : null;
			
			boolean currentInputIsFlagOrSwitch = current.startsWith(parameters.prefix());
			boolean currentInputIsNamedArgument = hasPrevious && (inputs[i - 1].startsWith(parameters.prefix()) && !parameters.hasSwitch(inputs[i - 1].replace("-", "")));

			if (currentInputIsFlagOrSwitch) {
				parseFlagOrSwitch(parameters, arguments, current, hasNext, next);
			} else if (!currentInputIsNamedArgument) {
				currentPositional = parsePositional(parameters, arguments, currentPositional, current);
			}
		}

		return arguments;
	}

	private void parseFlagOrSwitch(Parameters parameters, List<Argument> arguments, String current, boolean hasNext,
			String next) {
		String nameNoPrefix = current.replace(parameters.prefix(), "");
		boolean isSwitch = parameters.hasSwitch(nameNoPrefix);

		if (isSwitch) {
			arguments.add(Argument.switchFlag(nameNoPrefix));
		} else {
			String flagArgument = null;

			if (hasNext && !next.startsWith(parameters.prefix())) {
				flagArgument = next;
			}

			arguments.add(Argument.flag(nameNoPrefix,  flagArgument));
		}
	}

	private int parsePositional(Parameters parameters, List<Argument> arguments, int currentPositional,
			String currentInput) {
		boolean hasPositionalParameter = currentPositional <=  parameters.getPositionals().size() - 1;
		String positionalName = null;

		if (hasPositionalParameter) {
			positionalName = parameters.getPositionals().get(currentPositional);
			currentPositional++;
		}

		arguments.add(Argument.positional(positionalName, currentInput));
		return currentPositional;
	}
}
