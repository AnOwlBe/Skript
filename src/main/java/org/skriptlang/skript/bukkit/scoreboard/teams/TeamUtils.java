package org.skriptlang.skript.bukkit.scoreboard.teams;

import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Basic utilities for teams
 */
public class TeamUtils {

	/**
	 * Gets the nearest team color from a skript color.
	 *
	 * @param color the RGB color to find the nearest bar color for
	 * @return the nearest color
	 */
	public static @NonNull NamedTextColor nearest(Color color) {
		return NamedTextColor.nearestTo(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
	}

	/**
	 * Gets an RGB from a team color because Bukkit does not have it.
	 *
	 * @param color the team color to get the RGB color for
	 * @return the corresponding RGB color, or null
	 */
	public static @Nullable Color rgbFromTeamColor(TextColor color) {
		return SkriptColor.fromBukkitColor(org.bukkit.Color.fromRGB(color.red(), color.green(), color.blue()));
	}

}
