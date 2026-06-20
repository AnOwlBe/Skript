package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerBreakItem extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerBreakItem.class, "Player Tool Break")
			.supplier(EvtPlayerBreakItem::new)
			.addEvent(PlayerItemBreakEvent.class)
			.addPatterns("[player] tool break[ing]",
				"[player] break[ing] (a|the|) tool")
			.addDescription("""
				Called when a player breaks their tool because its damage reached the maximum value.
				This event cannot be cancelled.
				""")
			.addExample("""
				on player tool breaking:
				    broadcast "well.. its gone now"
				""")
			.addSince("2.1.1")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerItemBreakEvent.class, ItemStack.class)
			.getter(PlayerItemBreakEvent::getBrokenItem)
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
			.append("player tool breaking")
			.toString();
	}

}
