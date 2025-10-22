/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class Parsed implements Input {
	
	private Map<String, String> positionals = new HashMap<>();
	private Map<String, String> flags = new HashMap<>();
	private List<String> switchs = new ArrayList<>();
	
	private List<String> present = new ArrayList<>();

	private List<String> unknownPositionals = new ArrayList<>();
	private List<String> unknownFlagsAndSwitchs = new ArrayList<>();

	private String activeParameter;

	Parsed(Parameters parameters, List<Argument> arguments) {

		arguments.forEach(input -> {
			populatePositionalInternals(parameters, input);
			popolateFlagInternals(parameters, input);
			populateSwitchInternals(parameters, input);
		});
		
		this.activeParameter = this.activeArgument(parameters, arguments);
	}

	private void populatePositionalInternals(Parameters parameters, Argument argument) {
		if (argument.type().equals(Argument.Type.POSITIONAL)) {
			if (parameters.hasPositional(argument.name())) {
				this.positionals.put(argument.name(), argument.argument());
				this.present.add(argument.name());
			} else {
				this.unknownPositionals.add(argument.argument());
			}		
		}
	}

	private void popolateFlagInternals(Parameters parameters, Argument argument) {
		if (argument.type().equals(Argument.Type.FLAG)) {
			if (parameters.hasFlag(argument.name())) {
				this.flags.put(argument.name(), argument.argument());
				this.present.add(argument.name());
			} else {
				this.unknownFlagsAndSwitchs.add(argument.name());
			}
		}
	}

	private void populateSwitchInternals(Parameters parameters, Argument argument) {
		if (argument.type().equals(Argument.Type.SWITCH)) {
			if (parameters.hasSwitch(argument.name())) {
				this.switchs.add(argument.name());
				this.present.add(argument.name());
			} else {
				this.unknownFlagsAndSwitchs.add(argument.name());
			}			
		}
	}

	private String activeArgument(Parameters parameters, List<Argument> arguments) {
		if (arguments.isEmpty()) {
			return null;
		}

		Argument last = arguments.get(arguments.size() - 1);

		if (last.name() == null) {
			return null;
		}

		if (last.type().equals(Argument.Type.SWITCH)) {
			return null;
		}

		if (last.type().equals(Argument.Type.FLAG) && !parameters.hasFlag(last.name())) {
			return null;
		}

		if (last.type().equals(Argument.Type.FLAG) && last.argument() == null) {
			return null;
		}

		return last.name();
	}

	public List<String> getPresent() {
		return present;
	}

	@Override
	public Optional<String> getPositional(String name) {
		Objects.requireNonNull(name);

		String argument = this.positionals.get(name);

		if (argument != null) {
			return Optional.of(argument);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<String> getFlag(String name) {
		Objects.requireNonNull(name);

		String argument = this.flags.get(name);

		if (argument != null) {
			return Optional.of(argument);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<String> getPositionalOrFlag(String name) {
		Objects.requireNonNull(name);

		String positional = null;
		String flag = null;

		positional = this.positionals.get(name);
		flag = this.flags.get(name);

		if (positional != null) {
			return Optional.of(positional);
		} else if (flag != null) {
			return Optional.of(flag);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public boolean getSwitch(String name) {
		return this.switchs.contains(name);
	}

	@Override
	public Collection<String> getLeftoverPositionals() {
		return new ArrayList<>(this.unknownPositionals);
	}

	@Override
	public Collection<String> getUnknownFlags() {
		return new ArrayList<>(this.unknownFlagsAndSwitchs);
	}

	public Optional<String> getActiveArgument() {
		return Optional.ofNullable(this.activeParameter);
	}
}
