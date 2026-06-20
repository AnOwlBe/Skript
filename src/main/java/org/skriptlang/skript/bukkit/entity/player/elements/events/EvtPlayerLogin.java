package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

// To be replaced with https://github.com/SkriptLang/Skript/pull/8597

@SuppressWarnings("deprecation")
public class EvtPlayerLogin extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerLogin.class, "Player Connect")
			.supplier(EvtPlayerLogin::new)
			.addEvent(PlayerLoginEvent.class)
			.addPatterns("[player] connect[ing]")
			.addDescription("""
				Called when the player connects to the server.
				This event is called before the player actually joins the server, so if you want to prevent players from joining you should prefer this event over <a href='#join'>on join</a>.
				""")
			.addExample("""
				on connect:
				    if all:
				        player doesn't have permission "group.vip"
				        size of all players >= (max players - 5)
				    then:
				        kick player due to "The last 5 slots are reserved for those with VIP rank!"
				""")
			.addSince("2.0")
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
			.append("player connecting")
			.toString();
	}

}
