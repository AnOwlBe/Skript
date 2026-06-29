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

/*
public class EvtExperienceSpawn extends SkriptEvent {

	static {
		Skript.registerEvent("Experience Spawn", EvtExperienceSpawn.class, ExperienceSpawnEvent.class,
				"[e]xp[erience] [orb] spawn",
				"spawn of [a[n]] [e]xp[erience] [orb]"
			).description(
				"Called whenever experience is about to spawn.",
				"Please note that this event will not fire for xp orbs spawned by plugins (including Skript) with Bukkit."
			).examples(
				"on xp spawn:",
				"\tworld is \"minigame_world\"",
				"\tcancel event"
			).since("2.0");
		EventValues.registerEventValue(ExperienceSpawnEvent.class, Location.class, ExperienceSpawnEvent::getLocation);
		EventValues.registerEventValue(ExperienceSpawnEvent.class, Experience.class, event -> new Experience(event.getSpawnedXP()));
	}

	private static final List<Trigger> TRIGGERS = Collections.synchronizedList(new ArrayList<>());

	private static final AtomicBoolean REGISTERED_EXECUTORS = new AtomicBoolean();

	private static final EventExecutor EXECUTOR = (listener, event) -> {
		ExperienceSpawnEvent experienceEvent;
		if (event instanceof BlockExpEvent) {
			experienceEvent = new ExperienceSpawnEvent(
				((BlockExpEvent) event).getExpToDrop(),
				((BlockExpEvent) event).getBlock().getLocation().add(0.5, 0.5, 0.5)
			);
		} else if (event instanceof EntityDeathEvent) {
			experienceEvent = new ExperienceSpawnEvent(
				((EntityDeathEvent) event).getDroppedExp(),
				((EntityDeathEvent) event).getEntity().getLocation()
			);
		} else if (event instanceof ExpBottleEvent) {
			experienceEvent = new ExperienceSpawnEvent(
				((ExpBottleEvent) event).getExperience(),
				((ExpBottleEvent) event).getEntity().getLocation()
			);
		} else if (event instanceof PlayerFishEvent) {
			if (((PlayerFishEvent) event).getState() != PlayerFishEvent.State.CAUGHT_FISH) // There is no EXP
				return;
			experienceEvent = new ExperienceSpawnEvent(
				((PlayerFishEvent) event).getExpToDrop(),
				((PlayerFishEvent) event).getPlayer().getLocation()
			);
		} else {
			assert false;
			return;
		}

		SkriptEventHandler.logEventStart(event);
		synchronized (TRIGGERS) {
			for (Trigger trigger : TRIGGERS) {
				SkriptEventHandler.logTriggerStart(trigger);
				trigger.execute(experienceEvent);
				SkriptEventHandler.logTriggerEnd(trigger);
			}
		}
		SkriptEventHandler.logEventEnd();

		if (experienceEvent.isCancelled())
			experienceEvent.setSpawnedXP(0);

		if (event instanceof BlockExpEvent) {
			((BlockExpEvent) event).setExpToDrop(experienceEvent.getSpawnedXP());
		} else if (event instanceof EntityDeathEvent) {
			((EntityDeathEvent) event).setDroppedExp(experienceEvent.getSpawnedXP());
		} else if (event instanceof ExpBottleEvent) {
			((ExpBottleEvent) event).setExperience(experienceEvent.getSpawnedXP());
		} else if (event instanceof PlayerFishEvent) {
			((PlayerFishEvent) event).setExpToDrop(experienceEvent.getSpawnedXP());
		}
	};

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean postLoad() {
		TRIGGERS.add(trigger);
		if (REGISTERED_EXECUTORS.compareAndSet(false, true)) {
			EventPriority priority = SkriptConfig.defaultEventPriority.value();
			//noinspection unchecked
			for (Class<? extends Event> clazz : new Class[]{BlockExpEvent.class, EntityDeathEvent.class, ExpBottleEvent.class, PlayerFishEvent.class})
				Bukkit.getPluginManager().registerEvent(clazz, new Listener(){}, priority, EXECUTOR, Skript.getInstance(), true);
		}
		return true;
	}

	@Override
	public void unload() {
		TRIGGERS.remove(trigger);
	}

	@Override
	public boolean check(Event event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEventPrioritySupported() {
		return false;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "experience spawn";
	}

}
 */
public class EvtExperienceSpawn extends SkriptEvent {

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

	private EntityData<?>[] entityData;
	private TransformReason[] reasons;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("transform")) {
			Literal<TransformReason> transformLiteral = (Literal<TransformReason>) args[1];
			reasons = transformLiteral.getArray();
		}
		if (parseResult.hasTag("entity")) {
			Literal<EntityData<?>> entityLiteral = (Literal<EntityData<?>>) args[0];
			entityData = entityLiteral.getArray();
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		EntityTransformEvent entityEvent = (EntityTransformEvent) event;
		boolean reasonMatched = reasons == null || Arrays.stream(reasons).anyMatch(reason -> reason == entityEvent.getTransformReason());
		boolean entityDataMatched = entityData == null || Arrays.stream(entityData).anyMatch(data -> data.isInstance(entityEvent.getEntity()));
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
