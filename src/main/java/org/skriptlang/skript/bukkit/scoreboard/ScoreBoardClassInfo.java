package org.skriptlang.skript.bukkit.scoreboard;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoardClassInfo extends ClassInfo<Scoreboard> {

	public ScoreBoardClassInfo() {
		super(Scoreboard.class, "scoreboard");
		this.user("score ?boards?")
			.name("ScoreBoard")
			.description("""
				Represents a score board.
				A score board can have teams and objectives registered on them.
				""")
			.parser(new ScoreBoardParser())
			.since("INSERT VERSION")
			.defaultExpression(new EventValueExpression<>(Scoreboard.class));
	}

	private static class ScoreBoardParser extends Parser<Scoreboard> {
		//<editor-fold desc="score board parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Scoreboard board, int flags) {
			if (board == Bukkit.getScoreboardManager().getMainScoreboard())
				return "the server score board";
			return "a score board";
		}

		@Override
		public String toVariableNameString(Scoreboard board) {
			return String.valueOf(board.hashCode());
		}
		//</editor-fold>
	}

}
