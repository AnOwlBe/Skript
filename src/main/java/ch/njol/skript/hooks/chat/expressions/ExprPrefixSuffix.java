package ch.njol.skript.hooks.chat.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.hooks.VaultHook;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.skriptlang.skript.lang.script.ScriptWarning.printDeprecationWarning;

@Name("Prefix/Suffix")
@Description("The prefix or suffix as defined in the server's chat plugin.")
@Example("""
	on chat:
		cancel event
		broadcast "%player's prefix%%player's display name%%player's suffix%: %message%" to the player's world
	""")
@Example("set the player's prefix to \"[<red>Admin<reset>] \"")
@Example("clear player's prefix")
@Since("2.0, 2.10 (delete)")
@RequiredPlugins({"Vault", "a chat plugin that supports Vault"})
@Deprecated(since = "INSERT VERSION", forRemoval = true)
// This is deprecated in favor of the prefix/suffix type property
// This is kept and changed to have `chat` forced to allow for no immediate breaking changes
public class ExprPrefixSuffix extends SimplePropertyExpression<Player, String> {

	static {
		register(ExprPrefixSuffix.class, String.class, "chat (1:prefix|2:suffix)", "players");
	}
	
	private boolean prefix;
	
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		prefix = parseResult.mark == 1;
		printDeprecationWarning("Deprecated. This element is deprecated and scheduled for removal in a future release. Please use prefix and/or suffix expression.");
		return super.init(exprs, matchedPattern, isDelayed, parseResult);
	}
	
	@Override
	public String convert(Player player) {
		return prefix ? VaultHook.chat.getPlayerPrefix(player) : VaultHook.chat.getPlayerSuffix(player);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> new Class[] {String.class};
			case RESET, DELETE -> new Class<?>[0];
			default -> null;
		};
	}

	@Override
	public void change(Event event,  Object @Nullable [] delta, ChangeMode mode) {
		CompletableFuture.runAsync(() -> {
			for (Player player : getExpr().getArray(event)) {
				switch (mode) {
					case SET -> {
						if (prefix) {
							VaultHook.chat.setPlayerPrefix(player, (String) delta[0]);
						} else {
							VaultHook.chat.setPlayerSuffix(player, (String) delta[0]);
						}
					}
					case RESET, DELETE -> {
						if (prefix) {
							VaultHook.chat.setPlayerPrefix(player, null);
						} else {
							VaultHook.chat.setPlayerSuffix(player, null);
						}
					}
				}
			}
		}).join();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	protected String getPropertyName() {
		return prefix ? "prefix" : "suffix";
	}
	
}
