package vg.civcraft.mc.civmodcore.commands;

import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;

/**
 * This is a separate helper class that allows plugins to use ACF without needing to commit to {@link CommandManager}
 * to get all the additional features it provides. It also allows some degree of optimisation since not all plugins
 * need their own item-material completions (like CivChat2).
 */
@UtilityClass
public class CommandHelpers {
	// ============================================================
	// Completions
	// ============================================================

	/**
	 * ACF already has the "@nothing" completion, but that seems less intuitive than "@none", so this adds "@none".
	 */
	public void registerNoneCompletion(
		   final @NotNull CommandCompletions<?> completions
	) {
		completions.registerStaticCompletion(
				"none",
				List.of()
		);
	}

	/**
	 * Completion for all Bukkit materials.
	 */
	public void registerMaterialsCompletion(
			final @NotNull CommandCompletions<?> completions
	) {
		completions.registerStaticCompletion(
				"materials",
				Arrays.stream(Material.values())
						.map(Enum::name)
						.toList()
		);
	}

	/**
	 * Completion for all Bukkit materials that are items, excluding Air.
	 */
	public void registerItemMaterialsCompletion(
			final @NotNull CommandCompletions<?> completions
	) {
		completions.registerStaticCompletion(
				"itemMaterials",
				Arrays.stream(Material.values())
						.filter(ItemUtils::isValidItemMaterial)
						.map(Enum::name)
						.toList()
		);
	}

	// ============================================================
	// Contexts
	// ============================================================

	/**
	 * Registers a context that requires the command-sender to be the console.
	 */
	public void registerConsoleSenderContext(
			final @NotNull CommandContexts<?> contexts
	) {
		contexts.registerIssuerAwareContext(ConsoleCommandSender.class, (context) -> {
			if (context.getIssuer().getIssuer() instanceof final ConsoleCommandSender console) {
				return console;
			}
			throw new InvalidCommandArgument("Command can only be called from console!", false);
		});
	}
}

