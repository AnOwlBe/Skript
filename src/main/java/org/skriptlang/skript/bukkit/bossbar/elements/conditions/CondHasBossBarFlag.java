package org.skriptlang.skript.bukkit.bossbar.elements.conditions;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.Patterns;
import ch.njol.util.Kleenean;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Has Boss Bar Flag")
@Description("""
	Checks if a boss bar has a specific flag.
	There are 3 flags:
	`darken the sky`
	`create fog`
	`play boss music`
	""")
@Example("""
	if {_mybar} does darken the sky:
		broadcast "Its getting dark around here.."
	""")
@Since("INSERT VERSION")
public class CondHasBossBarFlag extends Condition {

	private static final Patterns<BarFlag> PATTERNS = new Patterns<>(new Object[][]{
		{"%bossbars% [do[es]] darken[s] the sky", BarFlag.DARKEN_SKY},
		{"%bossbars% (doesn't|do[es] not) darken the sky", BarFlag.DARKEN_SKY},
		{"%bossbars% [do[es]] create[s] fog", BarFlag.CREATE_FOG},
		{"%bossbars% (doesn't|do[es] not) create fog", BarFlag.CREATE_FOG},
		{"%bossbars% [do[es]] play[s] boss music", BarFlag.PLAY_BOSS_MUSIC},
		{"%bossbars% (doesn't|do[es] not) play boss music", BarFlag.PLAY_BOSS_MUSIC},
	});

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.CONDITION,
			SyntaxInfo.builder(CondHasBossBarFlag.class)
				.addPatterns(PATTERNS.getPatterns())
				.supplier(CondHasBossBarFlag::new)
				.build()
		);
	}

	private Expression<BossBar> bars;
	private BarFlag flag;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		flag = PATTERNS.getInfo(matchedPattern);
		bars = (Expression<BossBar>) exprs[0];
		setNegated(matchedPattern % 2 != 0);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return bars.check(event, bar -> bar.hasFlag(flag), isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append(bars);
		builder.append(isNegated() ? "does not" : "does");
		builder.append(switch (flag) {
			case DARKEN_SKY -> "darken the sky";
			case CREATE_FOG -> "create fog";
			case PLAY_BOSS_MUSIC -> "play boss music";
		});
		return builder.toString();
	}

}
