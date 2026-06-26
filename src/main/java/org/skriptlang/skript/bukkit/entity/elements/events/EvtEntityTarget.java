package org.skriptlang.skript.bukkit.entity.elements.events;


import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
/*
public class EvtEntityTarget extends SkriptEvent {
	static {
		Skript.registerEvent("Target", EvtEntityTarget.class, EntityTargetEvent.class, "[entity] target", "[entity] un[-]target")
				.description("Called when a mob starts/stops following/attacking another entity, usually a player.")
				.examples("on entity target:",
						"\ttarget is a player")
				.since("1.0");
	}

	private boolean target;

	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
		target = matchedPattern == 0;
		return true;
	}

	@Override
	public boolean check(final Event e) {
		return ((EntityTargetEvent) e).getTarget() == null ^ target;
	}

	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "entity " + (target ? "" : "un") + "target";
	}

}
 */

@SuppressWarnings("rawtypes")
public class EvtEntityTarget extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtEntityTarget.class, "Entity Target")
			.supplier(EvtEntityTarget::new)
			.addEvent(EntityTargetEvent.class)
			.addPatterns("[entity] target", "[entity] un[-]target")
			.addDescription("""
				Called when a mob starts/stops following/attacking another entity, usually a player.
				See <a href="Target">target</a>. for how to get the entity being targeted.
				""")
			.addExample("""
				
				""")
			.addSince("1.0, INSERT VERSION (entity data in pattern support)")
			.build());
	}

	private boolean target;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		EntityTargetEvent entityEvent = (EntityTargetEvent) event;
		return target == (entityEvent.getEntity() != null);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append(entityData != null ? entityData : "entity")
			.append("breaking a wooden door")
			.toString();
	}

}
