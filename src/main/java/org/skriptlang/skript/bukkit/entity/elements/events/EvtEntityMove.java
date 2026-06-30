package org.skriptlang.skript.bukkit.entity.elements.events;

/*import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.Nullable;

public class EvtMove extends SkriptEvent {

	// TODO - remove this when Spigot support is dropped
	private static final boolean HAS_ENTITY_MOVE = Skript.classExists("io.papermc.paper.event.entity.EntityMoveEvent");

	static {
		Class<? extends Event>[] events;
		if (HAS_ENTITY_MOVE)
			events = CollectionUtils.array(PlayerMoveEvent.class, EntityMoveEvent.class);
		else
			events = CollectionUtils.array(PlayerMoveEvent.class);
		Skript.registerEvent("Move / Rotate", ch.njol.skript.events.EvtMove.class, events,
				"%entitydata% (move|walk|step|rotate:(turn[ing] around|rotate))",
				"%entitydata% (move|walk|step) or (turn[ing] around|rotate)",
				"%entitydata% (turn[ing] around|rotate) or (move|walk|step)")
			.description(
				"Called when a player or entity moves or rotates their head.",
				"NOTE: Move event will only be called when the entity/player moves position, keyword 'turn around' is for orientation (ie: looking around), and the combined syntax listens for both.",
				"NOTE: These events can be performance heavy as they are called quite often.")
			.examples(
				"on player move:",
				"\tif player does not have permission \"player.can.move\":",
				"\t\tcancel event",
				"on skeleton move:",
				"\tif event-entity is not in world \"world\":",
				"\t\tkill event-entity",
				"on player turning around:",
				"\tsend action bar \"You are currently turning your head around!\" to player")
			.since("2.6, 2.8.0 (turn around)");
	}

	private EntityData<?> entityData;
	private boolean isPlayer;
	private boolean canBePlayer;
	private ch.njol.skript.events.EvtMove.Move moveType;

	private enum Move {

		MOVE("move"),
		MOVE_OR_ROTATE("move or rotate"),
		ROTATE("rotate");

		private final String name;

		Move(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
		entityData = ((Literal<EntityData<?>>) args[0]).getSingle();
		isPlayer = Player.class.isAssignableFrom(entityData.getType());
		if (!HAS_ENTITY_MOVE && !isPlayer) {
			Skript.error("Entity move event requires Paper");
			return false;
		}
		canBePlayer = entityData.getType().isAssignableFrom(Player.class);
		if (matchedPattern > 0) {
			moveType = ch.njol.skript.events.EvtMove.Move.MOVE_OR_ROTATE;
		} else if (parseResult.hasTag("rotate")) {
			moveType = ch.njol.skript.events.EvtMove.Move.ROTATE;
		} else {
			moveType = ch.njol.skript.events.EvtMove.Move.MOVE;
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		Location from, to;
		if (canBePlayer && event instanceof PlayerMoveEvent playerMoveEvent) {
			from = playerMoveEvent.getFrom();
			to = playerMoveEvent.getTo();
		} else if (HAS_ENTITY_MOVE && event instanceof EntityMoveEvent entityMoveEvent) {
			if (!(entityData.isInstance(entityMoveEvent.getEntity())))
				return false;
			from = entityMoveEvent.getFrom();
			to = entityMoveEvent.getTo();
		} else {
			return false;
		}
		return switch (moveType) {
			case MOVE -> hasChangedPosition(from, to);
			case ROTATE -> hasChangedOrientation(from, to);
			case MOVE_OR_ROTATE -> true;
		};
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends Event> [] getEventClasses() {
		if (isPlayer) {
			return new Class[] {PlayerMoveEvent.class};
		} else if (HAS_ENTITY_MOVE) {
			if (canBePlayer)
				return new Class[] {EntityMoveEvent.class, PlayerMoveEvent.class};
			return new Class[] {EntityMoveEvent.class};
		}
		throw new IllegalStateException("This event has not yet initialized!");
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return entityData + " " + moveType;
	}

	private static boolean hasChangedPosition(Location from, Location to) {
		return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ() || from.getWorld() != to.getWorld();
	}

	private static boolean hasChangedOrientation(Location from, Location to) {
		return from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch();
	}

}

 */
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

public class EvtEntityMove extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtEntityMove.class, "Entity Move / Rotate")
			.supplier(EvtEntityMove::new)
			.addEvents(CollectionUtils.array(EntityMoveEvent.class, PlayerMoveEvent.class))
			.addPatterns(
				"%entitydata% (move|walk|step|rotate:(turn[ing] around|rotate))",
				"%entitydata% (move|walk|step) or (turn[ing] around|rotate)",
				"%entitydata% (turn[ing] around|rotate) or (move|walk|step)"
			)
			.addDescription("""
				Called when a player or entity moves or rotates their head.
				The move event will only be called when the entity/player moves position,\s
				keyword 'turn around' is for orientation (ie: looking around),\s
				and the combined syntax listens for both.
				Note that this event is called extremely often and may cause performance issues.
				""")
			.addExample("""
				on healing of a zombie, cow or a wither:
				    event-heal reason is healing potion
				    cancel event
				""")
			.addExample("""
				on player healing from a regeneration potion:
				    send "all better!" to event-entity
				""")
			.addSince("2.6, 2.8.0 (turn around)")
			.build());

		eventValueRegistry.register(EventValue.builder(EntityRegainHealthEvent.class, RegainReason.class)
			.getter(EntityRegainHealthEvent::getRegainReason)
			.build());
	}

	private EntityData<?>[] entityData;
	private RegainReason[] reasons;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("reason")) {
			Literal<RegainReason> reasonLiteral = (Literal<RegainReason>) args[1];
			reasons = reasonLiteral.getArray();
		}
		Literal<EntityData<?>> entityLiteral = (Literal<EntityData<?>>) args[0];
		entityData = entityLiteral.getArray();
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends Event> [] getEventClasses() {
		return null;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append(entityData != null ? entityData : "entity")
			.append("healing")
			.appendIf(reasons != null, "due to", reasons)
			.toString();
	}

}
