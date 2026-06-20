package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.Timespan;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerStopUsingItem extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerStopUsingItem.class, "Player Stop Using Item")
			.supplier(EvtPlayerStopUsingItem::new)
			.addEvent(PlayerStopUsingItemEvent.class)
			.addPatterns("[player] (stop|end) (using item|item use)")
			.addDescription("""
				Called when a player stops using an item. For example,
				when the player releases the interact button when holding a bow, an edible item, or a spyglass.
				""")
			.addExample("""
				on player stop using item:
				    send "You just used %event-item% for %event-timespan% :)" to player
				""")
			.addSince("2.8.0")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerStopUsingItemEvent.class, ItemStack.class)
			.getter(PlayerStopUsingItemEvent::getItem)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerStopUsingItemEvent.class, Timespan.class)
			.getter(event -> new Timespan(Timespan.TimePeriod.TICK, event.getTicksHeldFor()))
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
			.append("player stop using item")
			.toString();
	}

}
