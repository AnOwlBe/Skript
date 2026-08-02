package org.skriptlang.skript.bukkit.scoreboard.teams.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.List;

@Name("Team From Key")
@Description("Obtains a team from the specified key in the specified scoreboard.")
@Example("""
    set {_team} to team with key "example" in {_board}
    """)
@Since("INSERT VERSION")
public class ExprTeamFromKey extends SimpleExpression<Team> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprTeamFromKey.class, Team.class)
				.addPatterns("[the] team[s] (from|with) (key|id) %strings% in %scoreboard%")
				.supplier(ExprTeamFromKey::new)
				.build()
		);
	}

	private Expression<String> keys;
	private Expression<Scoreboard> board;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		keys = (Expression<String>) expressions[0];
		board = (Expression<Scoreboard>) expressions[1];
		return true;
	}

	@Override
	protected Team @Nullable [] get(Event event) {
		List<Team> teams = new ArrayList<>();
		Scoreboard board = this.board.getSingle(event);
		if (board == null)
			return new Team[0];

		for (String key : keys.getArray(event)) {
			Team team = board.getTeam(key);
			if (team != null)
			    teams.add(team);
		}
		return teams.toArray(Team[]::new);
	}

	@Override
	public boolean isSingle() {
		return keys.isSingle();
	}

	@Override
	public Class<? extends Team> getReturnType() {
		return Team.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "team from keys" + keys.toString(event, debug);
	}

}
