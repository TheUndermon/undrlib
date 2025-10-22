/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

package me.undermon.undrlib.commands;

import java.util.Collection;
import java.util.Optional;

public interface Input {

	public Optional<String> getPositional(String name);

	public Optional<String> getFlag(String name);

	public Optional<String> getPositionalOrFlag(String name);

	public boolean getSwitch(String name);

	public Collection<String> getLeftoverPositionals();

	public Collection<String> getUnknownFlags();

}
