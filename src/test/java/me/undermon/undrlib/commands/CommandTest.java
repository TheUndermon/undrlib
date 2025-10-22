package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.undermon.undrlib.commands.Command;
import me.undermon.undrlib.commands.Input;

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
	void doesNotHaveNotAddedParameter() {
		assertFalse(this.command.hasParameter(FIRST_POSITIONAL));
	}

	@Test
	void positionalIsPresentAfterAddingIt() {
		this.command.addPositional(FIRST_POSITIONAL);
		assertTrue(this.command.hasParameter(FIRST_POSITIONAL));
	}

	@Test
	void positionalIsPresentAfterAddingIt2() {
		this.command.addPositional(FIRST_POSITIONAL, NO_COMPLETITIONS);
		assertTrue(this.command.hasParameter(FIRST_POSITIONAL));
	}

	@Test
	void flagIsPresentAfterAddingIt() {
		this.command.addFlag(FLAG_A);
		assertTrue(this.command.hasParameter(FLAG_A));
	}

	@Test
	void flagIsPresentAfterAddingIt2() {
		this.command.addFlag(FLAG_A, ALWAYS_AVAILABLE);
		assertTrue(this.command.hasParameter(FLAG_A));
	}

	@Test
	void flagIsPresentAfterAddingIt3() {
		this.command.addFlag(FLAG_A, NO_COMPLETITIONS);
		assertTrue(this.command.hasParameter(FLAG_A));
	}

	@Test
	void flagIsPresentAfterAddingIt4() {
		this.command.addFlag(FLAG_A, NO_COMPLETITIONS, ALWAYS_AVAILABLE);
		assertTrue(this.command.hasParameter(FLAG_A));
	}


	@Test
	void switchIsPresentAfterAddingIt() {
		this.command.addSwitch(SWITCH);
		assertTrue(this.command.hasParameter(SWITCH));
	}

	@Test
	void switchIsPresentAfterAddingIt2() {
		this.command.addSwitch(SWITCH, ALWAYS_AVAILABLE);
		assertTrue(this.command.hasParameter(SWITCH));
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
	void exceptionOnExecuteWhenSenderIsNull() {
		assertThrows(IllegalArgumentException.class, () -> this.command.execute(null, new String[] {}));
	}

	@Test
	void exceptionOnExecuteWhenArgumentsIsNull() {
		assertThrows(IllegalArgumentException.class,
			() -> this.command.execute(SENDER_DUMMY, null));
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
