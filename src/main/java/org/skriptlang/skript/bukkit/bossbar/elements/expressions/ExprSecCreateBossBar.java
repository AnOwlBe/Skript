package org.skriptlang.skript.bukkit.bossbar.elements.expressions;

import ch.njol.skript.bukkitutil.NamespacedUtils;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.util.Color;
import ch.njol.skript.variables.Variables;
import ch.njol.skript.doc.Example;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

import static org.skriptlang.skript.bukkit.bossbar.BossBarUtils.nearest;

@Name("Create Boss Bar")
@Description("""
	Creates a new boss bar.
	Boss bars can have viewers removed or added to them.
	Making the boss bar 'keyed' will add it to the persistent storage of the server and will be editable by commands and restored after restart.
	""")
@Example("""
	on join:
		set {_bar} to a boss bar:
			set color of event-bossbar to white
			set title of event-bossbar to "<green>Welcome %player%!"
			set progress of event-bossbar to 50%
			set style of event-bossbar to 6 notches
			make event-bossbar darken the sky
		add player to viewers of {_bar}
		wait 5 seconds
		remove player from viewers of {_bar}
	""")
@Since("INSERT VERSION")
public class ExprSecCreateBossBar extends SectionExpression<BossBar> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprSecCreateBossBar.class, BossBar.class)
				.addPatterns("[a] [new] [%-color%] boss[ ]bar",
					"[a] [new] keyed [%-color%] boss[ ]bar with (id|key) %string%")
				.build()
		);
		eventValueRegistry.register(EventValue.builder(CreateBossBarEvent.class, BossBar.class)
			.getter(CreateBossBarEvent::getBossBar)
			.build());
	}

	private Trigger trigger = null;
	private Expression<String> keyExpr;
	private Expression<Color> colorExpr;
	private Integer matchedPattern;

	@Override
	public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
		matchedPattern = pattern;
		colorExpr = (Expression<Color>) expressions[0];
		if (pattern == 1) {
			keyExpr = (Expression<String>) expressions[1];
		}
		if (node != null) {
			trigger = SectionUtils.loadLinkedCode("create bossbar", (beforeLoading, afterLoading)
				-> loadCode(node, "create bossbar", beforeLoading, afterLoading, CreateBossBarEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected BossBar @Nullable [] get(Event event) {
		BossBar bar = null;
		Color color = null;
		if (colorExpr != null) {
			color = colorExpr.getSingle(event);
		}

		if (matchedPattern == 1) {
			NamespacedKey key = NamespacedUtils.checkValidationAndSend(keyExpr.getSingle(event), this);
			if (key == null)
				return new BossBar[0];
			Bukkit.createBossBar(key, null, BarColor.WHITE, BarStyle.SOLID);
			bar = Bukkit.getBossBar(key);
		} else {
			bar = Bukkit.createBossBar(null, BarColor.WHITE, BarStyle.SOLID);
		}

		if (bar == null)
			return new BossBar[0];
		if (color != null) {
			BarColor nearest = nearest(color);
			if (nearest != null) {
				bar.setColor(nearest);
			}
		}

		if (trigger == null)
			return new BossBar[] {bar};
		CreateBossBarEvent bossbarEvent = new CreateBossBarEvent(bar);
		Variables.withLocalVariables(event, bossbarEvent, () -> TriggerItem.walk(trigger, bossbarEvent));
		return new BossBar[] {bossbarEvent.getBossBar()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends BossBar> getReturnType() {
		if (keyExpr != null)
			return KeyedBossBar.class;
		return BossBar.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (keyExpr != null) {
			return "a keyed bossbar with key" + keyExpr;
		}
		else {
			return "a bossbar";
		}
	}

	private static class CreateBossBarEvent extends Event {
		private final BossBar bar;

		public CreateBossBarEvent(BossBar bar) {
			this.bar = bar;
		}

		public BossBar getBossBar() {
			return bar;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}

	}

}
