package org.skriptlang.skript.bukkit.scoreboard.objective;

import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.registrations.Classes;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.scoreboard.objective.elements.expressions.ExprDisplaySlot;

public class ObjectiveModule extends HierarchicalAddonModule {

	public ObjectiveModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	protected void initSelf(SkriptAddon addon) {
		Classes.registerClass(new ObjectiveClassInfo());
		Classes.registerClass(new CriteriaClassInfo());

		Classes.registerClass(new EnumClassInfo<>(DisplaySlot.class, "displayslot", "display slot")
			.user("display ?slots?")
			.name("Display Slot")
			.description("The display slot of an objective.")
			.examples("set display slot of {_objective} to below name")
			.since("INSERT VERSION"));

		Classes.registerClass(new EnumClassInfo<>(RenderType.class, "rendertype", "render type")
			.user("render ?types?")
			.name("Render Type")
			.description("The render type of an objective.")
			.examples("set render type of {_objective} to hearts")
			.since("INSERT VERSION"));

	//	Classes.registerClass(new EnumClassInfo<>(Criteria.class, "criteria", "criteria")
		//	.user("criterias?")
			//.name("Criteria")
			//.description("Criteria")
			//.examples("set display slot of {_objective} to below name")
			//.since("INSERT VERSION"));
	}

	@Override
	protected void loadSelf(SkriptAddon addon) {
		register(addon,
			ExprDisplaySlot::register
			);
	}

	@Override
	public String name() {
		return "objective";
	}

}
