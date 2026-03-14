package ch.njol.skript.events;

import ch.njol.skript.registrations.EventValues;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;



public class EvtEntityPathFind extends SkriptEvent {
	static {
		Skript.registerEvent("Entity Pathfind", EvtEntityPathFind.class, EntityPathfindEvent.class, "[entity] [start[s]] pathfind[ing]")
			.description("Called when an entity attempts to pathfind to a location.")
			.examples("on pathfind:",
				"\tbroadcast \"%event-entity% has started pathfinding towards %event-location%!\"")
			.since("2.15");

		    EventValues.registerEventValue(EntityPathfindEvent.class, Location.class, EntityPathfindEvent::getLoc);
	}

	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
		return true;
	}
	@Override
	public boolean check(final Event e) {
		return true;
	}

	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "entity pathfind";
	}

}

