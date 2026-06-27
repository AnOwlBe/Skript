package org.skriptlang.skript.bukkit.entity.elements.events;

import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class EvtEntityTransform extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtEntityTransform.class, "Entity Transform")
			.supplier(EvtEntityTransform::new)
			.addEvent(EntityTransformEvent.class)
			.addPatterns("[entity:%*-entitydatas%] transform[ing] [transform: due to %-transformreasons%]")
			.addDescription("""
				Called when an entity is about to be replaced by another entity.
				e.g. when a zombie gets cured and a villager spawns,\s
				an entity drowns in water like a zombie that turns to a drown,\s
				an entity that gets frozen in powder snow,\s
				a mooshroom that when sheared, spawns a new cow.
				""")
			.addExample("""
				on a zombie transforming due to curing:
				    broadcast "Another one cured from this madness.."
				""")
			.addExample("""
				on mooshroom transforming:
				    cancel event
				    broadcast "forever a mooshroom!"
				""")
			.addSince("2.8.0")
			.build());

		eventValueRegistry.register(EventValue.builder(EntityTransformEvent.class, Entity[].class)
			.getter(event -> event.getTransformedEntities().toArray(Entity[]::new))
			.build());

		eventValueRegistry.register(EventValue.builder(EntityTransformEvent.class, TransformReason.class)
			.getter(EntityTransformEvent::getTransformReason)
			.build());
	}

	private EntityData[] entityData;
	private TransformReason[] reasons;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("transform")) {
			Literal<TransformReason> transformLiteral = (Literal<TransformReason>) args[1];
			reasons = transformLiteral.getArray();
		}
		if (parseResult.hasTag("entity")) {
			Literal<EntityData> entityLiteral = (Literal<EntityData>) args[0];
			entityData = entityLiteral.getArray();
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		EntityTransformEvent entityEvent = (EntityTransformEvent) event;
		boolean reasonMatched = reasons == null
			|| Arrays.stream(reasons)
			.anyMatch(reason -> reason == entityEvent.getTransformReason());

		boolean entityDataMatched = entityData == null
			|| Arrays.stream(entityData)
			.anyMatch(data -> data.isInstance(entityEvent.getEntity()));

		return reasonMatched && entityDataMatched;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append(entityData != null ? entityData : "entity")
			.append("transforming")
			.appendIf(reasons != null, "due to", reasons)
			.toString();
	}

}
