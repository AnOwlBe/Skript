package org.skriptlang.skript.bukkit.scoreboard.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Score Board")
@Description("""
	The score board of an objective, team, or player.
	
	Note that you cannot modify the score board of an objective or team.
	""")
@Example("set {_board} to the score board of player")
@Since("INSERT VERSION")
public class ExprScoreBoard extends SimplePropertyExpression<Object, Scoreboard> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			infoBuilder(
				ExprScoreBoard.class,
				Scoreboard.class,
				"score[ ]board",
				"objectives/teams/players",
				false
			)
				.supplier(ExprScoreBoard::new)
				.build()
		);
	}

	@Override
	public @Nullable Scoreboard convert(Object object) {
		return switch (object) {
			case Objective objective -> objective.getScoreboard();
			case Team team -> team.getScoreboard();
			case Player player -> player.getScoreboard();
			default -> null;
		};
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, RESET -> CollectionUtils.array(Scoreboard.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Scoreboard board = null;
		if (delta != null)
			board = (Scoreboard) delta[0];
		if (board == null)
			return;

		for (Object object : getExpr().getArray(event)) {
			if (object instanceof Player player)
				player.setScoreboard(board);

		}
	}

	@Override
	public Class<Scoreboard> getReturnType() {
		return Scoreboard.class;
	}

	@Override
	protected String getPropertyName() {
		return "score board";
	}

}
