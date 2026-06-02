package org.skriptlang.skript.bukkit.bossbar.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Boss Bar From Entities")
@Description("""
    Returns a boss bar from one or more entities.
    If the entity is not a wither or ender dragon nothing will be returned.
""")
@Example("""
	set title of (boss bar of nearest wither) to "hm"
	""")
@Since("INSERT VERSION")
public class ExprBossBarFromEntity extends SimplePropertyExpression<Entity, BossBar> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
			SyntaxRegistry.EXPRESSION,
			infoBuilder(
				ExprBossBarFromEntity.class,
				BossBar.class,
				"boss[ ]bar",
				"entities",
				true
			)
				.supplier(ExprBossBarFromEntity::new)
				.build()
		);
	}

	@Override
	@Nullable
	public BossBar convert(Entity entity) {
		if (entity instanceof Wither wither)
			return wither.getBossBar();
		if (entity instanceof EnderDragon dragon)
			return dragon.getBossBar();
		return null;
	}


	@Override
	protected String getPropertyName() {
		return "boss bar";
	}

	@Override
	public Class<BossBar> getReturnType() {
		return BossBar.class;
	}

}
