package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnReason;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.lang.reflect.Method;


public class EvtPlayerRespawn extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerRespawn.class, "Player Respawn")
			.supplier(EvtPlayerRespawn::new)
			.addEvent(PlayerRespawnEvent.class)
			.addPatterns("[player] respawn[ing]")
			.addDescription("""
				Called when a player respawns via death or entering the end portal in the end.
				You should prefer this event over the <a href='#death'>death event</a> as the player is technically alive when this event is called.")
				""")
			.addExample("""
				on respawn:
					send "Hi there!"
				""")
			.addSince("1.0")
			.build());

		// Skripts minimum version is  1.21.1 as of 2.15 & as of 2.16 will still only be 1.21.4
		// 1.21.5+ added AbstractRespawnEvent as a base class, where prior to that, getRespawnReason was in PlayerRespawnEvent
		if (Skript.classExists("org.bukkit.event.player.AbstractRespawnEvent")) {
			eventValueRegistry.register(EventValue.builder(PlayerRespawnEvent.class, PlayerRespawnEvent.RespawnReason.class)
				.getter(PlayerRespawnEvent::getRespawnReason)
				.build());
		} else {
			try {
				Method method = PlayerRespawnEvent.class.getMethod("getRespawnReason");
				eventValueRegistry.register(EventValue.builder(PlayerRespawnEvent.class, PlayerRespawnEvent.RespawnReason.class)
					.getter(event -> {
						try {
							return (RespawnReason) method.invoke(event);
						} catch (Exception e) {
							return null;
						}
					})
					.build());
			} catch (NoSuchMethodException ignored) {}
		}
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
			.append("player respawn")
			.toString();
	}

}
