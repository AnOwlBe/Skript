package org.skriptlang.skript.bukkit.scoreboard.objective.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Render Type")
@Description("""
	The render type of an objective or criteria.
	The render type of objective is how the client will view it.
	Valid options are INTEGER or HEARTS.
	
	Note that you cannot change the render type of a criteria, only objectives.
	""")
@Example("set render type of {_objective} to hearts")
@Example("send default render type of {_criteria}")
@Since("INSERT VERSION")
public class ExprRenderType extends SimplePropertyExpression<Object, RenderType> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			infoBuilder(
				ExprDisplaySlot.class,
				DisplaySlot.class,
				"[default] render[ ]type",
				"objectives/criterias",
				false
			)
				.supplier(ExprDisplaySlot::new)
				.build()
		);
	}

	@Override
	public @Nullable RenderType convert(Object object) {
		return switch (object) {
			case Objective objective -> objective.getRenderType();
			case Criteria criteria -> criteria.getDefaultRenderType();
			default -> null;
		};
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, RESET, DELETE -> CollectionUtils.array(RenderType.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		RenderType renderType = RenderType.INTEGER; // defaults to INTEGER not null
		if (delta != null)
			renderType = (RenderType) delta[0];

		for (Object object : getExpr().getArray(event)) {
			if (object instanceof Objective objective)
				objective.setRenderType(renderType);
		}
	}

	@Override
	public Class<RenderType> getReturnType() {
		return RenderType.class;
	}

	@Override
	protected String getPropertyName() {
		return "render type";
	}

}
