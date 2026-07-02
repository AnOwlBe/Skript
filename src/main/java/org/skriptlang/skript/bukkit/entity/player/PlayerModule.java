package org.skriptlang.skript.bukkit.entity.player;

import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.entity.player.elements.PlayerEvents;
import org.skriptlang.skript.bukkit.entity.player.elements.effects.*;
import org.skriptlang.skript.bukkit.entity.player.elements.events.*;
import org.skriptlang.skript.bukkit.entity.player.elements.expressions.*;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class PlayerModule extends HierarchicalAddonModule {

	public PlayerModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	protected void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
			EffBan::register,
			EffKick::register,
			syntaxRegistry -> EvtPlayerGameModeChange.register(syntaxRegistry, eventValueRegistry),
			ExprChatFormat::register,
			ExprChatMessage::register,
			ExprChatRecipients::register,
			ExprGameMode::register,
			ExprJoinMessage::register,
			ExprKickMessage::register,
			ExprOnScreenKickMessage::register,
			ExprPlayerListHeaderFooter::register,
			ExprPlayerListName::register,
			ExprPlayerListPriority::register,
			ExprQuitMessage::register
		);
		if (Skript.classExists("io.papermc.paper.event.player.PlayerPickBlockEvent")) {
			register(addon,
				EvtPlayerPickItem::register,
				ExprPickedItem::register
			);
		}

        EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);
		SyntaxRegistry syntaxRegistry = moduleRegistry(addon);

		PlayerEvents.register(syntaxRegistry, eventValueRegistry);

		EvtPlayerArmorChange.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerFillBucket.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerMoveOn.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerRespawn.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerSpectate.register(syntaxRegistry);

	}

	@Override
	public String name() {
		return "player";
	}

}
