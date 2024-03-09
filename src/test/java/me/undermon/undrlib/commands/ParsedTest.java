package me.undermon.undrlib.commands;

import static me.undermon.undrlib.commands.CustomAssertions.assertEqualsIgnoreOrdering;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import me.undermon.undrlib.commands.Parameters;
import me.undermon.undrlib.commands.Parsed;
import me.undermon.undrlib.commands.Parser;

class ParsedTest {

	@Test
	void positionalArgumentIsPresentWhenArgumentIsInputed() {
		Parameters parameters = new Parameters();
		
		String positional = "myPositional";
		parameters.addPositional(positional);
		String expected = "123";
		Parsed parsed = Parser.with(parameters).parse(List.of(expected));
	
		assertEquals(expected, parsed.getPositional(positional).get());
	}

	@Test
	void positionalArgumentIsPresentWhenArgumentIsInputed2() {
		Parameters parameters = new Parameters();
		
		String positional = "myPositional";
		parameters.addPositional(positional);
		String expected = "123";
		Parsed parsed = Parser.with(parameters).parse(List.of(expected));

		assertEquals(expected, parsed.getPositionalOrFlag(positional).get());
	}

	@Test
	void flagArgumentIsPresentWhenArgumentIsPresent() {
		Parameters parameters = new Parameters();
		
		String flag = "myFlag";
		parameters.addFlag(flag);
		String expected = "123";
		Parsed parsed = Parser.with(parameters).parse(List.of("-" + flag, expected));

		assertEquals(expected, parsed.getFlag(flag).get());
	}

	@Test
	void flagArgumentIsPresentWhenArgumentIsPresent2() {
		Parameters parameters = new Parameters();

		String flag = "myFlag";
		parameters.addFlag(flag);
		String expected = "123";
		Parsed parsed = Parser.with(parameters).parse(List.of("-" + flag, expected));

		assertEquals(expected, parsed.getPositionalOrFlag(flag).get());
	}

	@Test
	void flagHasNoArgumentWhenNoneIsInputed() {
		Parameters parameters = new Parameters();
		
		String flag = "myFlag";
		parameters.addFlag(flag);
		Parsed parsed = Parser.with(parameters).parse(List.of("-" + flag));

		assertTrue(parsed.getFlag(flag).isEmpty());
	}

 	@Test
	void absentFlagHasNoArgument() {
		Parameters parameters = new Parameters();
		
		String flag = "myFlag";
		parameters.addFlag(flag);
		Parsed parsed = Parser.with(parameters).parse(List.of());

		assertTrue(parsed.getFlag(flag).isEmpty());
	}

 	@Test
	void absentSwitchIsFalse() {
		Parameters parameters = new Parameters();
		
		String mySwitch = "mySwitch";
		parameters.addSwitch(mySwitch);
		Parsed parsed = Parser.with(parameters).parse(List.of());

		assertFalse(parsed.getSwitch(mySwitch));
	}

 	@Test
	void presentSwitchIsTrue() {
		Parameters parameters = new Parameters();
		
		String mySwitch = "mySwitch";
		parameters.addSwitch(mySwitch);
		Parsed parsed = Parser.with(parameters).parse(List.of("-" + mySwitch));

		assertTrue(parsed.getSwitch(mySwitch));
	}

 	@Test
	void unknownSwitchIsFalse() {
		Parameters parameters = new Parameters();
		
		String mySwitch = "mySwitch";
		Parsed parsed = Parser.with(parameters).parse(List.of("-" + mySwitch));

		assertFalse(parsed.getSwitch(mySwitch));
	}


	@Test
	void inputedUnrecognizedPositionalIsUnknown() {
		Parameters parameters = new Parameters();
		
		String unknownPositional = "unknownPositional";

		Parsed parsed = Parser.with(parameters).parse(List.of(unknownPositional));

		assertEqualsIgnoreOrdering(List.of(unknownPositional), parsed.getLeftoverPositionals());
	}


	@Test
	void inputedUnrecognizedFlagOrSwitchIsUnknown() {
		Parameters parameters = new Parameters();
		
		String unknownFlag = "unknownFlag";

		Parsed parsed = Parser.with(parameters).parse(List.of("-" + unknownFlag));

		assertEqualsIgnoreOrdering(List.of(unknownFlag), parsed.getUnknownFlags());
	}

	@Test
	void unrecognizedFlagArgumentIsEmpty() {
		Parameters parameters = new Parameters();
		
		String unknownFlag = "unknownFlag";

		Parsed parsed = Parser.with(parameters).parse(List.of("-" + unknownFlag, "flagArgument"));

		assertTrue(parsed.getFlag(unknownFlag).isEmpty());
	}
}
