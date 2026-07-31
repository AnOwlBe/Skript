package org.skriptlang.skript.bukkit.scoreboard.objective;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.coll.CollectionUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

public class ObjectiveClassInfo extends ClassInfo<Objective> {

	public ObjectiveClassInfo() {
		super(Objective.class, "objective");
		this.user("objectives?")
			.name("Objective")
			.description("""
				Represents an objective.
				Objectives can be added to a scoreboard and have values added or changed, such as, their display type or render type.
				""")
			.parser(new ObjectiveParser())
			.since("INSERT VERSION")
			.changer(new ObjectiveChangeHandler())
			.defaultExpression(new EventValueExpression<>(Objective.class))
			.property(Property.DISPLAY_NAME,
				"The objective's display name, as text. Can be set or reset.",
				Skript.instance(),
				new ObjectiveDisplayNameHandler()
			);
	}

	private static class ObjectiveParser extends Parser<Objective> {
		//<editor-fold desc="objective parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Objective objective, int flags) {
			return objective.getName();
		}

		@Override
		public String toVariableNameString(Objective objective) {
			return objective.getName();
		}
		//</editor-fold>
	}

	private static class ObjectiveChangeHandler implements Changer<Objective> {
		//<editor-fold desc="objective change handler" defaultstate="collapsed">
		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			if (mode == ChangeMode.DELETE)
				return CollectionUtils.array();
			return null;
		}

		@Override
		public void change(Objective[] objectives, Object @Nullable [] delta, ChangeMode mode) {
			for (Objective objective : objectives)
				objective.unregister();
		}
		//</editor-fold>
	}

	public static class ObjectiveDisplayNameHandler implements ExpressionPropertyHandler<Objective, Component> {
		//<editor-fold desc="objective display name handler" defaultstate="collapsed">
		@Override
		public Component convert(Objective objective) {
			return objective.displayName();
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(Component.class);
				default -> null;
			};
		}

		@Override
		public void change(Objective objective, Object @Nullable [] delta, ChangeMode mode) {
			Component name = null;
			if (delta != null)
				name = (Component) delta[0];

			objective.displayName(name);
		}

		@Override
		public @NotNull Class<Component> returnType() {
			return Component.class;
		}
		//</editor-fold>
	}

}
