package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.jetbrains.annotations.Nullable;

import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerFlightToggle extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerFlightToggle.class, "Player Flight Toggle")
			.supplier(EvtPlayerFlightToggle::new)
			.addEvent(PlayerToggleFlightEvent.class)
			.addPatterns("[player] flight toggl(e|ing)",
				"[player] toggl(e|ing) flight")
			.addDescription("""
				Called when a players stops/starts flying.
				""")
			.addExample("""
				on flight toggle:
				    player is not operator
				    kill player
				    send "You tried to use an admin ability!" to player
				""")
			.addSince("2.2-dev36")
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
			.append("player sneak toggle")
			.toString();
	}

}
