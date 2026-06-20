package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.BlockStateBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerEmptyBucket extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerEmptyBucket.class, "Player Empty Bucket")
			.supplier(EvtPlayerEmptyBucket::new)
			.addEvent(PlayerBucketEmptyEvent.class)
			.addPatterns("bucket empty[ing]",
				"[player] empty[ing] [a] bucket")
			.addDescription("""
				Called when a player empties a bucket.
				You can also use the <a href='#place'>place event</a> with a check for water or lava.
				""")
			.addExample("""
				on player emptying a bucket:
				""")
			.addSince("1.0")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerBucketEmptyEvent.class, Block.class)
			.getter(event -> event.getBlockClicked().getRelative(event.getBlockFace()))
				.time(Time.PAST)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerBucketEmptyEvent.class, Block.class)
			.getter(event -> {
				BlockState state = event.getBlockClicked().getRelative(event.getBlockFace()).getState();
				state.setType(event.getBucket() == Material.WATER_BUCKET ? Material.WATER : Material.LAVA);
				return new BlockStateBlock(state, true);
			})
			.build());
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("bucket emptying")
			.toString();
	}

}
