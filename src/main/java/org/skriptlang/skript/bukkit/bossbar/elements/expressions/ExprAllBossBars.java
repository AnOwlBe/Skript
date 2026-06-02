package org.skriptlang.skript.bukkit.bossbar.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Bukkit;
import org.bukkit.boss.KeyedBossBar;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.List;

@Name("All Boss Bars")
@Description("Returns all persistent boss bars.")
@Example("""
	broadcast all of the boss bars
	""")
@Since("INSERT VERSION")

public class ExprAllBossBars extends SimpleExpression<KeyedBossBar> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprAllBossBars.class, KeyedBossBar.class)
				.addPatterns("[(all|the|all [of] the)] keyed boss[ ]bars")
				.supplier(ExprAllBossBars::new)
				.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	@Nullable
	protected KeyedBossBar @Nullable [] get(Event event) {
		List<KeyedBossBar> list = new ArrayList<>();
		Bukkit.getBossBars().forEachRemaining(list::add);
		return list.toArray(KeyedBossBar[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends KeyedBossBar> getReturnType() {
		return KeyedBossBar.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "all boss bars";
	}

}
