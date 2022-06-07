package vg.civcraft.mc.civmodcore.dao;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vg.civcraft.mc.civmodcore.CivModCorePlugin;

/**
 * <p>This class is to be used in <i>other</i> plugin configs so that they can piggyback off CivModCode's database
 * connection instead of defining, parsing, and instantiating their own.</p>
 *
 * <p>Example usage: <pre>{@code
 * database:
 *   ==: vg.civcraft.mc.civmodcore.dao.DefaultDatasource
 * }</pre></p>
 *
 * <p>No other values are needed, nor are considered, so any other information, such as the username/hostname/etc can be
 * commented out to reduce confusion.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultDatasource implements ConfigurationSerializable {

	private static final DefaultDatasource INSTANCE = new DefaultDatasource();

	/**
	 * @return Returns CivModCore's database connection, which <i>may</i> be null.
	 */
	public @Nullable ManagedDatasource getManagedDatasource() {
		return CivModCorePlugin.getInstance().getDatabase();
	}

	@Override
	public @NotNull Map<String, Object> serialize() {
		return Map.of();
	}

	public static @NotNull DefaultDatasource deserialize(final @NotNull Map<String, Object> data) {
		return INSTANCE;
	}

}
