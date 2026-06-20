package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerReadyArrow extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerReadyArrow.class, "Player Ready Arrow")
			.supplier(EvtPlayerReadyArrow::new)
			.addEvent(PlayerReadyArrowEvent.class)
			.addPatterns("[player] ((ready|choose|draw|load) arrow|arrow (choose|draw|load))")
			.addDescription("""
				Called when a player is firing a bow and the server is choosing an arrow to use.
				Cancelling this event will skip the current arrow item and fire a new event for the next arrow item.
				The arrow and bow in the event can be accessed with the Readied Arrow/Bow expression.
				""")
			.addExample("""
				on player ready arrow:
				    if all:
				        selected bow's name is "Spectral Bow"
				        selected arrow is not a spectral arrow
				    then:
				        cancel event
				        send "You need a spectral arrow to use a spectral bow!" to player
				""")
			.addSince("1.8.0")
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
			.append("player ready arrow")
			.toString();
	}

}
