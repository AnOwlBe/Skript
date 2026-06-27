package org.skriptlang.skript.bukkit.entity.elements.events;

import ch.njol.skript.bukkitutil.HealthUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class EvtEntityDamage extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtEntityDamage.class, "Entity Damage")
			.supplier(EvtEntityDamage::new)
			.addEvent(EntityDamageEvent.class)
			.addPatterns("damag(e|ing) [of:of %-entitydata%] [by:by %-entitydata%]")
			.addDescription("""
				Called when an entity receives damage, e.g. by an attack from another entity, lava, fire, drowning, fall, suffocation, etc.
				See <a href='#Attacked'>attacker/victim/</a> for how to get the victim or attacker in this event.
				""")
			.addExample("""
				on damage of player by player:
				    send "Send you are being attacked.. defend yourself!" to victim
				    send "You better win this fight.." to attacker
				""")
			.addExample("""
				on damage of bee:
				    broadcast "A poor bee was just damaged :("
				    if attacker is a player:
				        send "You monster.." to attacker
				""")
			.addSince("1.0, 2.7 (by entity)")
			.build());
	}

	private EntityData[] byEntityData;
	private EntityData[] ofEntityData;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
		if (parseResult.hasTag("by")) {
			Literal<EntityData> entityLiteral = (Literal<EntityData>) args[1];
			byEntityData = entityLiteral.getArray();
		}
		if (parseResult.hasTag("of")) {
			Literal<EntityData> entityLiteral = (Literal<EntityData>) args[0];
			ofEntityData = entityLiteral.getArray();
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		EntityDamageEvent entityDamageEvent = (EntityDamageEvent) event;
		boolean damagerMatched = !(event instanceof EntityDamageByEntityEvent entityEvent)
			? byEntityData == null
			: byEntityData == null
				|| Arrays.stream(byEntityData)
				.anyMatch(data -> data.isInstance(entityEvent.getDamager()));

		boolean entityMatched = ofEntityData == null
			|| Arrays.stream(ofEntityData)
			.anyMatch(data -> data.isInstance(entityDamageEvent.getEntity()));

		boolean healthMatched = !(entityDamageEvent.getEntity() instanceof LivingEntity entity)
			|| !(HealthUtils.getHealth(entity) <= 0);

		return damagerMatched && entityMatched && healthMatched;

	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("damage")
			.appendIf(ofEntityData != null, "of", ofEntityData)
			.appendIf(byEntityData != null, "by", byEntityData)
			.toString();
	}

}
