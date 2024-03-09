package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParsedActiveArgumentTest {

	@Test
	void noneWhenFlagWithoutArgument() {
		Parameters parameters = new Parameters();
		
		String flag = "firstFlag";

		parameters.addFlag(flag);

		Parsed parsed = Parser.with(parameters).parse(List.of("-" + flag));

		assertTrue(parsed.getActiveArgument().isEmpty());
	}

	@Test
	void noneWhenFlagWithoutArgument2() {
		Parameters parameters = new Parameters();
		

		parameters.addFlag("buy");
		parameters.addSwitch("admin");
		parameters.addPositional("pos");

		Parsed parsed = Parser.with(parameters).parse(List.of("-pos"));

		assertTrue(parsed.getActiveArgument().isEmpty());
	}

	@Test
	void noneWhenFlagWithoutArgument3() {
		Parameters parameters = new Parameters();
		

		parameters.addFlag("buy");
		parameters.addSwitch("admin");
		parameters.addPositional("pos");
		parameters.addPositional("pos2");


		Parsed parsed = Parser.with(parameters).parse(List.of("1", "-pos", ""));

		assertTrue(parsed.getActiveArgument().isEmpty());
	}

	@ParameterizedTest
	@MethodSource
	void noneWhenUnknownFlag(List<String> inputs) {
		Parameters parameters = new Parameters();
		String unknownFlag = "unknownFlag";

		List<String> inputsWithFlag = new ArrayList<>(List.of("-" + unknownFlag));
		inputsWithFlag.addAll(inputs);

		Parsed parsed = Parser.with(parameters).parse(inputsWithFlag);

		assertTrue(parsed.getActiveArgument().isEmpty());
	}

	static Stream<Arguments> noneWhenUnknownFlag() {
		return Stream.of(
			Arguments.of(Named.of("without argument", List.of())),
			Arguments.of(Named.of("with argument", List.of("")))
		);
	}

	@Test
	void noneWhenSwitch() {
		Parameters parameters = new Parameters();
		
		String mySwitch = "switch";

		parameters.addSwitch(mySwitch);

		Parsed parsed = Parser.with(parameters).parse(List.of("-" + mySwitch));

		assertTrue(parsed.getActiveArgument().isEmpty());
	}

	@Test
	void positional() {
		Parameters parameters = new Parameters();
		
		String myPositional = "positional";

		parameters.addPositional(myPositional);

		Parsed parsed = Parser.with(parameters).parse(List.of(""));

		assertEquals(myPositional, parsed.getActiveArgument().get());
	}
}
