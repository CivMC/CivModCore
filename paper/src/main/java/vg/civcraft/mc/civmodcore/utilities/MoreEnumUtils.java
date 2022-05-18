package vg.civcraft.mc.civmodcore.utilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that fills in the gaps of {@link EnumUtils}.
 *
 * @author Protonull
 */
@UtilityClass
public class MoreEnumUtils {

	/**
	 * @param <E> The enum type itself.
	 * @param value The enum value to stringify.
	 * @return Returns a string-version of the given enum, or "null".
	 */
	public <E extends Enum<E>> @NotNull String getName(final E value) {
		return value == null ? "null" : value.name();
	}

	/**
	 * @param <E> The enum type itself.
	 * @param enumClass The enum class to get the names of.
	 * @return Returns a set of all enum-names of the given enum-class.
	 */
	public <E extends Enum<E>> @NotNull Set<String> getNames(final @NotNull Class<E> enumClass) {
		return Stream.of(enumClass.getEnumConstants())
				.map(MoreEnumUtils::getName)
				.collect(Collectors.toSet());
	}

	/**
	 * @param <E> The enum type itself.
	 * @param enums The enums to convert into names.
	 * @return Returns a set of all enum-names of the given enums.
	 */
	public <E extends Enum<E>> @NotNull Set<String> getNames(final E @NotNull [] enums) {
		return Arrays.stream(enums)
				.map(MoreEnumUtils::getName)
				.collect(Collectors.toSet());
	}

	/**
	 * @param <E> The enum type itself.
	 * @param enums The enums to convert into names.
	 * @return Returns a set of all enum-names of the given enums.
	 */
	public <E extends Enum<E>> @NotNull Set<String> getNames(final @NotNull Collection<E> enums) {
		return enums
				.stream()
				.map(MoreEnumUtils::getName)
				.collect(Collectors.toSet());
	}

	/**
	 * @param <E> The enum type itself.
	 * @param enums The enums to join together into a string.
	 * @return Returns a string representing the given enums. Can be empty.
	 */
	public <E extends Enum<E>> @NotNull String join(final E @NotNull [] enums) {
		return StringUtils.join(getNames(enums), ",");
	}

	/**
	 * @param <E> The enum type itself.
	 * @param enums The enums to join together into a string.
	 * @return Returns a string representing the given enums. Can be empty.
	 */
	public <E extends Enum<E>> @NotNull String join(final @NotNull Collection<E> enums) {
		return StringUtils.join(getNames(enums), ",");
	}

}
