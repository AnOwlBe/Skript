package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerLocaleChange extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerQuit.class, "Player Locale Change")
			.supplier(EvtPlayerQuit::new)
			.addEvent(PlayerQuitEvent.class)
			.addPatterns("[player] (language|locale) chang(e|ing)",
			"[player] chang(e|ing) (language|locale)")
			.addDescription("""
				Called after a player changed their language in the game settings.
				You can use the <a href='#ExprLanguage'>language</a> expression to get the current language of the player.
				""")
			.addExample("""
				on language change:
				    player's language starts with "en"
				    send "Hello!" to player
				""")
			.addSince("2.3")
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
			.append("player locale change")
			.toString();
	}

}
