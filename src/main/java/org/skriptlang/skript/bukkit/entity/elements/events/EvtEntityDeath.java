package org.skriptlang.skript.bukkit.entity.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.log.ErrorQuality;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class EvtEntityDeath extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtEntityDeath.class, "Entity Death")
			.supplier(EvtEntityDeath::new)
			.addEvent(EntityDeathEvent.class)
			.addPatterns("death [entity:of %-entitydatas%]")
			.addDescription("""
			    Called when a living entity (including players) dies.
			    See <a href='#Attacked'>attacker/victim/</a> for how to get the victim or attacker in this event.
			    """)
			.addExample("""
				on death of player:
				    send "You died.. tragic" to victim
				    send "Nice kill.. did you get any loot?" to attacker
				""")
			.addExample("""
				on death of a wither or ender dragon:
				    broadcast "A great boss has been slain today.."
				""")
			.addSince("1.0")
			.build());
	}

	private EntityData[] entityData;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("entity")) {
			Literal<EntityData> entityLiteral = (Literal<EntityData>) args[0];
			entityData = entityLiteral.getArray();
			for (EntityData value : entityData) {
				if (!LivingEntity.class.isAssignableFrom(value.getType())) {
					Skript.error("The death event only works for living entities", ErrorQuality.SEMANTIC_ERROR);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (entityData != null) {
			EntityDeathEvent entityEvent = (EntityDeathEvent) event;
			return Arrays.stream(entityData).anyMatch(entity -> entity.isInstance(entityEvent.getEntity()));
		}
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("death")
			.appendIf(entityData != null, "of", entityData)
			.toString();
	}

}
