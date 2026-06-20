package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.entity.Egg;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerThrowEgg extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerThrowEgg.class, "Player Throw Egg")
			.supplier(EvtPlayerThrowEgg::new)
			.addEvent(PlayerEggThrowEvent.class)
			.addPatterns("throw[ing] [of] [an] egg",
				"[player] egg throw")
			.addDescription("""
				Called when a player throws an egg and it lands.
				You can just use the <a href='#shoot'>shoot event</a> in most cases.
				However, this event allows modification of properties like the hatched entity type and the number of entities to hatch.
				""")
			.addExample("""
				on throw of an egg:
				    broadcast "An egg has been thrown!"
				""")
			.addSince("1.0")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerEggThrowEvent.class, Egg.class)
			.getter(PlayerEggThrowEvent::getEgg)
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
			.append("player egg throw")
			.toString();
	}

}
