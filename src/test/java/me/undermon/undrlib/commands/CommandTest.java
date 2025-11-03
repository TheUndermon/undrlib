package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandTest {

	private static final CommandSenderDummy SENDER_DUMMY = new CommandSenderDummy();
	private static final String SUBCOMMAND_NAME = "subCommandName";
	static final String FIRST_POSITIONAL = "firstPositional";
	static final String SECOND_POSITIONAL = "secondPositional";
	static final String THIRD_POSITIONAL = "thirdPositional";
	static final String FLAG_A = "myFlagA";
	static final String FLAG_B = "myFlagB";
	static final String SWITCH = "mySwitch";

	static final BiFunction<CommandSender, String, List<String>> NO_COMPLETITIONS = (sender, arg) -> List.of();
	static final Function<CommandSender, Boolean> ALWAYS_AVAILABLE = (sender) -> true;

	private Command command;

	@BeforeEach
	void beforeEach() {
		this.command = new CommandDummy(SUBCOMMAND_NAME);
	}

	@Test
	void exceptionIfNameIsNull() {
		assertThrows(IllegalArgumentException.class, () -> new Command(null) {
			@Override
			protected void onCommand(CommandSender sender, Input arguments) {
				throw new UnsupportedOperationException("Unimplemented method 'onCommand'");
			}
		});
	}

	@Test
	void returnsCorrectName() {
		assertEquals(SUBCOMMAND_NAME, this.command.getName());
	}

	@Test
	void exceptionWhenAddPositionalWithNullName() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addPositional(null));
	}

	@Test
	void exceptionWhenAddPositionalWithNullName2() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addPositional(null, NO_COMPLETITIONS));
	}

	@Test
	void exceptionWhenAddPositionalWithNullSuggester() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addPositional(FIRST_POSITIONAL, null));
	}

	@Test
	void exceptionWhenAddFlagWithNullName() {
		assertThrows(IllegalArgumentException.class, () -> this.command.addFlag(null));
	}

	@Test
	void exceptionWhenAddFlagWithNullName2() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addFlag(null, NO_COMPLETITIONS));
	}

	@Test
	void exceptionWhenAddFlagWithNullName3() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addFlag(null, ALWAYS_AVAILABLE));
	}

	@Test
	void exceptionWhenAddFlagWithNullName4() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addFlag(null, NO_COMPLETITIONS, ALWAYS_AVAILABLE));
	}

	@Test
	void exceptionWhenAddFlagWithNullSuggester() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addFlag(null, null, ALWAYS_AVAILABLE));
	}

	@Test
	void exceptionWhenAddFlagWithNullAvailability() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addFlag(null, NO_COMPLETITIONS, null));
	}

	@Test
	void exceptionWhenAddSwitchWithNullName() {
		assertThrows(IllegalArgumentException.class, () -> this.command.addSwitch(null));
	}

	@Test
	void exceptionWhenAddSwitchWithNullName2() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addSwitch(null, ALWAYS_AVAILABLE));
	}

	@Test
	void exceptionWhenAddSwitchWithNullAvailability() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.addSwitch(SWITCH, null));
	}

	@Test
	void exceptionGetCompletitionWhenSenderIsNull() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.getCompletition(null, new String[] {}));
	}

	@Test
	void exceptionGetCompletitionWhenArgumentsIsNull() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.getCompletition(SENDER_DUMMY, null));
	}

	@Test
	void exceptionWhenAddPositionalWithDuplicatedName() {
		String name = "positional";
		this.command.addPositional(name);

		assertThrows(IllegalArgumentException.class,
				() -> this.command.addPositional(name));
	}

	@Test
	void exceptionWhenAddFlagWithDuplicatedName() {
		String name = "flag";
		this.command.addFlag(name);

		assertThrows(IllegalArgumentException.class,
				() -> this.command.addFlag(name));
	}

	@Test
	void exceptionWhenAddSwitchWithDuplicatedName() {
		String name = "switch";
		this.command.addSwitch(name);
	
		assertThrows(IllegalArgumentException.class,
				() -> this.command.addSwitch(name));
	}
}
