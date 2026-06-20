package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerMendItem extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerMendItem.class, "Player Item Mend")
			.supplier(EvtPlayerMendItem::new)
			.addEvent(PlayerItemMendEvent.class)
			.addPatterns("[player] item mend[(ed|ing)]",
			"[player] mend item")
			.addDescription("""
				Called when a player has an item repaired via the Mending enchantment.
				""")
			.addExample("""
				on item mend:
				     send "One of your tools was mended!" to player
				""")
			.addSince("2.5.1")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerItemMendEvent.class, ItemStack.class)
			.getter(PlayerItemMendEvent::getItem)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerItemMendEvent.class, Entity.class)
			.getter(PlayerItemMendEvent::getExperienceOrb)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerItemMendEvent.class, Integer.class)
			.getter(PlayerItemMendEvent::getConsumedExperience)
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
			.append("player item mend")
			.toString();
	}

}
