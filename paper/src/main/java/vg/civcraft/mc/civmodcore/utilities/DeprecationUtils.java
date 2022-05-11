package vg.civcraft.mc.civmodcore.utilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.experimental.StandardException;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * @author Protonull
 */
@UtilityClass
public class DeprecationUtils {

	private final Logger LOGGER = Bukkit.getLogger();

	/**
	 * Prints a warning to Bukkit's console with a stacktrace warning of deprecation usages.
	 */
	public void printDeprecationWarning() {
		LOGGER.log(Level.WARNING,
				"You are using a deprecated feature. Please consider updating to its recommended replacement.",
				new DeprecationException());
	}

	@StandardException
	private static class DeprecationException extends RuntimeException {

	}

}
