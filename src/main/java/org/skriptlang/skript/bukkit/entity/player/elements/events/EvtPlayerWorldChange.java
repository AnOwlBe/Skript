package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerWorldChange extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerWorldChange.class, "Player World Change")
			.supplier(EvtPlayerWorldChange::new)
			.addEvent(PlayerChangedWorldEvent.class)
			.addPatterns("[player] world chang(ing|e[d])")
			.addDescription("""
				Called when a player enters a world.
				""")
			.addExample("""
				on player world change:
				    event-world is "world_the_end"
				    send "Its the end!" to player
				""")
			.addSince("2.2-dev28")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerChangedWorldEvent.class, World.class)
			.getter(PlayerChangedWorldEvent::getFrom)
			.time(Time.PAST)
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
			.append("player world changing")
			.toString();
	}

}
