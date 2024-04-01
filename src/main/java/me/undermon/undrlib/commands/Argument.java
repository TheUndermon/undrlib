/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

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