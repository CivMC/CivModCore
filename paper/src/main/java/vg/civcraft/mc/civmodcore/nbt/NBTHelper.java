package vg.civcraft.mc.civmodcore.nbt;

import java.util.UUID;
import java.util.logging.Level;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;
import vg.civcraft.mc.civmodcore.nbt.wrappers.NBTCompound;
import vg.civcraft.mc.civmodcore.utilities.CivLogger;
import vg.civcraft.mc.civmodcore.utilities.UuidUtils;
import vg.civcraft.mc.civmodcore.utilities.serialization.ISerializer;

@UtilityClass
public class NBTHelper {

	private final CivLogger LOGGER = CivLogger.getLogger(NBTHelper.class);

	// ------------------------------------------------------------
	// Location
	// ------------------------------------------------------------

	public final ISerializer.NBT<Location> LOCATION = new ISerializer.NBT<>() {
		private static final String LOCATION_WORLD_KEY = "world";
		private static final String LOCATION_X_KEY = "x";
		private static final String LOCATION_Y_KEY = "y";
		private static final String LOCATION_Z_KEY = "z";
		private static final String LOCATION_YAW_KEY = "yaw";
		private static final String LOCATION_PITCH_KEY = "pitch";
		@Override
		public @NotNull CompoundTag serialize(final @NotNull Location location) {
			final var nbt = new CompoundTag();
			try {
				nbt.putUUID(LOCATION_WORLD_KEY, location.getWorld().getUID());
			}
			catch (final Throwable ignored) {
				nbt.putUUID(LOCATION_WORLD_KEY, UuidUtils.IDENTITY);
			}
			nbt.putDouble(LOCATION_X_KEY, location.getX());
			nbt.putDouble(LOCATION_Y_KEY, location.getY());
			nbt.putDouble(LOCATION_Z_KEY, location.getZ());
			final float yaw = location.getYaw();
			if (yaw != 0) {
				nbt.putFloat(LOCATION_YAW_KEY, yaw);
			}
			final float pitch = location.getPitch();
			if (pitch != 0) {
				nbt.putFloat(LOCATION_PITCH_KEY, pitch);
			}
			return nbt;
		}
		@Override
		public @NotNull Location deserialize(final @NotNull CompoundTag nbt) {
			final UUID worldUUID = nbt.getUUID(LOCATION_WORLD_KEY);
			return new Location(
					UuidUtils.isNullOrIdentity(worldUUID) ? null : Bukkit.getWorld(worldUUID),
					nbt.getDouble(LOCATION_X_KEY),
					nbt.getDouble(LOCATION_Y_KEY),
					nbt.getDouble(LOCATION_Z_KEY),
					nbt.getFloat(LOCATION_YAW_KEY),
					nbt.getFloat(LOCATION_PITCH_KEY));
		}
	};

	/**
	 * @deprecated Use {@link #LOCATION} instead.
	 */
	@Deprecated
	@Contract("!null -> !null")
	public @Nullable Location locationFromNBT(final NBTCompound nbt) {
		return nbt == null ? null : LOCATION.deserialize(nbt.getRAW());
	}

	/**
	 * @deprecated Use {@link #LOCATION} instead.
	 */
	@Deprecated
	@Contract("!null -> !null")
	public @Nullable NBTCompound locationToNBT(final Location location) {
		return location == null ? null : new NBTCompound(LOCATION.serialize(location));
	}

	// ------------------------------------------------------------
	// ItemStack
	// ------------------------------------------------------------

	public final ISerializer.NBT<ItemStack> ITEMSTACK = new ISerializer.NBT<>() {
		@Override
		public @NotNull CompoundTag serialize(final @NotNull ItemStack item) {
			final var nbt = new CompoundTag();
			ItemUtils.getNMSItemStack(item).save(nbt);
			return nbt;
		}
		@Override
		public @NotNull ItemStack deserialize(final @NotNull CompoundTag nbt) {
			return net.minecraft.world.item.ItemStack.of(nbt).getBukkitStack();
		}
	};

	/**
	 * @deprecated Use {@link #ITEMSTACK} instead.
	 */
	@Deprecated
	@Contract("!null -> !null")
	public @Nullable ItemStack itemStackFromNBT(final NBTCompound nbt) {
		return nbt == null ? null : ITEMSTACK.deserialize(nbt.getRAW());
	}

	/**
	 * @deprecated Use {@link #ITEMSTACK} instead.
	 */
	@Deprecated
	@Contract("!null -> !null")
	public @Nullable NBTCompound itemStackToNBT(final ItemStack item) {
		return item == null ? null : new NBTCompound(ITEMSTACK.serialize(item));
	}

	// ------------------------------------------------------------
	// Inventory
	// ------------------------------------------------------------

	public final ISerializer.NBT<Inventory> INVENTORY = new ISerializer.NBT<Inventory>() {
		@Override
		public @NotNull CompoundTag serialize(final @NotNull Inventory inventory) {
			final var nbt = new CompoundTag();
			final ItemStack[] contents = inventory.getContents();
			for (int i = 0; i < contents.length; i++) {
				final ItemStack item = contents[i];
				if (!ItemUtils.isEmptyItem(item)) {
					nbt.put(Integer.toString(i), ITEMSTACK.serialize(item));
				}
			}
			return nbt;
		}
		public void deserialize(final @NotNull Inventory inventory,
								final @NotNull CompoundTag nbt) {
			inventory.clear();
			final ItemStack[] contents = inventory.getContents();
			for (final String key : nbt.getAllKeys()) {
				if (!(nbt.get(key) instanceof final CompoundTag compound)) {
					LOGGER.log(Level.WARNING,
							"Inventory slot [" + key + "] is not a parsable NBT compound!",
							new RuntimeException());
					continue;
				}
				final ItemStack parsed = ITEMSTACK.deserialize(compound);
				if (ItemUtils.isEmptyItem(parsed)) {
					// Just ignore empty items
					continue;
				}
				final int index;
				try {
					index = Integer.parseInt(key);
				}
				catch (final NumberFormatException thrown) {
					LOGGER.log(Level.WARNING,
							"Inventory slot [" + key + "] not a valid number! Item[" + parsed + "] will be ignored.",
							new RuntimeException());
					continue;
				}
				if (index < 0 || index >= contents.length) {
					LOGGER.log(Level.WARNING,
							"Inventory slot [" + index + "] is out of bounds of array[" + contents.length + "]! Item[" + parsed + "] will be ignored.",
							new RuntimeException());
					continue;
				}
				contents[index] = parsed;
			}
			inventory.setContents(contents);
		}
		@Deprecated
		@Override
		public @NotNull Inventory deserialize(final @NotNull CompoundTag raw) {
			throw new NotImplementedException();
		}
	};

}
