package org.skriptlang.skript.bukkit.entity.player.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.registrations.Classes;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.lang.converter.Converter;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Map;

public class EvtPlayerArmorChange extends SkriptEvent {

	private static final boolean BODY_SLOT_EXISTS = Skript.fieldExists(EquipmentSlot.class, "BODY");
	private static Converter<PlayerArmorChangeEvent, EquipmentSlot> GET_SLOT;

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPlayerArmorChange.class, "Player Armor Change")
			.supplier(EvtPlayerArmorChange::new)
			.addEvent(PlayerArmorChangeEvent.class)
			.addPatterns("[player] armo[u]r change[d]",
				"[player] %equipmentslot% change[d]")
			.addKeywords("armor", "armour")
			.addDescription("""
				Called when armor pieces of a player are changed.
				""")
			.addExample("""
				on armor change:
					broadcast the old armor item
				""")
			.addExample("""
				on helmet change:
					broadcast "hmm.."
				""")
			.addSince("2.5, 2.11 (equipment slots)")
			.build());

		// get slot function is dependent on version. 1.21.4+ has a new method.
		if (Skript.methodExists(PlayerArmorChangeEvent.class, "getSlot")) {
			GET_SLOT = PlayerArmorChangeEvent::getSlot;
		} else {
			//noinspection deprecation
			Map<SlotType, EquipmentSlot> slotTypeMap = Map.of(
				Enum.valueOf(SlotType.class, "HEAD"), EquipmentSlot.HEAD,
				Enum.valueOf(SlotType.class, "CHEST"), EquipmentSlot.CHEST,
				Enum.valueOf(SlotType.class, "LEGS"), EquipmentSlot.LEGS,
				Enum.valueOf(SlotType.class, "FEET"), EquipmentSlot.FEET);
			GET_SLOT = event -> {
				//noinspection deprecation
				return slotTypeMap.get(event.getSlotType());
			};
		}

		eventValueRegistry.register(EventValue.builder(PlayerArmorChangeEvent.class, EquipmentSlot.class)
			.getter(GET_SLOT)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerArmorChangeEvent.class, ItemStack.class)
			.getter(PlayerArmorChangeEvent::getOldItem)
			.time(Time.PAST)
			.build());

		eventValueRegistry.register(EventValue.builder(PlayerArmorChangeEvent.class, ItemStack.class)
			.getter(PlayerArmorChangeEvent::getNewItem)
			.time(Time.FUTURE)
			.build());
	}

	private @Nullable EquipmentSlot slot = null;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (args.length == 1) {
			//noinspection unchecked
			Literal<EquipmentSlot> slotLiteral = (Literal<EquipmentSlot>) args[0];
			slot = slotLiteral.getSingle();
			if (slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND || (BODY_SLOT_EXISTS && slot == EquipmentSlot.BODY)) {
				Skript.error("You can't detect an armor change event for " + Classes.toString(slot) + ".");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean check(Event event) {
		PlayerArmorChangeEvent playerEvent = (PlayerArmorChangeEvent) event;
		if (slot == null)
			return true;
		return slot == GET_SLOT.convert(playerEvent);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.appendIf(slot == null, "armor change")
			.appendIf(slot != null, switch (slot) {
				case HAND -> "hand";
				case OFF_HAND -> "off hand";
				default -> "unknown";
			}, "changed")
			.toString();
	}

}
