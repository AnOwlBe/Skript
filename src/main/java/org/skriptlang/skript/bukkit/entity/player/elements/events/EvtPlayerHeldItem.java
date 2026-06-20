package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerHeldItem extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerHeldItem.class, "Player Tool Change")
			.supplier(EvtPlayerHeldItem::new)
			.addEvent(PlayerItemHeldEvent.class)
			.addPatterns("[player['s]] (tool|item held|held item) chang(e|ing)")
			.addDescription("""
				Called whenever a player changes their held item by selecting a different slot (e.g. the keys 1-9 or the mouse wheel), <i>not</i> by dropping or replacing the item in the current slot.
				""")
			.addExample("""
				on player's held item change:
				    send "You changed your held item!" to player
				""")
			.addSince("1.0")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerItemHeldEvent.class, Slot.class)
			.getter(event -> new InventorySlot(event.getPlayer().getInventory(), event.getNewSlot()))
			.build());
		eventValueRegistry.register(EventValue.builder(PlayerItemHeldEvent.class, Slot.class)
			.getter(event -> new InventorySlot(event.getPlayer().getInventory(), event.getPreviousSlot()))
			.time(Time.PAST)
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
			.append("player tool changing")
			.toString();
	}

}
