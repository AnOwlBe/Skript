package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerSwapHandItems extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerSwapHandItems.class, "Player Hand Swap Items")
			.supplier(EvtPlayerSwapHandItems::new)
			.addEvent(PlayerSwapHandItemsEvent.class)
			.addPatterns("[player] swap[ping of] [(hand|held)] item[s]")
			.addDescription("""
				Called whenever a player swaps the items in their main- and offhand slots.
				Works also when one or both of the slots are empty.
				The event is called before the items are actually swapped,
				so when you use the player's tool or player's offtool expressions,
				they will return the values before the swap - this enables you to cancel the event before anything happens.
				""")
			.addExample("""
				on swap hand items:
				    player's tool is a totem of undying
				    chance of 50%:
				         send "Failed! Please try again!" to player
				         cancel event
				""")
			.addSince("2.3")
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
			.append("player hand swap items")
			.toString();
	}

}
