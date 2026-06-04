package org.skriptlang.skript.bukkit.bossbar.elements.conditions;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Is Keyed")
@Description("""
	Checks if a boss bar is keyed or not.
	""")
@Example("""
	if {_mybar} is a keyed boss bar:
		broadcast "Its keyed!"
	else if {_mybar} is not a keyed boss bar:
		broadcast "It isn't keyed!"
	""")
@Since("INSERT VERSION")
public class CondIsKeyedBossBar extends Condition {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.CONDITION,
			SyntaxInfo.builder(CondIsKeyedBossBar.class)
				.addPattern("%bossbars% is [a] keyed boss[ ]bar")
				.addPattern("%bossbars% is not [a] keyed boss[ ]bar")
				.supplier(CondIsKeyedBossBar::new)
				.build()
		);
	}

	private Expression<BossBar> bar;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		bar = (Expression<BossBar>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (bar instanceof KeyedBossBar) {
			return isNegated();
		}
		return !isNegated();
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append(bar, "is");
		builder.appendIf(isNegated(), "not");
		builder.append("a keyed boss bar");
		return builder.toString();
	}

}
