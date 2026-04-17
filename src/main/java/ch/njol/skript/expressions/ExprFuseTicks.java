package ch.njol.skript.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Timespan.TimePeriod;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Entity Fuse Duration")
@Description("Get or set how long until a Creeper/Primed TNT explodes. For Creepers, the fuse time will be 0 seconds and if set it will be ticking down even if the entity is not currently in exploding animation.")
@Example("send \"Run! That guy is going to explode in %fuse ticks of player's target%\"")
@Example("send the max fuse ticks of target")
@Since("INSERT VERSION")
public class ExprFuseTicks extends SimplePropertyExpression<Entity, Timespan> {

	static {
		register(ExprFuseTicks.class, Timespan.class, "[:max[imum]] fuse (duration|length)", "entities");
	}

	private boolean max;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		max = (parseResult.hasTag("max"));
		return super.init(expressions, matchedPattern, isDelayed, parseResult);
	}

	@Override
	public @Nullable Timespan convert(Entity entity) {
		if (entity instanceof Creeper creeper) {
			return new Timespan(TimePeriod.TICK, (max ? creeper.getMaxFuseTicks() : creeper.getFuseTicks()));
		}
		if (entity instanceof TNTPrimed tntprimed) {
			return new Timespan(TimePeriod.TICK, tntprimed.getFuseTicks());
		}
		return null;
	}


	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (max)
			return null;
		return switch (mode) {
			case ADD, SET, REMOVE -> CollectionUtils.array(Timespan.class);
			case RESET, DELETE -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		Entity[] entities = getExpr().getArray(event);
		int change = delta == null ? 0 : (int) ((Timespan) delta[0]).getAs(Timespan.TimePeriod.TICK);
		switch (mode) {
			case REMOVE:
				change = -change;

			case ADD:
				for (Entity entity : entities)
					if (entity instanceof Creeper creeper) {
						creeper.setFuseTicks(creeper.getFuseTicks() + change);
					} else if (entity instanceof TNTPrimed tntprimed) {
						tntprimed.setFuseTicks(tntprimed.getFuseTicks() + change);
					}

				break;
			case SET:
				for (Entity entity : entities)
					if (entity instanceof Creeper creeper) {
						creeper.setMaxFuseTicks(change);
						creeper.setFuseTicks(change);
					} else if (entity instanceof TNTPrimed tntprimed) {
						tntprimed.setFuseTicks(change);
					}
				break;

			case DELETE:
			case RESET:
				for (Entity entity : entities)
					if (entity instanceof Creeper creeper) {
						creeper.setFuseTicks(creeper.getMaxFuseTicks());
					} else if (entity instanceof TNTPrimed tntprimed) {
						tntprimed.setFuseTicks(80);
					}

				break;
			default:
				assert false;
		}
	}

	@Override
	public Class<Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	protected String getPropertyName() {
		return "fuse time";
	}

}
