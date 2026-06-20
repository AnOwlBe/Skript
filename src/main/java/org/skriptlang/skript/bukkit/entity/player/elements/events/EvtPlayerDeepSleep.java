package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerDeepSleep extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerDeepSleep.class, "Player Deep Sleep")
			.supplier(EvtPlayerDeepSleep::new)
			.addEvent(PlayerDeepSleepEvent.class)
			.addPatterns("[player] deep sleep[ing]")
			.addDescription("""
				Called when a player has slept long enough to count as passing the night/storm.
				Cancelling this event will prevent the player from being counted as deeply sleeping unless they exit and re-enter the bed.
				""")
			.addExample("""
				on player deep sleeping:
				    send "Have a good sleep!" to player
				    send actionbar "Zzzz..." to player
				""")
			.addSince("2.7")
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
			.append("player deep sleep")
			.toString();
	}

}
