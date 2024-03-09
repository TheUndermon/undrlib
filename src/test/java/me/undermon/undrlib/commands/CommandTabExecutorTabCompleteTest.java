package me.undermon.undrlib.commands;

import static me.undermon.undrlib.commands.CustomAssertions.assertEqualsIgnoreOrdering;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.undermon.undrlib.commands.CommandTabExecutor;

final class CommandTabExecutorTabCompleteTest {
	private static final CommandSender SENDER_DUMMY = new CommandSenderDummy();
	private CommandTabExecutor tabExecutor;

	@BeforeEach
	public void beforeEach() {
		this.tabExecutor = new CommandTabExecutor() {
			@Override
			protected void onFailure(CommandSender sender) {
				throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
			}
		};
	}

	@Test
	void noSubcommandsHasNoCompletitions() {
		String[] args = new String[] {""};

		assertEqualsIgnoreOrdering(List.<String>of(),
			this.tabExecutor.onTabComplete(SENDER_DUMMY, null, null, args));
	}

	@Test
	void suggestsAllSubcommandsNames() {
		String[] args = new String[] {""};

		String firstSubcommand = "firstSubcommand";
		String secondSubcommand = "secondSubcommand";

		this.tabExecutor.add(new CommandDummy(firstSubcommand));
		this.tabExecutor.add(new CommandDummy(secondSubcommand));

		assertEqualsIgnoreOrdering(List.of(firstSubcommand, secondSubcommand),
			this.tabExecutor.onTabComplete(SENDER_DUMMY, null, null, args));
	}

	@Test
	void suggestsOnlyPartiallyMatchedSubcommandsNames() {
		String[] args = new String[] {"ar"};

		String firstSubcommand = "arbitrary";
		String secondSubcommand = "arch";
		String thirdSubcommand = "tree";

		this.tabExecutor.add(new CommandDummy(firstSubcommand));
		this.tabExecutor.add(new CommandDummy(secondSubcommand));
		this.tabExecutor.add(new CommandDummy(thirdSubcommand));

		assertEqualsIgnoreOrdering(List.of(firstSubcommand, secondSubcommand),
			this.tabExecutor.onTabComplete(SENDER_DUMMY, null, null, args));
	}

	@Test
	void suggestsAllSubcommandsNamesAndDefaultSubcommandCompletitions() {
		String[] args = new String[] {""};

		String firstSubcommandName = "firstSubcommand";
		String secondSubcommandName = "secondSubcommand";
		String defaultSubcommandName = "defaultSubcommand";

		this.tabExecutor.add(new CommandDummy(firstSubcommandName));
		this.tabExecutor.add(new CommandDummy(secondSubcommandName));

		CommandDummy defaultSubcommand = new CommandDummy(defaultSubcommandName);
		defaultSubcommand.addPositional("positionalParameter", (sender, arg) -> List.of("a", "b"));
		
		this.tabExecutor.setDefault(defaultSubcommand);

		assertEqualsIgnoreOrdering(
			List.of(firstSubcommandName, secondSubcommandName, "a", "b"),
			this.tabExecutor.onTabComplete(SENDER_DUMMY, null, null, args)
		);
	}
}
