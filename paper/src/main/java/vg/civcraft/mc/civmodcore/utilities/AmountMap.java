package vg.civcraft.mc.civmodcore.utilities;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Objects;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * An attempt to fix the ever-present problem of tracking amounts. There are two default implementations:
 * {@link ArrayMap} and {@link HashMap}. Be aware that this class doesn't support key preprocessing, so you <i>will</i>
 * need to manually call {@link org.bukkit.inventory.ItemStack#asOne()} for example if you intend to use items.
 *
 * @author Protonull
 */
public interface AmountMap<T> extends Object2IntMap<T> {

	/**
	 * Removes "empties", ie: keys with non-positive amounts.
	 */
	default void removeEmpties() {
		values().removeIf((final int amount) -> amount <= 0);
	}

	/**
	 * Computes the amount for a certain key. If the computer returns a non-positive amount, the key is considered
	 * "empty" and will be removed.
	 *
	 * @param key The key to compute the amount of.
	 * @param computer The computer method/lambda to call.
	 * @return Returns the new amount value, or zero if "empty".
	 */
	default int computeAmount(final T key,
							  @NotNull final Int2IntFunction computer) {
		return computeInt(key, (final T _key, Integer amount) -> {
			amount = computer.applyAsInt(amount == null ? defaultReturnValue() : amount);
			return amount <= 0 ? null : amount;
		});
	}

	/**
	 * Convenience method to add a particular amount to a given key.
	 *
	 * @param key The key to add to.
	 * @param amount The amount to add, which can be null.
	 * @return Returns the new amount value, or zero if "empty".
	 */
	default int changeAmountBy(final T key,
							   final int amount) {
		return computeAmount(key, (final int currentAmount) -> currentAmount + amount);
	}

	/**
	 * @deprecated Changing the default value from 0 in an amount map is not supported!
	 */
	@Deprecated
	@Override
	default void defaultReturnValue(final int returnValue) {
		throw new NotImplementedException("Please don't try to change the default return value!");
	}

	// ------------------------------------------------------------
	// Default Implementations
	// ------------------------------------------------------------

	/**
	 * Predefined increment method useful for {@link #computeAmount(Object, Int2IntFunction)}.
	 */
	Int2IntFunction INCREMENT = (final int amount) -> amount + 1;

	/**
	 * Predefined decrement method useful for {@link #computeAmount(Object, Int2IntFunction)}.
	 */
	Int2IntFunction DECREMENT = (final int amount) -> amount - 1;

	/**
	 * Array-map implementation class for {@link AmountMap}.
	 */
	class ArrayMap<T> extends Object2IntArrayMap<T> implements AmountMap<T> {
		public ArrayMap() {
			this(16);
		}
		public ArrayMap(final int size) {
			super(size);
		}
	}

	/**
	 * Hash-map implementation class for {@link AmountMap}.
	 */
	class HashMap<T> extends Object2IntOpenHashMap<T> implements AmountMap<T> {
		public HashMap() {
			this(16);
		}
		public HashMap(final int size) {
			super(size);
		}
	}

	/**
	 * Wrapper class to make an {@link AmountMap} unmodifiable.
	 */
	class Unmodifiable<T> extends Object2IntMaps.UnmodifiableMap<T> implements AmountMap<T> {
		public Unmodifiable(@NotNull final AmountMap<T> map) {
			super(Objects.requireNonNull(map));
		}
	}

}
