package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerJump extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerJump.class, "Player Jump")
			.supplier(EvtPlayerJump::new)
			.addEvent(PlayerJumpEvent.class)
			.addPatterns("[player] jump[ing]")
			.addDescription("""
				Called whenever a player jumps.
				""")
			.addExample("""
				on jump:
				    add 1 to {-example::%player's uuid%}
				    if {-example::%player's uuid%} >= 3:
				        delete {-example::%player's uuid%}
				        push player forwards at speed 2
				        send "Forward you go!" to player
				    wait 10 ticks
				    delete {-example::%player's uuid%}
				""")
			.addSince("2.3")
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
			.append("player jump")
			.toString();
	}

}