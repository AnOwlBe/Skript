package org.skriptlang.skript.bukkit.scoreboard.teams;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;

public class TeamModule extends HierarchicalAddonModule {

	public TeamModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	protected void initSelf(SkriptAddon addon) {
	}

	@Override
	protected void loadSelf(SkriptAddon addon) {

	}

	@Override
	public String name() {
		return "team";
	}

}
