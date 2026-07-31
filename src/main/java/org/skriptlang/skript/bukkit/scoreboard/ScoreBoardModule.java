package org.skriptlang.skript.bukkit.scoreboard;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.scoreboard.elements.expressions.ExprScoreBoard;
import org.skriptlang.skript.bukkit.scoreboard.elements.expressions.ExprSecScoreBoard;
import org.skriptlang.skript.bukkit.scoreboard.objective.ObjectiveModule;
import org.skriptlang.skript.bukkit.scoreboard.teams.TeamModule;

import java.util.List;

public class ScoreBoardModule extends HierarchicalAddonModule {

	public ScoreBoardModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public Iterable<AddonModule> children() {
		return List.of(
			new ObjectiveModule(this),
			new TeamModule(this)
		);
	}

	@Override
	protected void initSelf(SkriptAddon addon) {
		Classes.registerClass(new ScoreBoardClassInfo());
	}

	@Override
	protected void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
			ExprScoreBoard::register,
			syntaxRegistry -> ExprSecScoreBoard.register(syntaxRegistry, eventValueRegistry)
		);
	}

	@Override
	public String name() {
		return "score board";
	}

}
