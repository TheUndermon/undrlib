package me.undermon.undrlib.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.undermon.undrlib.commands.Parameters;

class ParametersTest {
	private static final String SWITCH = "mySwitch";
	private static final String POSITIONAL = "myPositional";
	private static final String FLAG = "myFlag";
	private Parameters parameters;

	@BeforeEach
	void beforeEach() {
		this.parameters = new Parameters();
	}

	@Test
	void AddFlagReturnsTrue() {
		assertTrue(this.parameters.addFlag(FLAG));
	}

	@Test
	void AddNullPositionalReturnsFalse() {
		assertFalse(this.parameters.addPositional(null));
	}

	@Test
	void AddNullFlagReturnsFalse() {
		assertFalse(this.parameters.addFlag(null));
	}

	@Test
	void AddNullSwitchReturnsFalse() {
		assertFalse(this.parameters.addSwitch(null));
	}

	@Test
	void AddDuplicatedFlagReturnsFalse() {
		this.parameters.addFlag(FLAG);

		assertFalse(this.parameters.addFlag(FLAG));
	}

	@Test
	void AddSwitchReturnsTrue() {
		assertTrue(this.parameters.addSwitch(SWITCH));
	}

	@Test
	void AddDuplicatedSwitchReturnsFalse() {
		String switchName = SWITCH;
		this.parameters.addSwitch(switchName);

		assertFalse(this.parameters.addSwitch(switchName));
	}

	@Test
	void AddPositionalReturnsTrue() {
		assertTrue(this.parameters.addPositional(POSITIONAL));
	}

	@Test
	void AddDuplicatedPositionalReturnsFalse() {
		String positionalName = SWITCH;
		this.parameters.addPositional(positionalName);

		assertFalse(this.parameters.addPositional(positionalName));
	}

	@Test
	void emptyParametersDoesNotHaveParameter() {
		assertFalse(this.parameters.has("myParameter"));
	}

	@Test
	void emptyParametersDoesNotHavePositional() {
		assertFalse(this.parameters.hasPositional(POSITIONAL));
	}

	@Test
	void emptyParametersDoesNotHaveFlag() {
		assertFalse(this.parameters.hasFlag(FLAG));
	}

	@Test
	void hasPositional() {
		this.parameters.addPositional(POSITIONAL);
		assertTrue(this.parameters.hasPositional(POSITIONAL));
	}

	@Test
	void hasFlag() {
		this.parameters.addFlag(FLAG);
		assertTrue(this.parameters.hasFlag(FLAG));
	}

	@Test
	void hasSwitch() {
		this.parameters.addSwitch(SWITCH);
		assertTrue(this.parameters.has(SWITCH));
	}

	@Test
	void removingRegisteredPositionalReturnsTrue() {
		this.parameters.addPositional(POSITIONAL);

		assertTrue(this.parameters.removePositional(POSITIONAL));
	}

	@Test
	void removingRegisteredFlagReturnsTrue() {
		this.parameters.addFlag(FLAG);

		assertTrue(this.parameters.removeFlag(FLAG));
	}

	@Test
	void removingRegisteredSwitchReturnsTrue() {
		this.parameters.addSwitch(SWITCH);

		assertTrue(this.parameters.removeSwitch(SWITCH));
	}

	@Test
	void getEmptyPositionalsNonePositionalsRegistered() {
		assertTrue(this.parameters.getPositionals().isEmpty());
	}

	@Test
	void getEmptyFlagsWhenNoneRegistered() {
		assertTrue(this.parameters.getFlags().isEmpty());
	}

	@Test
	void getEmptySwitchsWhenNoneRegistered() {
		assertTrue(this.parameters.getSwitchs().isEmpty());
	}

	@Test
	void containsRegisteredPositionals() {
		String secondPositional = POSITIONAL + "Second";
		String thirdPositional = POSITIONAL + "Third";

		this.parameters.addPositional(POSITIONAL);
		this.parameters.addPositional(secondPositional);
		this.parameters.addPositional(thirdPositional);

		assertTrue(this.parameters.getPositionals().containsAll(List.of(POSITIONAL, secondPositional, thirdPositional)));
	}

	@Test
	void containsRegisteredFlags() {
		String secondFlag = FLAG + "Second";
		String thirdFlag = FLAG + "Third";

		this.parameters.addFlag(FLAG);
		this.parameters.addFlag(secondFlag);
		this.parameters.addFlag(thirdFlag);

		assertTrue(this.parameters.getFlags().containsAll(List.of(FLAG, secondFlag, thirdFlag)));
	}

	@Test
	void containsRegisteredSwitchs() {
		String secondSwitch = SWITCH + "Second";
		String thirdSwitch = SWITCH + "Third";

		this.parameters.addSwitch(SWITCH);
		this.parameters.addSwitch(secondSwitch);
		this.parameters.addSwitch(thirdSwitch);

		assertTrue(this.parameters.getSwitchs().containsAll(List.of(SWITCH, secondSwitch, thirdSwitch)));
	}

	@Test
	void isNotEqualToNull() {
		assertNotEquals(null, this.parameters);	
	}

	@Test
	void twoParametersNotAreEqual() {
		this.parameters.addPositional(POSITIONAL);
		this.parameters.addFlag(FLAG);
		this.parameters.addSwitch(SWITCH);

		Parameters other = new Parameters();
		other.addPositional(POSITIONAL);
		other.addFlag(FLAG);

		assertNotEquals(other, this.parameters);
	}

	@Test
	void twoParametersHashCodeNotAreEqual() {
		this.parameters.addPositional(POSITIONAL);
		this.parameters.addFlag(FLAG);
		this.parameters.addSwitch(SWITCH);

		Parameters other = new Parameters();
		other.addPositional(POSITIONAL);
		other.addFlag(FLAG);

		assertNotEquals(other.hashCode(), this.parameters.hashCode());
	}
}
