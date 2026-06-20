package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerElytraBoost extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerElytraBoost.class, "Player Elytra Boost")
			.supplier(EvtPlayerElytraBoost::new)
			.addEvent(PlayerElytraBoostEvent.class)
			.addPatterns("[player] elytra boost[ing]")
			.addDescription("""
				Called when a player uses a firework to boost their fly speed when flying with an elytra.
				""")
			.addExample("""
				on elytra boost:
				    push player up at speed 3
				    send "You go forward and up!" to player
				""")
			.addSince("1.10")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerElytraBoostEvent.class, ItemStack.class)
			.getter(PlayerElytraBoostEvent::getItemStack)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerElytraBoostEvent.class, Entity.class)
			.getter(PlayerElytraBoostEvent::getFirework)
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
			.append("player elytra boost")
			.toString();
	}

}
