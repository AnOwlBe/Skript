package org.skriptlang.skript.common.properties.elements.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.jspecify.annotations.NonNull;
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
public class PropExprPrefix extends PropertyBaseExpression<ExpressionPropertyHandler<?,?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
			PropertyExpression.infoBuilder(PropExprPrefix.class, Object.class, "prefix[es]", "objects", false)
				.supplier(PropExprPrefix::new)
				.build());
	}

	@Override
	public @NonNull Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Property.PREFIX;
	}

}
