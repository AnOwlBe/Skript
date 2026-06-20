package org.skriptlang.skript.bukkit.entity.player;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
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
		register(addon,
			EffBan::register,
			EffKick::register,
			ExprChatFormat::register,
			ExprChatMessage::register,
			ExprChatRecipients::register,
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

		EvtPlayerAnimation.register(syntaxRegistry);
		EvtPlayerArmorChange.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerBedEnter.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerBedLeave.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerBreakItem.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerChangeBeaconEffect.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerDamageItem.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerDeepSleep.register(syntaxRegistry);
		EvtPlayerElytraBoost.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerEmptyBucket.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerExprCooldownChange.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerFillBucket.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerFlightToggle.register(syntaxRegistry);
		EvtPlayerHeldItem.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerInventorySlotChange.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerJoin.register(syntaxRegistry);
		EvtPlayerJump.register(syntaxRegistry);
		EvtPlayerKick.register(syntaxRegistry);
		EvtPlayerLocaleChange.register(syntaxRegistry);
		EvtPlayerLogin.register(syntaxRegistry);
		EvtPlayerMendItem.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerMoveOn.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerQuit.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerReadyArrow.register(syntaxRegistry);
		EvtPlayerRespawn.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerRiptide.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerSneakToggle.register(syntaxRegistry);
		EvtPlayerSpectate.register(syntaxRegistry);
		EvtPlayerSprintToggle.register(syntaxRegistry);
		EvtPlayerStopUsingItem.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerSwapHandItems.register(syntaxRegistry);
		EvtPlayerThrowEgg.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerTrade.register(syntaxRegistry, eventValueRegistry);
		EvtPlayerWorldChange.register(syntaxRegistry, eventValueRegistry);

		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(SimpleEvent.class, "Chat")
			.addDescription("Called whenever a player chats.",
				"Use <a href='#ExprChatFormat'>chat format</a> to change message format.",
				"Use <a href='#ExprChatRecipients'>chat recipients</a> to edit chat recipients.")
			.addExample("""
				on chat:
					if the player has permission "owner":
						set the chat format to "<red>[player]<light gray>: <light red>[message]"
					else if the player has permission "admin":
						set the chat format to "<light red>[player]<light gray>: <orange>[message]"
					else: # default message format
						set the chat format to "<orange>[player]<light gray>: <white>[message]"
				""")
			.addSince("1.4.1")
			.addPattern("chat")
			.addEvent(AsyncChatEvent.class)
			.build());
	}

	@Override
	public String name() {
		return "player";
	}

}
