package vg.civcraft.mc.civmodcore.utilities;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.list.LazyList;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that fills in the gaps of {@link CollectionUtils}.
 *
 * @author Protonull
 */
@UtilityClass
public class MoreCollectionUtils {

	/**
	 * Creates a new collection with a given set of predefined elements, if any are given.
	 *
	 * @param <T> The type of the elements to store in the collection.
	 * @param constructor The constructor for the collection.
	 * @param elements The elements to add to the collection.
	 * @return Returns a new collection, or null if no constructor was given, or the constructor didn't produce a new
	 * collection.
	 *
	 * @deprecated Use {@link #collectExact(Int2ObjectFunction, Object[])} instead.
	 */
	@Deprecated
	@SafeVarargs
	public <T, K extends Collection<T>> K collect(final Supplier<K> constructor,
												  final T... elements) {
		if (constructor == null) {
			return null;
		}
		final K collection = constructor.get();
		if (collection != null) {
			CollectionUtils.addAll(collection, elements);
		}
		return collection;
	}

	/**
	 * Creates a new collection with the exact size of a given set of predefined elements, if any are given.
	 *
	 * @param <T> The type of the elements to store in the collection.
	 * @param constructor The constructor for the collection.
	 * @param elements The elements to add to the collection.
	 * @return Returns a new collection, or null if no constructor was given, or the constructor didn't produce a new
	 * collection.
	 */
	@SafeVarargs
	public <T, K extends Collection<T>> K collectExact(final Int2ObjectFunction<K> constructor,
													   final T... elements) {
		if (constructor == null) {
			return null;
		}
		if (elements == null) {
			return constructor.get(0);
		}
		final K collection = constructor.get(elements.length);
		if (collection != null) {
			Collections.addAll(collection, elements);
		}
		return collection;
	}

	/**
	 * <p>Tests whether there is at least one element in the given collection that passes the criteria of the given
	 * predicate.</p>
	 *
	 * <p>Emulates: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/some</p>
	 *
	 * @param <T> The type of the collection's elements.
	 * @param collection The collection to iterate.
	 * @param predicate The element tester.
	 * @return Returns true if at least one element passes the predicate test. Or false if the array fails the
	 *         {@link ArrayUtils#isEmpty(Object[]) isNullOrEmpty()} test, or true if the give predicate is null.
	 *
	 * @deprecated Use {@link IterableUtils#matchesAny(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> boolean anyMatch(final Collection<T> collection,
								final Predicate<T> predicate) {
		DeprecationUtils.printDeprecationWarning();
		return predicate != null && IterableUtils.matchesAny(collection, predicate::test);
	}

	/**
	 * <p>Tests whether every element in an collection passes the criteria of the given predicate.</p>
	 *
	 * <p>Emulates: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/every</p>
	 *
	 * @param <T> The type of the collection's elements.
	 * @param collection The collection to iterate.
	 * @param predicate The element tester.
	 * @return Returns true if no element fails the predicate test, or if the array fails the
	 *         {@link ArrayUtils#isEmpty(Object[]) isNullOrEmpty()} test, or if the give predicate is null.
	 *
	 * @deprecated Use {@link IterableUtils#matchesAll(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> boolean allMatch(final Collection<T> collection,
								final Predicate<T> predicate) {
		DeprecationUtils.printDeprecationWarning();
		return predicate != null && IterableUtils.matchesAll(collection, predicate::test);
	}

	/**
	 * @param <T> The type of the collection's elements.
	 * @param collection The collection to ensure the size of.
	 * @param minimumSize The size to ensure.
	 * @param defaultElement The element to place into the collection if expanded.
	 */
	public <T> void ensureMinimumSize(@NotNull final Collection<T> collection,
									  final int minimumSize,
									  final T defaultElement) {
		final int sizeDelta = Math.max(minimumSize, 0) - collection.size();
		if (sizeDelta > 0) {
			for (int i = 0; i < sizeDelta; i++) {
				collection.add(defaultElement);
			}
		}
	}

	/**
	 * Determines whether a given index is safe for a given collection.
	 *
	 * @param <T> The type of the collection's elements.
	 * @param collection The collection to test the index for.
	 * @param index The index to test.
	 * @return Returns true if the given index represents a valid index for the collection; that a returned false would
	 *         necessarily imply a {@link IndexOutOfBoundsException} (or similar) if attempted.
	 */
	public <T> boolean isSafeIndex(final Collection<T> collection,
								   final int index) {
		return collection != null && index >= 0 && index < collection.size();
	}

	/**
	 * Attempts to retrieve an element from a collection based on a given index. If the index is out of bounds, this
	 * function will gracefully return fast, returning null.
	 *
	 * @param <T> The type of the collection's elements.
	 * @param collection The collection to get the element from.
	 * @param index The index of the element.
	 * @return Returns the element, or null.
	 */
	public <T> T getElement(final Collection<T> collection,
							final int index) {
		return isSafeIndex(collection, index) ? null : IterableUtils.get(collection, index);
	}

	/**
	 * Removes the element at the end of the given list.
	 *
	 * @param <T> The type of the list's elements.
	 * @param list The list to remove the last element from.
	 * @return Returns the element removed.
	 */
	public <T> T removeLastElement(final List<T> list) {
		return CollectionUtils.isEmpty(list) ? null : list.remove(list.size() - 1);
	}

	/**
	 * Retrieves a random element from an list of elements.
	 *
	 * @param <T> The type of element.
	 * @param list The list to retrieve a value from.
	 * @return Returns a random element, or null.
	 */
	public <T> T randomElement(final List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		final int size = list.size();
		if (size == 1) {
			return list.get(0);
		}
		return list.get(ThreadLocalRandom.current().nextInt(size));
	}

	/**
	 * Calculates the number of elements that fulfill a given condition.
	 *
	 * @param <T> The type of element.
	 * @param collection The collection to match the elements of.
	 * @param matcher The matcher function itself.
	 * @return Returns the number of elements that match.
	 *
	 * @deprecated Use {@link IterableUtils#countMatches(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> int numberOfMatches(final Collection<T> collection,
								   final Predicate<T> matcher) {
		DeprecationUtils.printDeprecationWarning();
		return matcher == null ? 0 : (int) IterableUtils.countMatches(collection, matcher::test);
	}

	public <T> @NotNull LazyList<T> lazyList(final @NotNull List<Supplier<T>> suppliers) {
		final int size = suppliers.size();
		final LazyList<T> lazyList = LazyList.lazyList(
				new ArrayList<>(size),
				(i) -> suppliers.get(i).get());
		// initialize size of LazyList if size > 0
		if (size > 0) {
			lazyList.get(size - 1); // Ignore highlighter
		}
		return lazyList;
	}

	/**
	 * @param <T> The type of the set's values.
	 * @return Returns a new identity-hash set.
	 */
	public <T> @NotNull Set<T> newIdentityHashSet() {
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}

}
