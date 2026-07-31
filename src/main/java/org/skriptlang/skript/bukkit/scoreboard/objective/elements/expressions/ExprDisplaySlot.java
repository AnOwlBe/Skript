package org.skriptlang.skript.bukkit.scoreboard.objective.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

// This may be confused with the display slot (item/blockdata) of a display entity
// open to thoughts
@Name("Display Slot")
@Description("""
	The display slot of an objective.
	Not to be confused with the display slot of a display entity:
	The display slot of an objective controls how it's viewed, \
	for example, `below name` will put it below the player's name, \
	while `sidebar` will put it on the scoreboard.
	
	Can be set, reset, deleted and retrieved.
	""")
@Example("set display slot of {_objective} to below name")
@Since("INSERT VERSION")
public class ExprDisplaySlot extends SimplePropertyExpression<Objective, DisplaySlot> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			infoBuilder(
				ExprDisplaySlot.class,
				DisplaySlot.class,
				"display[ ]slot",
				"objectives",
				false
			)
				.supplier(ExprDisplaySlot::new)
				.build()
		);
	}

	@Override
	public @Nullable DisplaySlot convert(Objective objective) {
		return objective.getDisplaySlot();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, RESET, DELETE -> CollectionUtils.array(DisplaySlot.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		DisplaySlot slot = null;
		if (delta != null)
			slot = (DisplaySlot) delta[0];

		for (Objective objective : getExpr().getArray(event))
			objective.setDisplaySlot(slot);
	}

	@Override
	public Class<DisplaySlot> getReturnType() {
		return DisplaySlot.class;
	}

	@Override
	protected String getPropertyName() {
		return "display slot";
	}

}
