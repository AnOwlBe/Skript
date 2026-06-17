package org.skriptlang.skript.bukkit.bossbar.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Is Keyed")
@Description("Checks whether a boss bar is keyed or not.")
@Example("""
	if {_mybar} is a keyed boss bar:
		broadcast "It's keyed: %boss bar key of {_mybar}%"
	else:
		broadcast "It isn't keyed!"
	""")
@Since("INSERT VERSION")
public class CondIsKeyedBossBar extends PropertyCondition<BossBar> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.CONDITION,
			infoBuilder(
				CondIsKeyedBossBar.class,
				PropertyType.BE,
				"[a] keyed boss[ ]bar",
				"bossbars"
			)
				.supplier(CondIsKeyedBossBar::new)
				.build()
		);
	}

	@Override
	public boolean check(BossBar bar) {
		return bar instanceof KeyedBossBar;
	}

	@Override
	protected PropertyType getPropertyType() {
		return PropertyType.BE;
	}

	@Override
	protected String getPropertyName() {
		return "a keyed boss bar";
	}

}
