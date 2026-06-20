package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.jetbrains.annotations.Nullable;

import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerSprintToggle extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerSprintToggle.class, "Player Sprint Toggle")
			.supplier(EvtPlayerSprintToggle::new)
			.addEvent(PlayerToggleSprintEvent.class)
			.addPatterns("[player] toggl(e|ing) sprint",
				"[player] sprint toggl(e|ing)")
			.addDescription("""
				Called when a player starts or stops sprinting.
				Use <a href='#CondIsSprinting'>is sprinting</a> to get whether the player was sprinting before the event was called.
				""")
			.addExample("""
				on sprint toggle:
				    player is not sprinting
				    chance of 30%:
				        spawn wither behind player
				        send "Run.." to player
				        stop
				    send "You got lucky this time.." to player
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
			.append("player sprint toggle")
			.toString();
	}

}
