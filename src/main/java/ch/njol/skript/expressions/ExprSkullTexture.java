package ch.njol.skript.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.SkullMeta;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.jetbrains.annotations.Nullable;

@Name("Skull Texture")
@Description("The skull texture for a player head.")
@Example("set the skull texture of {_i} to \"<base64>\"")
@Since("2.15")
public class ExprSkullTexture extends SimplePropertyExpression<ItemType, String> {

	static {
		register(ExprSkullTexture.class, String.class, "[the] (skull|head) texture", "itemtypes");
	}
	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(String.class);
			case DELETE, RESET -> CollectionUtils.array();
			default -> null;
		};
	}
	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		String value = delta == null ? null : (String) delta[0];
		switch (mode) {
			case DELETE, RESET:
				for (ItemType item : getExpr().getArray(event)) {
					if (item.getMaterial() == Material.PLAYER_HEAD) {
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setPlayerProfile(null);
						item.setItemMeta(meta);
					}
				}
				break;
			case SET:
				for (ItemType item : getExpr().getArray(event)) {
					if (item.getMaterial() == Material.PLAYER_HEAD) {
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						PlayerProfile playerProfile = Bukkit.createProfile(java.util.UUID.randomUUID());
						playerProfile.setProperty(new ProfileProperty("textures", value));
						meta.setPlayerProfile(playerProfile);
						item.setItemMeta(meta);

					}
				}
		}

	}

	@Nullable
	@Override
	public String convert(ItemType item) {
		if (!(item.getMaterial() == Material.PLAYER_HEAD)) {
			return null;
		}
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		PlayerProfile profile = meta.getPlayerProfile();
		if (profile == null) {
			return null;
		}
		ProfileProperty texture = profile.getProperties().stream()
			.filter(p -> p.getName().equals("textures"))
			.findFirst()
			.orElse(null);
		if (!(texture == null)) {
			return texture.getValue();
		} else{
			return null;
		}
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	protected String getPropertyName() {
		return "skull texture";
	}

}