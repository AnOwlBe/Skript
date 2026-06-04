package org.skriptlang.skript.bukkit.bossbar;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.variables.Variables;
import ch.njol.util.coll.CollectionUtils;
import ch.njol.yggdrasil.Fields;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.njol.skript.classes.Changer.ChangeMode;

public class BossBarClassInfo extends ClassInfo<BossBar> {

	public BossBarClassInfo() {
		super(BossBar.class, "bossbar");
		this.user("boss ?bars?")
			.name("BossBar")
			.description("Represents a boss bar.")
			.since("INSERT VERSION")
			.parser(new BossBarParser())
			.changer(new BossBarChangeHandler())
			.serializer(new BossBarSerializer())
			.defaultExpression(new EventValueExpression<>(BossBar.class))
			.property(Property.TITLE,
				"The title of a boss bar.",
				Skript.instance(),
				new BossBarTitleHandler())
			.property(Property.PROGRESS,
				"The progress of a boss bar.",
				Skript.instance(),
				new BossBarProgressHandler())
			.property(Property.STYLE,
				"The style of a boss bar.",
				Skript.instance(),
				new BossBarStyleHandler())
			.property(Property.VIEWERS, """
				The viewers of a boss bar.
				Removing players from a boss bar will remove all of the effects of the flags of the boss bar.
				""",
				Skript.instance(),
				new BossBarViewersHandler());
		Variables.yggdrasil.registerSingleClass(BarColor.class, "bossbar.color");
		Variables.yggdrasil.registerSingleClass(BarFlag.class, "bossbar.flag");
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
					return "boss bar with id '" + keyed.getKey() + "' named '" + bar.getTitle() + "'";
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
				if (bar instanceof KeyedBossBar keyed) {
					Bukkit.removeBossBar(keyed.getKey());
				}
				bar.removeAll();
			}
		}
		//</editor-fold>
	}

	private static class BossBarSerializer extends Serializer<BossBar> {
		//<editor-fold desc="boss bar serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(BossBar bar) {
			Fields fields = new Fields();
			if (bar instanceof KeyedBossBar keyedBar) {
				fields.putObject("key", keyedBar.getKey().toString());
				List<Player> viewers = new ArrayList<>(bar.getPlayers());
				Player[] viewersArray = viewers.toArray(Player[]::new);
				fields.putObject("viewers", viewersArray);
			}
			fields.putObject("title", bar.getTitle());
			fields.putObject("progress", bar.getProgress());
			fields.putObject("style", bar.getStyle());
			fields.putObject("color", bar.getColor());
			List<BarFlag> flags = new ArrayList<>();
			Arrays.stream(BarFlag.values())
				.filter(bar::hasFlag)
				.forEach(flags::add);
			BarFlag[] flagsArray = flags.toArray(BarFlag[]::new);
			fields.putObject("flags", flagsArray);
			return fields;
		}

		@Override
		public void deserialize(BossBar bar, Fields fields) {
			assert false;
		}

		@Override
		protected BossBar deserialize(Fields fields) throws StreamCorruptedException {
			String title = fields.getObject("title", String.class);
			Double progress = fields.getObject("progress", Double.class);
			BarStyle style = fields.getObject("style", BarStyle.class);
			BarColor color = fields.getObject("color", BarColor.class);
			BarFlag[] flags = fields.getObject("flags", BarFlag[].class);
			Player[] viewers = fields.getObject("viewers", Player[].class);
			String stringKey = fields.getObject("key", String.class);
			NamespacedKey key = null;
			if (stringKey != null)
				key = NamespacedKey.fromString(stringKey);
			if (color == null)
				throw new StreamCorruptedException();
			if (style == null)
				throw new StreamCorruptedException();
			BossBar bar;
			if (key != null) {
				bar = Bukkit.createBossBar(key, title, color, style, flags);
			} else {
				bar = Bukkit.createBossBar(title, color, style, flags);
			}
			// for some reason you can't make a boss bar with progress?
			if (progress != null)
				bar.setProgress(progress);
			if (viewers != null) {
				for (Player player : viewers)
					bar.addPlayer(player);
			}
			return bar;
		}

		@Override
		public boolean mustSyncDeserialization() {
			return true;
		}

		@Override
		public boolean canBeInstantiated() {
			return false;
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
				case SET, RESET -> CollectionUtils.array(Component.class);
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
			Double progress = delta != null ? (Double) delta[0] : null;
			switch (mode) {
				case SET -> bar.setProgress(Math.clamp(progress, 0.0, 1.0));
				case RESET -> bar.setProgress(0.0);
				case ADD -> bar.setProgress(Math.clamp(bar.getProgress() + progress, 0.0, 1.0));
				case REMOVE -> bar.setProgress(Math.clamp(bar.getProgress() - progress, 0.0, 1.0));
			}
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

