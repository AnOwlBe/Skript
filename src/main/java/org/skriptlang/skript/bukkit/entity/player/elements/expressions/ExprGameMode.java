package org.skriptlang.skript.bukkit.entity.player.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Game Mode")
@Description("""
	The gamemode of a player. See <a href="#gamemode">Gamemodes</a>
	""")
@Example("player's gamemode is survival")
@Example("set the player's gamemode to creative")
@Example("set gamemode of player to adventure")
@Since("1.0")
public class ExprGameMode extends PropertyExpression<Player, GameMode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprGameMode.class, GameMode.class)
			.supplier(ExprGameMode::new)
			.addPatterns("[the] game[ ]mode of %players%",
			"%players%'[s] game[ ]mode")
			.build());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
		setExpr((Expression<Player>) expressions[0]);
		return true;
	}

	@Override
	protected GameMode[] get(final Event event, final Player[] source) {
		return get(source, player -> {
			if (getTime() >= 0 && event instanceof PlayerGameModeChangeEvent playerEvent && playerEvent.getPlayer() == player && !Delay.isDelayed(event))
				return playerEvent.getNewGameMode();
			return player.getGameMode();
		});
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == ChangeMode.RESET)
			return CollectionUtils.array(GameMode.class);
		return null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		final GameMode gamemode = delta == null ? Bukkit.getDefaultGameMode() : (GameMode) delta[0];
		for (Player player : getExpr().getArray(event)) {
			if (getTime() >= 0 && event instanceof PlayerGameModeChangeEvent playerEvent && playerEvent.getPlayer() == player && !Delay.isDelayed(event)) {
				if (playerEvent.getNewGameMode() != gamemode)
					playerEvent.setCancelled(true);
			}
			if (gamemode == null)
				return;
			player.setGameMode(gamemode);
		}
	}

	@Override
	public Class<GameMode> getReturnType() {
		return GameMode.class;
	}

	@Override
	public String toString(final @Nullable Event event, final boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("the gamemode of")
			.toString();
	}

	@Override
	public boolean setTime(final int time) {
		return super.setTime(time, PlayerGameModeChangeEvent.class);
	}

}
