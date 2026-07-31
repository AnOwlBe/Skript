package org.skriptlang.skript.bukkit.scoreboard.elements.expressions;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.skript.doc.Example;
import ch.njol.util.Kleenean;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

@Name("Score Board")
@Description("""
	Allows you to either get the main server scoreboard, or create an entirely new one.
	
	Note that new scoreboards are not persistent, and will be lost across a restart.
	However, the main server scoreboard will persist and retain its values across restarts.
	""")
@Example("""
	// TODO
	""")
@Since("INSERT VERSION")
public class ExprSecScoreBoard extends SectionExpression<Scoreboard> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
			SyntaxRegistry.EXPRESSION,
			SyntaxInfo.Expression.builder(ExprSecScoreBoard.class, Scoreboard.class)
				.addPatterns("[a] (new [custom]|custom) score[ ]board",
					"[the] (vanilla|main|server|default) score[ ]board")
				.supplier(ExprSecScoreBoard::new)
				.build()
		);

		eventValueRegistry.register(EventValue.builder(ScoreBoardEvent.class, Scoreboard.class)
			.getter(ScoreBoardEvent::getScoreBoard)
			.patterns("score board")
			.build());
	}

	private Trigger trigger = null;
	private boolean createNew;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
		createNew = matchedPattern == 0;

		if (node != null) {
			trigger = SectionUtils.loadLinkedCode("create scoreboard", (beforeLoading, afterLoading)
				-> loadCode(node, "create scoreboard", beforeLoading, afterLoading, ScoreBoardEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected Scoreboard @Nullable [] get(Event event) {
		Scoreboard board;
		if (createNew) {
			board = Bukkit.getScoreboardManager().getNewScoreboard();
		} else {
			board = Bukkit.getScoreboardManager().getMainScoreboard();
		}

		board.registerNewObjective("", Criteria.DUMMY, Component.text("test"));
		if (trigger == null)
			return new Scoreboard[0];
		ScoreBoardEvent scoreboardEvent = new ScoreBoardEvent(board);
		Variables.withLocalVariables(event, scoreboardEvent, () -> TriggerItem.walk(trigger, scoreboardEvent));
		return new Scoreboard[]{scoreboardEvent.getScoreBoard()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Scoreboard> getReturnType() {
		return Scoreboard.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return createNew ? "a new scoreboard" : "the server scoreboard";
	}

	private static class ScoreBoardEvent extends Event {
		private final Scoreboard board;

		public ScoreBoardEvent(Scoreboard board) {
			this.board = board;
		}

		public Scoreboard getScoreBoard() {
			return board;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}

	}

}
