package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerQuitEvent.QuitReason;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerQuit extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerQuit.class, "Player Quit")
			.supplier(EvtPlayerQuit::new)
			.addEvent(PlayerQuitEvent.class)
			.addPatterns("(quit[ting]|disconnect[ing]|log[ ]out|logging out|leav(e|ing))")
			.addDescription("""
				Called when a player leaves the server.
				This event cannot be cancelled.
				""")
			.addExample("""
				on quit:
				    set the quit message to "%player% just left :("
				""")
			.addSince("1.0 (simple disconnection)")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerQuitEvent.class, QuitReason.class)
			.getter(PlayerQuitEvent::getReason)
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
			.append("player quit")
			.toString();
	}

}
