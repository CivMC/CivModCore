package vg.civcraft.mc.civmodcore.utilities;

import java.util.Iterator;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.IteratorUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that fills in the gaps of {@link IteratorUtils}.
 *
 * @author Protonull
 */
@UtilityClass
public class MoreIteratorUtils {

	/**
	 * @param <T> The iterator's element type.
	 * @param iterator The iterator to remove elements from.
	 * @param predicate The method to test entries with. It should return true to remove that entry.
	 */
	public <T> void removeIf(final @NotNull Iterator<T> iterator,
							 final @NotNull Predicate<T> predicate) {
		while (iterator.hasNext()) {
			if (predicate.test(iterator.next())) {
				iterator.remove();
			}
		}
	}

}
