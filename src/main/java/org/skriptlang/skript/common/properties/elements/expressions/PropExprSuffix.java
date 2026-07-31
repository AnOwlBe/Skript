package org.skriptlang.skript.common.properties.elements.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.jspecify.annotations.NonNull;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Suffix")
@Description("""
	Represents the suffix of something.
	""")
@Example("""
	on join:
	    set player's tab list name to "%suffix of player% %player%"
	""")
@Example("set {_prefix} to the suffix of {_team}")
@Since("INSERT VERSION")
@RelatedProperty("suffix")
public class PropExprSuffix extends PropertyBaseExpression<ExpressionPropertyHandler<?,?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
			PropertyExpression.infoBuilder(PropExprSuffix.class, Object.class, "suffix[es]", "objects", false)
				.supplier(PropExprSuffix::new)
				.build());
	}

	@Override
	public @NonNull Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Property.SUFFIX;
	}

}
