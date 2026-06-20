package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerChangeBeaconEffect extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerChangeBeaconEffect.class, "Player Change Beacon Effect")
			.supplier(EvtPlayerChangeBeaconEffect::new)
			.addEvent(PlayerChangeBeaconEffectEvent.class)
			.addPatterns("[player] chang(e[s]|ing) [of] beacon effect")
			.addDescription("""
				Called when a player changes the effects of a beacon.
				""")
			.addExample("""
				on player changing of beacon effect:
				    broadcast "The player who did this: %player%"
				    broadcast "The location: %location of event-block%"
				    broadcast "Hurry to the given location!"
				""")
			.addSince("2.10")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerChangeBeaconEffectEvent.class, Block.class)
			.getter(PlayerChangeBeaconEffectEvent::getBeacon)
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
			.append("player beacon effect change")
			.toString();
	}

}
