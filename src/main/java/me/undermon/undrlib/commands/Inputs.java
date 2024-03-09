package me.undermon.undrlib.commands;

import java.util.Collection;
import java.util.Optional;

public interface Inputs {

	public Optional<String> getPositional(String name);

	public Optional<String> getFlag(String name);

	public Optional<String> getPositionalOrFlag(String name);

	public boolean getSwitch(String name);

	public Collection<String> getLeftoverPositionals();

	public Collection<String> getUnknownFlags();

}
