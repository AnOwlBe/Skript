package org.skriptlang.skript.bukkit.entity.player.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Send Block Damage")
@Description("""
    Makes a player see a block's damage as something else.
    This will not actually change the block's break progress in any way.
    Note that a single entity can only be breaking 1 block at a time.
    """)
@Example("make player see block damage of target block of player as 100%")
@Example("make player see block damage of block at player as 75% using random integer between 1 and 999")
@Since("INSERT VERSION")
public class EffSendBlockDamage extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(EffSendBlockDamage.class)
			.supplier(EffSendBlockDamage::new)
			.addPatterns("make %players% see [block] damage of %block% as %number% [using %-entity/integer%]",
			"make %players% see [block] damage of %locations% as [the|its] (original|normal|actual) [damage]")
			.build());
	}

	private Expression<Player> playersExpr;
	private Expression<Block> blocksExpr;
	private Expression<Double> amountExpr;
	private Expression<Object> sourceExpr;
	private boolean asOriginal;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		playersExpr = (Expression<Player>) expressions[0];
		blocksExpr = (Expression<Block>) expressions[1];
		asOriginal = matchedPattern == 1;
		if (!asOriginal) {
			amountExpr = (Expression<Double>) expressions[2];
			sourceExpr = (Expression<Object>) expressions[3];
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Player[] players = playersExpr.getArray(event);
		Block[] blocks = blocksExpr.getArray(event);
		if (asOriginal) {
			for (Block block : blocks) {
				for (Player player : players)
					player.sendBlockDamage(block.getLocation(), 0);
			}
		} else {
			Double amount = amountExpr.getSingle(event);
			if (amount == null)
				return;

			if (sourceExpr == null) {
				for (Block block : blocks)
					for (Player player : players)
						player.sendBlockDamage(block.getLocation(), (float) Math.clamp(amount, 0.0, 1.0));
				return;
			}
			Object source = sourceExpr.getSingle(event);
			if (source instanceof Integer sourceId) {
				for (Block block : blocks)
					for (Player player : players)
						player.sendBlockDamage(block.getLocation(), (float) Math.clamp(amount, 0.0, 1.0), sourceId);
			} else if (source instanceof Entity entity) {
				for (Block block : blocks) {
					for (Player player : players)
						player.sendBlockDamage(block.getLocation(), (float) Math.clamp(amount, 0.0, 1.0), entity);
				}
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("make", playersExpr, "see block damage of", blocksExpr, "as")
			.appendIf(asOriginal, "its original block damage")
			.appendIf(!asOriginal, amountExpr)
			.toString();
	}

}
