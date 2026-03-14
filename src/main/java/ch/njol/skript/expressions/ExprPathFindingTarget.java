package ch.njol.skript.expressions;

import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.util.coll.CollectionUtils;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Events;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;

import static org.bukkit.Bukkit.getEntity;

/**
 * @author Peter Güttinger
 */
@Name("Attacker")
@Description({"The pathfinding target in an on entity pathfind event (The entity that the event-entity is pathfinding towards if set)",})
@Example("""
	on pathfind:
		pathfinding target = villager
		broadcast "I suspect a zombie is trying to go towards a villager.."
	""")
@Since("2.15")
@Events({"entity pathfind"})
public class ExprPathFindingTarget extends SimpleExpression<Entity> implements EventRestrictedSyntax {

	static {
		Skript.registerExpression(ExprPathFindingTarget.class, Entity.class, ExpressionType.SIMPLE, "[the] path[ ]finding target [entity]");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
		return true;
	}

	@Override
	public Class<? extends Event>[] supportedEvents() {
		return CollectionUtils.array(EntityPathfindEvent.class);
	}



	@Override
	protected Entity[] get(Event e) {
		if (!(e instanceof EntityPathfindEvent event))
			return new Entity[0];
		return new Entity[]{event.getTargetEntity()};
	}

	@Override
	public Class<? extends Entity> getReturnType() {
		return Entity.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		if (e == null)
			return "the pathfinding target";
		return Classes.getDebugMessage(getSingle(e));
	}

	@Override
	public boolean isSingle() {
		return true;
	}

}

