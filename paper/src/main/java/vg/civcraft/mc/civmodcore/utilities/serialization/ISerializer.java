package vg.civcraft.mc.civmodcore.utilities.serialization;

import java.util.Objects;
import lombok.Synchronized;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.nbt.NBTDeserializer;
import vg.civcraft.mc.civmodcore.nbt.NBTSerializable;
import vg.civcraft.mc.civmodcore.nbt.NBTSerialization;
import vg.civcraft.mc.civmodcore.nbt.wrappers.NBTCompound;

/**
 * This is a class inspired by {@link NBTSerializable} and {@link PersistentDataType} and is designed to act as a bridge
 * between them while not being specific to either.
 */
public interface ISerializer<T, R> {

	@NotNull R serialize(@NotNull T instance);

	@NotNull T deserialize(@NotNull R raw);

	/**
	 * Template interface for byte serialization.
	 *
	 * @param <C> The type that can be serialised to and from byte data.
	 */
	interface Bytes<C> extends ISerializer<C, byte[]> {

	}

	/**
	 * Template interface for NBT serialization.
	 *
	 * @param <C> The type that can be serialised to and from NBT.
	 */
	interface NBT<C> extends ISerializer<C, CompoundTag> {

	}

	/**
	 * Template that wraps legacy NBT serialization methods.
	 *
	 * @param <C> The type that can be serialised to and from NBT.
	 */
	class LegacyNBT<C extends NBTSerializable> implements ISerializer<C, NBTCompound> {

		private final NBTDeserializer<C> deserializer;

		/**
		 * @param deserializer The serializable's version of {@link NBTSerializable#fromNBT(NBTCompound) fromNBT(NBTCompound)}
		 */
		public LegacyNBT(final @NotNull NBTDeserializer<C> deserializer) {
			this.deserializer = Objects.requireNonNull(deserializer);
		}

		/**
		 * @param serializableClass The serializable's class to extract the deserializer from.
		 */
		public LegacyNBT(final @NotNull Class<C> serializableClass) {
			this(NBTSerialization.getDeserializer(serializableClass));
		}

		/** {@inheritDoc} */
		@Override
		public @NotNull NBTCompound serialize(final @NotNull C instance) {
			final var nbt = new NBTCompound();
			instance.toNBT(nbt);
			return nbt;
		}

		/** {@inheritDoc} */
		@Override
		public @NotNull C deserialize(final @NotNull NBTCompound raw) {
			return this.deserializer.fromNBT(raw);
		}

	}

	/**
	 * Template that wraps PersistentDataContainer serialization.
	 *
	 * @param <C> The <b>C</b>omplex type.
	 * @param <P> The <b>P</b>rimitive type.
	 */
	abstract class PDC<C, P> implements ISerializer<C, P>, PersistentDataType<P, C> {

		private final Class<C> complexClass;
		private final Class<P> primitiveClass;
		private PersistentDataAdapterContext adapter;

		public PDC(final @NotNull Class<C> complexClass,
				   final @NotNull Class<P> primitiveClass) {
			this.complexClass = Objects.requireNonNull(complexClass);
			this.primitiveClass = Objects.requireNonNull(primitiveClass);
		}

		protected final @NotNull PersistentDataAdapterContext getAdapter() {
			return this.adapter;
		}

		/** {@inheritDoc} */
		public final @NotNull Class<C> getComplexType() {
			return this.complexClass;
		}

		/** {@inheritDoc} */
		public final @NotNull Class<P> getPrimitiveType() {
			return this.primitiveClass;
		}

		/** {@inheritDoc} */
		@Deprecated
		@Synchronized
		public final @NotNull P toPrimitive(final @NotNull C complex,
											final @NotNull PersistentDataAdapterContext context) {
			this.adapter = context;
			try {
				return serialize(complex);
			}
			finally {
				this.adapter = null;
			}
		}

		/** {@inheritDoc} */
		@Deprecated
		@Synchronized
		public final @NotNull C fromPrimitive(final @NotNull P primitive,
											  final @NotNull PersistentDataAdapterContext context) {
			this.adapter = context;
			try {
				return deserialize(primitive);
			}
			finally {
				this.adapter = null;
			}
		}

	}

}
