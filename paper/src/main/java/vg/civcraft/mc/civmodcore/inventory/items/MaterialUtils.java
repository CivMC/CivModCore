package vg.civcraft.mc.civmodcore.inventory.items;

import com.destroystokyo.paper.MaterialTags;
import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.Translatable;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.chat.ChatUtils;
import vg.civcraft.mc.civmodcore.utilities.DeprecationUtils;

/**
 * <p>See <a href="https://github.com/Protonull/BukkitReport/tree/master/reports">BukkitReports</a>.</p>
 *
 * <ul>
 *     <li>See also:</li>
 *     <li>{@link MoreTags MoreTags}</li>
 *     <li>{@link SpawnEggUtils SpawnEggUtils}</li>
 *     <li>{@link TreeTypeUtils TreeTypeUtils}</li>
 * </ul>
 */
@UtilityClass
public final class MaterialUtils {

	private static final List<Material> HASH_MATERIALS = new ArrayList<>() {{
		addAll(Tag.WOOL.getValues());
		addAll(MaterialTags.STAINED_GLASS.getValues());
		addAll(MaterialTags.STAINED_GLASS_PANES.getValues());
		addAll(MaterialTags.CONCRETES.getValues());
	}};

	public final Set<Material> MATERIALS = Set.of(Material.values());
	/**
	 * @return Returns a new material set. DO NOT USE {@link java.util.EnumSet<Material>}! As it'll force load all
	 * materials, including all legacy materials, forcing the server into legacy mode. For some reason
	 * {@link Material#values()} doesn't do that... odd.
	 */
	public @NotNull Set<Material> getMaterials() {
		return new HashSet<>(MATERIALS);
	}

	/**
	 * Attempts to retrieve a material by its slug.
	 *
	 * @param value The value to search for a matching material by.
	 * @return Returns a matched material or null.
	 */
	@Nullable
	public static Material getMaterial(@Nullable final String value) {
		return StringUtils.isEmpty(value) ? null : Material.getMaterial(value.toUpperCase());
	}

	/**
	 * {@link ItemUtils#asTranslatable(ItemStack)} is preferable since that will return a translatable based on
	 * additional item data, such as Potion becoming Potion of Water Breathing or Potion of Regeneration.
	 *
	 * @param material The material to translate.
	 * @return Returns a translatable component based on the given material.
	 *
	 * @deprecated Use {@link ChatUtils#asTranslatable(Translatable)} instead.
	 */
	@Deprecated
	@Nonnull
	public static TranslatableComponent asTranslatable(@Nonnull final Material material) {
		DeprecationUtils.printDeprecationWarning();
		return Component.translatable(material.translationKey());
	}

	/**
	 * Checks whether a material is air.
	 * Will also return true if the given material is null.
	 *
	 * @param material The material to check.
	 * @return Returns true if the material is air.
	 */
	public static boolean isAir(@Nullable final Material material) {
		return material == null || material.isAir();
	}

	/**
	 * Gets a random material based on the given objects hashcode.
	 *
	 * @param object Object to base returned material on
	 * @return Material hash of the given object
	 */
	@Nonnull
	public static Material getMaterialHash(@Nullable final Object object) {
		if (object == null) {
			return HASH_MATERIALS.get(0);
		}
		final int index = IntMath.mod(object.hashCode(), HASH_MATERIALS.size());
		return HASH_MATERIALS.get(index);
	}

}
