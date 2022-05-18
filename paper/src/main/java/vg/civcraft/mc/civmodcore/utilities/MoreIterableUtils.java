package vg.civcraft.mc.civmodcore.utilities;

import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.IterableUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that fills in the gaps of {@link IterableUtils}.
 *
 * @author Protonull
 */
@UtilityClass
public class MoreIterableUtils {

	/**
	 * @param <T> The iterable's element type.
	 * @param iterable The iterable to remove elements from.
	 * @param predicate The method to test entries with. It should return true to remove that entry.
	 */
	public <T> void removeIf(final @NotNull Iterable<T> iterable,
							 final @NotNull Predicate<T> predicate) {
		MoreIteratorUtils.removeIf(iterable.iterator(), predicate);
	}

}
