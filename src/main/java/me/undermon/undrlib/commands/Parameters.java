package me.undermon.undrlib.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class Parameters {
	private static final String PREFIX = "-";
	
	private List<String> positionals = new ArrayList<>();
	private Set<String> flags = new HashSet<>();
	private Set<String> switchs = new HashSet<>();

	public String prefix() {
		return PREFIX;
	}

	public boolean has(String name) {
		return this.positionals.contains(name) || this.flags.contains(name) || this.switchs.contains(name);
	}

	public boolean hasPositional(String name) {
		return this.positionals.contains(name);
	}

	public boolean hasFlag(String name) {
		return this.flags.contains(name);
	}

	public boolean hasSwitch(String name) {
		return this.switchs.contains(name);
	}

	public boolean addPositional(String name) {
		if (name != null && !this.has(name)) {
			this.positionals.add(name);

			return true;
		} else {
			
			return false;
		}
	}

	public boolean addFlag(String name) {
		if (name != null && !this.has(name)) {
			return this.flags.add(name);
		} else {
			return false;
		}
	}

	public boolean addSwitch(String name) {
		if (name != null && !this.has(name)) {
			return this.switchs.add(name);
		} else {
			return false;
		}
	}

	public boolean removePositional(String name) {
		return this.positionals.remove(name);
	}

	public boolean removeFlag(String name) {
		return this.flags.remove(name);
	}

	public boolean removeSwitch(String name) {
		return this.switchs.remove(name);
	}

	public List<String> getPositionals() {
		return new ArrayList<>(this.positionals);
	}

	public List<String> getFlags() {
		return new ArrayList<>(this.flags);
	}

	public List<String> getSwitchs() {
		return new ArrayList<>(this.switchs);
	}
}