package org.skriptlang.skript.bukkit.bossbar;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.coll.CollectionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

import ch.njol.skript.classes.Changer.ChangeMode;

public class BossBarClassInfo extends ClassInfo<BossBar> {

	public BossBarClassInfo(SkriptAddon addon) {
		super(BossBar.class, "bossbar");
		this.user("boss ?bars?")
			.name("BossBar")
			.description("Represents a boss bar.")
			.since("INSERT VERSION")
			.parser(new BossBarParser())
			.changer(new BossBarChangeHandler())
			.defaultExpression(new EventValueExpression<>(BossBar.class))
			.property(Property.TITLE,
				"The title of a boss bar.",
				addon,
				new BossBarTitleHandler())
			.property(Property.NAME,
				"The name of a boss bar.",
				addon,
				new BossBarTitleHandler())
			.property(Property.PROGRESS,
				"The progress of a boss bar.",
				addon,
				new BossBarProgressHandler())
			.property(Property.STYLE,
				"The style of a boss bar.",
				addon,
				new BossBarStyleHandler())
			.property(Property.VIEWERS, """
				The viewers of a boss bar.
				If you remove a player from viewers of a boss bar they will no longer see the flags of the boss bar.
				""",
				addon,
				new BossBarViewersHandler());
	}

	private static class BossBarParser extends Parser<BossBar> {
		//<editor-fold desc="boss bar parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(BossBar bar, int flags) {
			boolean emptyTitle = bar.getTitle().isEmpty();
			if (bar instanceof KeyedBossBar keyed) {
				if (emptyTitle) {
					return "boss bar with id '" + keyed.getKey() + "'";
				} else {
					return "boss bar with id '" + keyed.getKey() + "' titled '" + bar.getTitle() + "'";
				}
			} else {
				if (emptyTitle) {
					return "boss bar";
				} else {
					return "boss bar titled '" + bar.getTitle() + "'";
				}
			}
		}

		@Override
		public String toVariableNameString(BossBar bar) {
			return toString(bar, 0);
		}
		//</editor-fold>
	}

	private static class BossBarChangeHandler implements Changer<BossBar> {
		//<editor-fold desc="boss bar change handler" defaultstate="collapsed">
		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			if (mode == ChangeMode.DELETE)
				return CollectionUtils.array();
			return null;
		}

		@Override
		public void change(BossBar[] bars, Object @Nullable [] delta, ChangeMode mode) {
			for (BossBar bar : bars) {
				bar.removeAll();
				if (bar instanceof KeyedBossBar keyed) {
					Bukkit.removeBossBar(keyed.getKey());
				}
			}
		}
		//</editor-fold>
	}

	private static class BossBarTitleHandler implements ExpressionPropertyHandler<BossBar, Component> {
		//<editor-fold desc="boss bar title handler" defaultstate="collapsed">
		@Override
		public @Nullable Component convert(BossBar bar) {
			return LegacyComponentSerializer.legacySection().deserialize(bar.getTitle());
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, DELETE, RESET -> CollectionUtils.array(Component.class);
				default -> null;
			};
		}

		@Override
		public void change(BossBar bar, Object @Nullable [] delta, ChangeMode mode) {
			String title = delta == null ? null : 
				LegacyComponentSerializer.legacySection().serialize((Component) delta[0]);
			bar.setTitle(title);
		}

		@Override
		public @NotNull Class<Component> returnType() {
			return Component.class;
		}
		//</editor-fold>
	}

	private static class BossBarProgressHandler implements ExpressionPropertyHandler<BossBar, Double> {
		//<editor-fold desc="boss bar progress handler" defaultstate="collapsed">
		@Override
		public @Nullable Double convert(BossBar bar) {
			return bar.getProgress();
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET, ADD, REMOVE -> CollectionUtils.array(Double.class);
				default -> null;
			};
		}

		@Override
		public void change(BossBar bar, Object @Nullable [] delta, ChangeMode mode) {
			double progress = delta == null ? 0.0 : (double) delta[0];
			if (mode == ChangeMode.ADD) {
				progress += bar.getProgress();
			} else if (mode == ChangeMode.REMOVE) {
				progress -= bar.getProgress();
			}
			bar.setProgress(Math.clamp(progress, 0.0, 1.0));
		}

		@Override
		public @NotNull Class<Double> returnType() {
			return Double.class;
		}
		//</editor-fold>
	}

	private static class BossBarStyleHandler implements ExpressionPropertyHandler<BossBar, BarStyle> {
		//<editor-fold desc="boss bar style handler" defaultstate="collapsed">
		@Override
		public @Nullable BarStyle convert(BossBar bar) {
			return bar.getStyle();
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(BarStyle.class);
				default -> null;
			};
		}

		@Override
		public void change(BossBar bar, Object @Nullable [] delta, ChangeMode mode) {
			BarStyle style = delta == null ? BarStyle.SOLID : (BarStyle) delta[0];
			bar.setStyle(style);
		}

		@Override
		public @NotNull Class<BarStyle> returnType() {
			return BarStyle.class;
		}
		//</editor-fold>
	}

	private static class BossBarViewersHandler implements ExpressionPropertyHandler<BossBar, Player[]> {
		//<editor-fold desc="boss bar viewers handler" defaultstate="collapsed">
		@Override
		public Player @Nullable [] convert(BossBar bar) {
			return bar.getPlayers().toArray(Player[]::new);
		}

		@Override
		public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Player[].class);
				default -> null;
			};
		}

		@Override
		public void change(BossBar bar, Object @Nullable [] delta, ChangeMode mode) {
			Player[] players = delta != null ? (Player[]) delta : null;
			if (players == null && mode != Changer.ChangeMode.RESET)
				return;
			switch (mode) {
				case SET -> {
					bar.removeAll();
					for (Player player : players)
						bar.addPlayer(player);
				}
				case ADD -> {
					for (Player player : players)
						if (!bar.getPlayers().contains(player))
							bar.addPlayer(player);
				}
				case REMOVE -> {
					for (Player player : players)
						bar.removePlayer(player);
				}
				case RESET -> bar.removeAll();
			}
		}

		@Override
		public @NotNull Class<Player[]> returnType() {
			return Player[].class;
		}
		//</editor-fold>
	}

}

