package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.slot.EquipmentSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtPlayerInventorySlotChange extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerInventorySlotChange.class, "Player Inventory Slot Change")
			.supplier(EvtPlayerInventorySlotChange::new)
			.addEvent(PlayerInventorySlotChangeEvent.class)
			.addPatterns("[player] inventory slot chang(e|ing)")
			.addDescription("""
				Called when a slot in a player's inventory is changed."
				Warning: setting the event-slot to a new item can result in an infinite loop.
				""")
			.addExample("""
				on inventory slot change:
				    event-item is a diamond
				    send "Nice diamond you got there!" to player
				    chance of 30%:
				        remove 1 diamond from player
				        send "One diamond for me!" to player
				""")
			.addSince("2.7")
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerInventorySlotChangeEvent.class, ItemStack.class)
			.getter(PlayerInventorySlotChangeEvent::getNewItemStack)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerInventorySlotChangeEvent.class, ItemStack.class)
			.getter(PlayerInventorySlotChangeEvent::getOldItemStack)
			.time(Time.PAST)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerInventorySlotChangeEvent.class, Slot.class)
			.getter(event -> {
				PlayerInventory inventory = event.getPlayer().getInventory();
				int slotIndex = event.getSlot();
				// Not all indices point to inventory slots. Equipment, for example
				if (slotIndex >= 36) {
					return new EquipmentSlot(event.getPlayer(), slotIndex);
				} else {
					return new InventorySlot(inventory, slotIndex);
				}
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
			.append("player inventory slot change")
			.toString();
	}

}
