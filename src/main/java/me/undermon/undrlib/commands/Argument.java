package me.undermon.undrlib.commands;

final record Argument(Argument.Type type, String name, String argument) {
	enum Type {
		POSITIONAL,
		FLAG,
		SWITCH
	}

	static Argument positional(String name, String argument) {
		return new Argument(Type.POSITIONAL, name, argument);
	}

	static Argument flag(String name, String argument) {
		return new Argument(Type.FLAG, name, argument);
	}

	static Argument switchFlag(String name) {
		return new Argument(Type.SWITCH, name, null);
	}
}