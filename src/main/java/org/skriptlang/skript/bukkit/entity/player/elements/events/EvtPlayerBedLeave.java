package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerBedLeave extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerBedLeave.class, "Player Bed Leave")
			.supplier(EvtPlayerBedLeave::new)
			.addEvent(PlayerBedLeaveEvent.class)
			.addPatterns("bed leav(e|ing)",
				"[player] leav(e|ing) [a] bed")
			.addDescription("""
				Called when a player leaves a bed.
				""")
			.addExample("""
				on player leaving a bed:
				""")
			.addSince("1.0")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerBedLeaveEvent.class, Block.class)
			.getter(PlayerBedLeaveEvent::getBed)
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
			.append("bed leave")
			.toString();
	}

}
