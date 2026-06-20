package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerRiptide extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerRiptide.class, "Player Riptide")
			.supplier(EvtPlayerRiptide::new)
			.addEvent(PlayerRiptideEvent.class)
			.addPatterns("[use of] riptide [enchant[ment]]")
			.addDescription("""
				Called when the player activates the riptide enchantment, using their trident to propel them through the air.
				Note: the riptide action is performed client side, so manipulating the player in this event may have undesired effects.
				""")
			.addExample("""
				on riptide:
				    chance of 10%:
				        set the weather to clear
				        send "You got unlucky.. the sky cleared up!" to player
				""")
			.addSince("2.5")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerRiptideEvent.class, ItemStack.class)
			.getter(PlayerRiptideEvent::getItem)
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
			.append("player riptide")
			.toString();
	}

}
