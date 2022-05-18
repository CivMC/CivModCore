package vg.civcraft.mc.civmodcore.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility class that fills in the gaps of {@link ArrayUtils}.
 *
 * @author Protonull
 */
@UtilityClass
public class MoreArrayUtils {

	/**
	 * Fills an array with a particular value.
	 *
	 * @param <T> The type of the array.
	 * @param array The array to fill.
	 * @param value The value to fill the array with.
	 * @return Returns the given array with the filled values.
	 */
	public <T> T[] fill(final T[] array,
						final T value) {
		if (ArrayUtils.isNotEmpty(array)) {
			Arrays.fill(array, value);
		}
		return array;
	}

	/**
	 * <p>Tests whether there is at least one element in the given array that passes the criteria of the given
	 * predicate.</p>
	 *
	 * <p>Emulates: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/some</p>
	 *
	 * @param <T> The type of the array's elements.
	 * @param array The array to iterate.
	 * @param predicate The element tester.
	 * @return Returns true if at least one element passes the predicate test. Or false if the array fails the
	 *         {@link ArrayUtils#isEmpty(Object[]) isNullOrEmpty()} test, or true if the give predicate is null.
	 *
	 * @deprecated Use {@link IterableUtils#matchesAny(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> boolean anyMatch(final T[] array,
								final Predicate<T> predicate) {
		DeprecationUtils.printDeprecationWarning();
		return predicate != null && IterableUtils.matchesAny(List.of(array), predicate::test);
	}

	/**
	 * <p>Tests whether every element in an array passes the criteria of the given predicate.</p>
	 *
	 * <p>Emulates: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/every</p>
	 *
	 * @param <T> The type of the array's elements.
	 * @param array The array to iterate.
	 * @param predicate The element tester.
	 * @return Returns true if no element fails the predicate test, or if the array fails the
	 *         {@link ArrayUtils#isEmpty(Object[]) isNullOrEmpty()} test, or if the give predicate is null.
	 *
	 * @deprecated Use {@link IterableUtils#matchesAll(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> boolean allMatch(final T[] array,
								final Predicate<T> predicate) {
		DeprecationUtils.printDeprecationWarning();
		return predicate != null && IterableUtils.matchesAll(List.of(array), predicate::test);
	}

	/**
	 * Determines whether a given index is safe for a given collection.
	 *
	 * @param <T> The type of the array's elements.
	 * @param array The array to test the index for.
	 * @param index The index to test.
	 * @return Returns true if the given index represents a valid index for the array; that a returned false would
	 *         necessarily imply a {@link IndexOutOfBoundsException} (or similar) if attempted.
	 */
	public <T> boolean isSafeIndex(final T[] array,
								   final int index) {
		return array != null && index >= 0 && index < array.length;
	}

	/**
	 * Attempts to retrieve an element from an array based on a given index. If the index is out of bounds, this
	 * function will gracefully return fast, returning null.
	 *
	 * @param <T> The type of the array's elements.
	 * @param array The array to get the element from.
	 * @param index The index of the element.
	 * @return Returns the element, or null.
	 */
	public <T> T getElement(final T[] array,
							final int index) {
		return isSafeIndex(array, index) ? null : array[index];
	}

	/**
	 * Retrieves a random element from an array of elements.
	 *
	 * @param <T> The type of element.
	 * @param array The array to retrieve a value from.
	 * @return Returns a random element, or null.
	 */
	@SafeVarargs
	public <T> T randomElement(final T... array) {
		if (ArrayUtils.isEmpty(array)) {
			return null;
		}
		if (array.length == 1) {
			return array[0];
		}
		return array[ThreadLocalRandom.current().nextInt(array.length)];
	}

	/**
	 * Computes elements, allowing them to be changed to something of the same type.
	 *
	 * @param <T> The type of element.
	 * @param array The array to compute the elements of.
	 * @param mapper The compute function itself.
	 */
	public <T> void computeElements(final T[] array,
									final Function<T, T> mapper) {
		if (ArrayUtils.isNotEmpty(array) && mapper != null) {
			for (int i = 0, l = array.length; i < l; i++) {
				array[i] = mapper.apply(array[i]);
			}
		}
	}

	/**
	 * Calculates the number of elements that fulfill a given condition.
	 *
	 * @param <T> The type of element.
	 * @param array The array to match the elements of.
	 * @param matcher The matcher function itself.
	 * @return Returns the number of elements that match.
	 *
	 * @deprecated Use {@link IterableUtils#countMatches(Iterable, org.apache.commons.collections4.Predicate)} instead.
	 */
	@Deprecated
	public <T> int numberOfMatches(final T[] array,
								   final Predicate<T> matcher) {
		DeprecationUtils.printDeprecationWarning();
		return matcher == null ? 0 : (int) IterableUtils.countMatches(List.of(array), matcher::test);
	}

}
