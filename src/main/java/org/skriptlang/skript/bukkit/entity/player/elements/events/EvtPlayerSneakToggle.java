package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.Nullable;

import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerSneakToggle extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerSneakToggle.class, "Player Sneak Toggle")
			.supplier(EvtPlayerSneakToggle::new)
			.addEvent(PlayerToggleSneakEvent.class)
			.addPatterns("[player] toggl(e|ing) sneak",
				"[player] sneak toggl(e|ing)")
			.addDescription("""
				Called when a player starts or stops sneaking.
				Use <a href='#CondIsSneaking'>is sneaking</a> to get whether the player was sneaking before the event was called.
				""")
			.addExample("""
				on sneak toggle:
				    player is sneaking
				    push player upwards at 0t.5
				    send "UP!" to player
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
			.append("player sneak toggle")
			.toString();
	}

}
