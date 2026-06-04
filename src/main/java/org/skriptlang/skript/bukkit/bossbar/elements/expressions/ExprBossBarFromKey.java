package org.skriptlang.skript.bukkit.bossbar.elements.expressions;

import ch.njol.skript.bukkitutil.NamespacedUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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

@Name("Boss Bar From Key")
@Description("Returns a keyed boss bar from the specified key.")
@Example("""
	set title of (boss bar from key "test") to "hm"
	""")
@Since("INSERT VERSION")
public class ExprBossBarFromKey extends SimpleExpression<KeyedBossBar> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprBossBarFromKey.class, KeyedBossBar.class)
				.addPatterns("[the] boss[ ]bar[s] (from|with) [the] (id|key)[s] %strings%")
				.supplier(ExprBossBarFromKey::new)
				.build()
		);
	}

	private Expression<String> keyExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		keyExpr = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected KeyedBossBar @Nullable [] get(Event event) {
		List<KeyedBossBar> bars = new ArrayList<>();
		for (String string : keyExpr.getArray(event)) {
			NamespacedKey key = NamespacedUtils.checkValidationAndSend(string, this);
			if (key == null)
				continue;
			KeyedBossBar bar = Bukkit.getBossBar(key);
			if (bar != null)
				bars.add(bar);
		}
		return bars.toArray(KeyedBossBar[]::new);
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
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append("boss bar from keys");
		builder.append(keyExpr);
		return builder.toString();
	}

}
