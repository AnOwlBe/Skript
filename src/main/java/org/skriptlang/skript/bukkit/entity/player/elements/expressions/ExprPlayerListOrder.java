package org.skriptlang.skript.bukkit.entity.player.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Keywords;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Player List Order")
@Description("The order of the player in the player list in the tab menu.")
@Example("""
	on join:
		player has permission "group.mod"
		set the player's tab list order to 5
	""")
@Since("INSERT VERSION")
@Keywords({"tablist", "tab list"})
public class ExprPlayerListOrder extends SimplePropertyExpression<Player, Integer> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION, infoBuilder(ExprPlayerListOrder.class, Integer.class,
			"(player|tab)[ ]list order", "players", false)
			.supplier(ExprPlayerListOrder::new)
			.build());
	}

	@Override
	public Integer convert(Player player) {
		return player.getPlayerListOrder();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, ADD, RESET -> CollectionUtils.array(Integer.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Integer amount = mode == ChangeMode.RESET ? null : (Integer) delta[0];
		switch (mode) {
			case ADD -> {
				assert delta != null;
				for (Player player : getExpr().getArray(event)) {
					player.setPlayerListOrder(Math.max(0, player.getPlayerListOrder() + amount));
				}
			}
			case SET -> {
				assert delta != null;
				for (Player player : getExpr().getArray(event)) {
					player.setPlayerListOrder(Math.max(0, amount));
				}
			}
			case RESET -> {
				for (Player player : getExpr().getArray(event)) {
					player.setPlayerListOrder(0);
				}
			}
		}
	}

	@Override
	public Class<Integer> getReturnType() {
		return Integer.class;
	}

	@Override
	protected String getPropertyName() {
		return "tablist order";
	}
}