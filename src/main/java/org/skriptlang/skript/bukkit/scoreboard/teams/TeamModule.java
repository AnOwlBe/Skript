package org.skriptlang.skript.bukkit.scoreboard.teams;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.scoreboard.teams.elements.expressions.ExprTeamFromKey;

public class TeamModule extends HierarchicalAddonModule {

	public TeamModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	protected void initSelf(SkriptAddon addon) {
		Classes.registerClass(new TeamClassInfo());
	}

	@Override
	protected void loadSelf(SkriptAddon addon) {
		register(addon,
			ExprTeamFromKey::register
			);
	}

	@Override
	public String name() {
		return "team";
	}

}
