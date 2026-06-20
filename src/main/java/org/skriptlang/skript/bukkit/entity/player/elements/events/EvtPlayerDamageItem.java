package org.skriptlang.skript.bukkit.entity.player.elements.events;


import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerDamageItem extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerDamageItem.class, "Player Tool Break")
			.supplier(EvtPlayerDamageItem::new)
			.addEvent(PlayerItemDamageEvent.class)
			.addPatterns("item damag(e|ing)")
			.addDescription("""
				Called when an item is damaged. Most tools are damaged by using them; armor is damaged when the wearer takes damage.
				""")
			.addExample("""
				on item damaging:
				    send actionbar "One of your items is taking damage!" to player
				""")
			.addSince("2.5")
			.build());
		eventValueRegistry.register(EventValue.builder(PlayerItemDamageEvent.class, ItemStack.class)
			.getter(PlayerItemDamageEvent::getItem)
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
			.append("player item damaging")
			.toString();
	}

}

