package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.Timespan;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent.ChangeReason;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerExprCooldownChange extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerExprCooldownChange.class, "Player Experience Cooldown Change")
			.supplier(EvtPlayerExprCooldownChange::new)
			.addEvent(PlayerExpCooldownChangeEvent.class)
			.addPatterns("[player] (experience|[e]xp) cooldown change")
			.addDescription("""
				Called when a player's experience cooldown changes.
				Experience cooldown is how long until a player can pick up another orb of experience.
				""")
			.addExample("""
				on player experience cooldown change:
				    broadcast player
				    broadcast event-timespan
				    broadcast past event-timespan
				    broadcast xp cooldown change reason
				""")
			.addSince("2.10")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerExpCooldownChangeEvent.class, ChangeReason.class)
			.getter(PlayerExpCooldownChangeEvent::getReason)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerExpCooldownChangeEvent.class, Timespan.class)
			.getter(event -> new Timespan(Timespan.TimePeriod.TICK, event.getPlayer().getExpCooldown()))
			.time(Time.PAST)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerExpCooldownChangeEvent.class, Timespan.class)
			.getter(event -> new Timespan(Timespan.TimePeriod.TICK, event.getNewCooldown()))
			.build());
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("player experience cooldown change")
			.toString();
	}

}
