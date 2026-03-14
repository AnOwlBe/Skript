package ch.njol.skript.expressions;

import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.util.coll.CollectionUtils;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
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
import ch.njol.util.Kleenean;

@Name("Pathfinding Target")
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
	protected Entity[] get(Event event) {
		if (!(event instanceof EntityPathfindEvent pathfindEvent))
			return new Entity[0];
		Entity target = pathfindEvent.getTargetEntity();
		return target != null ? new Entity[]{target} : new Entity[0];
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Entity> getReturnType() {
		return Entity.class;
	}

	@Override
	public String toString(@Nullable Event event,boolean debug) {
		return "the pathfinding target";
	}

	@Override
	public Class<? extends Event>[] supportedEvents() {
		return CollectionUtils.array(EntityPathfindEvent.class);
	}

}

