package me.undermon.undrlib.commands;

import static me.undermon.undrlib.commands.CustomAssertions.assertEqualsIgnoreOrdering;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class CommandCompletitionTest {
	
	private static final CommandSenderDummy SENDER_DUMMY = new CommandSenderDummy();
	private CommandDummy command;

	@BeforeEach
	void beforeEach() {
		this.command = new CommandDummy("subcommandName");
	}

	@ParameterizedTest
	@MethodSource
	void hasNoCompletitionsWhenHasNoParameters(List<String> inputs) {
		assertTrue(this.command.getCompletition(SENDER_DUMMY, inputs.toArray(String[]::new)).isEmpty());
	}

	static Stream<Arguments> hasNoCompletitionsWhenHasNoParameters() {
		return Stream.of(
			Arguments.of(List.of()),
			Arguments.of(List.of("")),
			Arguments.of(List.of("-myFlag", ""))
		);
	}

	@Test
	void blankPositionalArgumentHasAllCompletitions() {
		List<String> completitions = List.of("car", "carnage", "toronto", "torment");

		this.command.addPositional("myPositional", (sender, arg) -> completitions);

		List<String> actual = this.command.getCompletition(SENDER_DUMMY, new String[] {""});

		assertEqualsIgnoreOrdering(completitions, actual);
	}

	@Test
	void blankFlagArgumentHasAllCompletitions() {
		List<String> completitions = List.of("car", "carnage", "toronto", "torment");

		this.command.addFlag("myFlag", (sender, arg) -> completitions);

		List<String> actual = this.command.getCompletition(SENDER_DUMMY, new String[] {"-myFlag", ""});

		assertEqualsIgnoreOrdering(completitions, actual);
	}

	@Test
	void partialPositionalArgumentHasOnlyPartiallyMatchedCompletitions() {
		List<String> completitions = List.of("car", "carnage", "toronto", "torment");

		this.command.addPositional("myPositional", (sender, arg) -> completitions);

		List<String> actual = this.command.getCompletition(SENDER_DUMMY, new String[] {"car"});

		assertEqualsIgnoreOrdering(List.of("car", "carnage"), actual);
	}

	@Test
	void partialFlagArgumentHasOnlyPartiallyMatchedCompletitions() {
		List<String> completitions = List.of("car", "carnage", "toronto", "torment");

		this.command.addFlag("myFlag", (sender, arg) -> completitions);

		List<String> actual = this.command.getCompletition(SENDER_DUMMY, new String[] {"-myFlag", "car"});

		assertEqualsIgnoreOrdering(List.of("car", "carnage"), actual);
	}

	@Test
	void switchDoesNotHaveCompletitions() {
		String[] inputs = new String[] {"-mySwitch", ""};
		List<String> expected = List.of();

		this.command.addSwitch("mySwitch");

		assertEqualsIgnoreOrdering(expected, this.command.getCompletition(SENDER_DUMMY, inputs));
	}

	@ParameterizedTest
	@MethodSource
	void suggestsFlagsAndSwitchsParametersWhenPositionalsAreExausted(List<String> inputs, List<String> expected) {
		this.command.addFlag("myFlag");
		this.command.addSwitch("mySwitch");

		assertEqualsIgnoreOrdering(expected, this.command.getCompletition(SENDER_DUMMY, inputs.toArray(String[]::new)));
	}

	static Stream<Arguments> suggestsFlagsAndSwitchsParametersWhenPositionalsAreExausted() {
		return Stream.of(
			Arguments.of(List.of(""), List.of("-mySwitch", "-myFlag")),
			Arguments.of(List.of("-unknown"), List.of()),
			Arguments.of(List.of("-myF"), List.of("-myFlag"))
		);
	}

	@ParameterizedTest
	@MethodSource
	void suggestsFlagsAndSwitchsParametersWhenPositionalsAreNotExausted(List<String> inputs, List<String> expected) {
		this.command.addPositional("myPositional", (sender, arg) -> List.of("a", "b", "c"));
		this.command.addFlag("myFlag");
		this.command.addSwitch("mySwitch");

		assertEqualsIgnoreOrdering(expected, this.command.getCompletition(SENDER_DUMMY, inputs.toArray(String[]::new)));
	}

	static Stream<Arguments> suggestsFlagsAndSwitchsParametersWhenPositionalsAreNotExausted() {
		return Stream.of(
			Arguments.of(List.of("-"), List.of("-mySwitch", "-myFlag")),
			Arguments.of(List.of("-myPositional"), List.of()),
			Arguments.of(List.of("-m"), List.of("-mySwitch", "-myFlag")),
			Arguments.of(List.of("-myF"), List.of("-myFlag"))
		);
	}

	@Test
	void flagNotAvailableIsNotSuggested() {
		String[] inputs = new String[] {""};
		
		this.command.addFlag("myFlag", sender -> true);
		this.command.addFlag("myNotAvailableFlag", sender -> false);

		assertEqualsIgnoreOrdering(List.of("-myFlag"), this.command.getCompletition(SENDER_DUMMY, inputs));
	}

	@Test
	void switchNotAvailableIsNotSuggested() {
		String[] inputs = new String[] {""};
		
		this.command.addSwitch("mySwitch", sender -> true);
		this.command.addSwitch("myNotAvailableSwitch", sender -> false);

		assertEqualsIgnoreOrdering(List.of("-mySwitch"), this.command.getCompletition(SENDER_DUMMY, inputs));
	}
}
