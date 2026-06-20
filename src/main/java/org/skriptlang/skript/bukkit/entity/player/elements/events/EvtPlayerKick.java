package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerKick extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerKick.class, "Player Kick")
			.supplier(EvtPlayerKick::new)
			.addEvent(PlayerKickEvent.class)
			.addPatterns("[player] (kick|being kicked)")
			.addDescription("""
				Called when a player is kicked from the server.
				You can change the <a href='#ExprMessage'>kick message</a> or <a href='#EffCancelEvent'>cancel the event</a> entirely.
				""")
			.addExample("""
				on kick:
				    send "%player% just got kicked!" to all operators
				""")
			.addSince("1.0")
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
			.append("player kick")
			.toString();
	}

}
