package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
public class EvtPlayerJoin extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerJoin.class, "Player Join")
			.supplier(EvtPlayerJoin::new)
			.addEvent(PlayerJoinEvent.class)
			.addPatterns("[player] (login|logging in|join[ing])")
			.addDescription("""
				Called when the player joins the server.
				The player is already in a world when this event is called, so if you want to prevent players from joining you should prefer <a href='#connect'>on connect</a> over this event.
				See <a href='#join message'>join message</a> for how to set the join message.
				
				""")
			.addExample("""
				on join:
				    send "Hello %player%!"
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
			.append("player join")
			.toString();
	}

}
