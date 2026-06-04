package org.skriptlang.skript.bukkit.bossbar.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.boss.BossBar;
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

@Name("Key Of Boss Bar")
@Description("""
	Returns the key of a keyed boss bar.
	Does not return anything for normal boss bars.
	""")
@Example("""
	broadcast the boss bar key of {_mybar}
	""")
@Since("INSERT VERSION")
public class ExprKeyOfBossBar extends SimpleExpression<String> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprKeyOfBossBar.class, String.class)
				.addPatterns("[the] boss[ ]bar (key|id) of %bossbars%")
				.supplier(ExprKeyOfBossBar::new)
				.build()
		);
	}

	private Expression<BossBar> barExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		barExpr = (Expression<BossBar>) exprs[0];
		return true;
	}

	@Override
	protected String @Nullable [] get(Event event) {
		List<String> keys = new ArrayList<>();
		for (BossBar bar : barExpr.getArray(event)) {
			if (bar instanceof KeyedBossBar keyed) {
				keys.add(keyed.getKey().toString());
			}
		}
		return keys.toArray(new String[0]);
	}


	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append("the keys of");
		builder.append(barExpr);
		return builder.toString();
	}

}
