package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParserTest {
	private static final String SWITCH = "mySwitch";
	private static final String POSITIONAL = "myPositional";
	private static final String FLAG = "myFlag";

	@Test
	void exceptionIfParametersIsNull() {
		assertThrows(IllegalArgumentException.class, () -> Parser.with(null));		
	}

	@Test
	void exceptionIfInputArgumentIsNull() {
		Parser parser = Parser.with(new Parameters());
		assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
	}

	@Test
	void isNotEqualToNull() {
		assertNotEquals(null, Parser.with(new Parameters()));	
	}

	@Test
	void twoParametersNotAreEqual() {
		Parameters parameters = new Parameters();
		parameters.addPositional(POSITIONAL);
		parameters.addFlag(FLAG);
		parameters.addSwitch(SWITCH);

		Parameters other = new Parameters();
		other.addPositional(POSITIONAL);
		other.addFlag(FLAG);

		assertNotEquals(Parser.with(other), Parser.with(parameters));
	}

	@Test
	void twoParametersHashCodeNotAreEqual() {
		Parameters parameters = new Parameters();
		parameters.addPositional(POSITIONAL);
		parameters.addFlag(FLAG);
		parameters.addSwitch(SWITCH);

		Parameters other = new Parameters();
		other.addPositional(POSITIONAL);
		other.addFlag(FLAG);

		assertNotEquals(Parser.with(other).hashCode(), Parser.with(parameters).hashCode());
	}
}
