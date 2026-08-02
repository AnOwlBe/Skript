package org.skriptlang.skript.common.properties.elements.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Prefix")
@Description("""
	Represents the prefix of something.
	""")
@Example("""
	on chat:
	    set {_msg} to unformatted message
	    set the chat format to formatted "%player's prefix% %player% <reset>-> %{_msg}%"
	""")
@Example("set {_prefix} to the prefix of {_mycoolteam}")
@Since("INSERT VERSION")
@RelatedProperty("prefix")
public class PropExprPrefix extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION,
			PropertyExpression.infoBuilder(PropExprPrefix.class, Object.class, "prefix[:es]", "objects", false)
				.supplier(PropExprPrefix::new)
				.build());
	}

	private boolean isPlural;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		isPlural = parseResult.hasTag("es");
		return super.init(expressions, matchedPattern, isDelayed, parseResult);
	}

	@Override
	public boolean isSingle() {
		return !isPlural;
	}

	@Override
	public @NotNull Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Property.PREFIX;
	}

}
