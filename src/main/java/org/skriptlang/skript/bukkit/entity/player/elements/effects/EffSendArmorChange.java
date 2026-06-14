package org.skriptlang.skript.bukkit.entity.player.elements.effects;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Send Armor Change")
@Description("""
	 Makes a player see an entity's armor as something else.
	 Note that most entities can have armor but it is not visible.
	 """)
@Example("make player see {_entity}'s leggings as diamond leggings")
@Since("INSERT VERSION")
public class EffSendArmorChange extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(EffSendArmorChange.class)
			.supplier(EffSendArmorChange::new)
			.addPatterns("make %players% see %livingentities%'s %equipmentslot% as %itemtype%",
				"make %players% see %livingentities%'s %equipmentslot% as [the|its] (original|normal|actual) [(armor piece|equipment)]")
			.build());
	}

	private Expression<Player> playersExpr;
	private Expression<LivingEntity> entitiesExpr;
	private Expression<EquipmentSlot> equipmentExpr;
	private Expression<ItemType> itemExpr;
	private boolean asOriginal;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		playersExpr = (Expression<Player>) expressions[0];
		entitiesExpr = (Expression<LivingEntity>) expressions[1];
		equipmentExpr = (Expression<EquipmentSlot>) expressions[2];
		asOriginal = matchedPattern == 1;
		if (!asOriginal)
			itemExpr = (Expression<ItemType>) expressions[3];
		return true;
	}

	@Override
	protected void execute(Event event) {
		EquipmentSlot equipment = equipmentExpr.getSingle(event);
		if (equipment == null)
			return;

		Player[] players = playersExpr.getArray(event);
		LivingEntity[] entities = entitiesExpr.getArray(event);
		ItemType itemType = asOriginal ? null : itemExpr.getSingle(event);
		ItemStack item = itemType != null ? itemType.getRandom() : new ItemStack(Material.AIR);

		for (LivingEntity entity : entities)
			for (Player player : players) {
				EntityEquipment entityEquipment = entity.getEquipment();
				ItemStack slotItem = asOriginal && entityEquipment != null ? entityEquipment.getItem(equipment) : item;
				if (slotItem == null) slotItem = new ItemStack(Material.AIR);
				player.sendEquipmentChange(entity, equipment, slotItem);
			}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("make", playersExpr, "see", entitiesExpr, equipmentExpr, "as")
			.appendIf(asOriginal, "its original")
			.appendIf(!asOriginal, itemExpr)
			.toString();
	}

}
