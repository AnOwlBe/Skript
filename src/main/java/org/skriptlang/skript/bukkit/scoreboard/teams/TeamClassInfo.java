package org.skriptlang.skript.bukkit.scoreboard.teams;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.coll.CollectionUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.lang.properties.handlers.base.PropertyHandler;

public class TeamClassInfo extends ClassInfo<Team> {

	public TeamClassInfo() {
		super(Team.class, "team");
		this.user("teams?")
			.name("Team")
			.description("""
				Represents a team.
				A team is simply a grouping object that can have entities added to it.
				Additionally, you can set properties such as friendly fire and/or colour of the team.
				""")
			.parser(new TeamParser())
			.changer(new TeamChangeHandler())
			.since("INSERT VERSION")
			.property(Property.PREFIX,
				"The team's prefix, as text. Can be set or reset.",
				Skript.instance(),
				new TeamPrefixHandler())
			.property(Property.SUFFIX,
				"The team's suffix, as text. Can be set or reset.",
				Skript.instance(),
				new TeamSuffixHandler())
			.defaultExpression(new EventValueExpression<>(Team.class));
	}

	private static class TeamParser extends Parser<Team> {
		//<editor-fold desc="team parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Team team, int flags) {
			return team.getName();
		}

		@Override
		public String toVariableNameString(Team team) {
			return team.getName();
		}
		//</editor-fold>
	}

	private static class TeamChangeHandler implements Changer<Team> {
		//<editor-fold desc="team change handler" defaultstate="collapsed">
		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			if (mode == ChangeMode.DELETE)
				return CollectionUtils.array();
			return null;
		}

		@Override
		public void change(Team[] teams, Object @Nullable [] delta, ChangeMode mode) {
			for (Team team : teams)
				team.unregister();
		}
		//</editor-fold>
	}

	public static class TeamPrefixHandler implements ExpressionPropertyHandler<Team, Component> {
		//<editor-fold desc="team prefix handler" defaultstate="collapsed">

		@Override
		public Component convert(Team team) {
			return team.prefix();
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(Component.class);
				default -> null;
			};
		}

		@Override
		public void change(Team team, Object @Nullable [] delta, ChangeMode mode) {
			Component prefix = null;
			if (delta != null)
				prefix = (Component) delta[0];

			team.prefix(prefix);
		}

		@Override
		public @NotNull Class<Component> returnType() {
			return Component.class;
		}
		//</editor-fold>
	}

	public static class TeamSuffixHandler implements ExpressionPropertyHandler<Team, Component> {
		//<editor-fold desc="team prefix handler" defaultstate="collapsed">
		@Override
		public Component convert(Team team) {
			return team.suffix();
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(Component.class);
				default -> null;
			};
		}

		@Override
		public void change(Team team, Object @Nullable [] delta, ChangeMode mode) {
			Component suffix = null;
			if (delta != null)
				suffix = (Component) delta[0];

			team.suffix(suffix);
		}

		@Override
		public @NotNull Class<Component> returnType() {
			return Component.class;
		}
		//</editor-fold>
	}

}
