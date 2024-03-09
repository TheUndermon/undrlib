package me.undermon.undrlib.commands;

import org.bukkit.command.CommandSender;

final class CommandDummy extends Command {

	protected CommandDummy(String name) {
		super(name);
	}

	@Override
	protected void onCommand(CommandSender sender, Inputs arguments) {
		throw new UnsupportedOperationException("Unimplemented method 'onCommand'");
	}
	
}
