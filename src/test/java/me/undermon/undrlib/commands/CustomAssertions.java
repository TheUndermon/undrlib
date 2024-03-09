package me.undermon.undrlib.commands;

import java.util.Collection;

import org.junit.jupiter.api.AssertionFailureBuilder;

final class CustomAssertions {

	private CustomAssertions() {}

	public static void assertEqualsIgnoreOrdering(Collection<String> expected, Collection<String> actual) {
		if (!(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected))) {
			AssertionFailureBuilder.assertionFailure().expected(expected).actual(actual).buildAndThrow();
		}
	}
}
