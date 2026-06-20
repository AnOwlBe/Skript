package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerBedEnter extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerBedEnter.class, "Player Bed Enter")
			.supplier(EvtPlayerBedEnter::new)
			.addEvent(PlayerBedEnterEvent.class)
			.addPatterns("bed enter[ing]",
				"[player] enter[ing] [a] bed")
			.addDescription("""
				Called when a player starts sleeping.
				""")
			.addExample("""
				on player leaving a bed:
				""")
			.addSince("1.0")
			.build());

			eventValueRegistry.register(EventValue.builder(PlayerBedEnterEvent.class, Block.class)
				.getter(PlayerBedEnterEvent::getBed)
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
			.append("bed enter")
			.toString();
	}

}
