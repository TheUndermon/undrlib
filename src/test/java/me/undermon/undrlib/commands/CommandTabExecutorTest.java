package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.undermon.undrlib.commands.Command;
import me.undermon.undrlib.commands.CommandTabExecutor;
import me.undermon.undrlib.commands.Inputs;

class CommandTabExecutorTest {
	private CommandTabExecutor tabExecutor;
	
	private static final Command SUBCOMMAND = new Command("mySubcommand") {
		@Override
		protected void onCommand(CommandSender sender, Inputs arguments) {
			throw new UnsupportedOperationException("Unimplemented method 'onCommand'");
		}
	};

	@BeforeEach
	void BeforeEach() {
		this.tabExecutor = new CommandTabExecutor() {
			@Override
			protected void onFailure(CommandSender sender) {
				throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
			}			
		};
	}

	@Test
	void exceptionAddSubcommandWithNullSubcommandArgument() {
		assertThrows(IllegalArgumentException.class, () -> this.tabExecutor.add(null));
	}

	@Test
	void exceptionAddSubcommandWithNullSubcommandArgumentOverloaded() {
		assertThrows(IllegalArgumentException.class, () -> this.tabExecutor.add(null, sender -> true));
	}

	@Test
	void exceptionAddSubcommandWithNullAvailabilityArgumentOverloaded() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.tabExecutor.add(SUBCOMMAND, null);
		});
	}

	@Test
	void exceptionAddSubcommandWithDuplicatedName() {
		this.tabExecutor.add(SUBCOMMAND);

		assertThrows(IllegalArgumentException.class, () -> {
			this.tabExecutor.add(SUBCOMMAND, null);
		});
	}

	@Test
	void exceptionSetDefaultSubcommandWithNullSubcommandArgument() {
		assertThrows(IllegalArgumentException.class, () -> this.tabExecutor.setDefault(null));
	}

	@Test
	void exceptionSetDefaultSubcommandWithNullSubcommandArgumentOverloaded() {
		assertThrows(IllegalArgumentException.class,
			() -> this.tabExecutor.setDefault(null, sender -> true));
	}

	@Test
	void exceptionSetDefaultSubcommandWithNullAvailabilityArgumentOverloaded() {	
		assertThrows(IllegalArgumentException.class, () -> {
			this.tabExecutor.setDefault(SUBCOMMAND, null);
		});
	}
}
