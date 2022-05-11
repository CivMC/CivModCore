package vg.civcraft.mc.civmodcore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandCompletions.CommandCompletionHandler;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MinecraftMessageKeys;
import com.google.common.base.Strings;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;
import vg.civcraft.mc.civmodcore.utilities.CivLogger;
import vg.civcraft.mc.civmodcore.utilities.UuidUtils;

/**
 * Command registration class wrapper around {@link BukkitCommandManager}.
 *
 * This manager comes with several additional completions and contexts by default:
 *
 * <p>Completions</p>
 * <ul>
 *     <li>"none" - Alias for "nothing"</li>
 *     <li>"allplayers" - All known players</li>
 *     <li>"materials" - All Bukkit materials</li>
 *     <li>"itemMaterials" - All Bukkit item-materials</li>
 * </ul>
 *
 * <p>Contexts:</p>
 * <ul>
 *     <li>"org.bukkit.command.ConsoleCommandSender" - Ensure the sender is the console</li>
 *     <li>"org.bukkit.World" - Parses a Bukkit world without defaulting to the sender's world</li>
 * </ul>
 */
public class CommandManager extends BukkitCommandManager {

	private final CivLogger logger;

	/**
	 * Creates a new command manager for Aikar based commands and tab completions.
	 *
	 * @param plugin The plugin to bind this manager to.
	 */
	public CommandManager(@NotNull final Plugin plugin) {
		super(Objects.requireNonNull(plugin));
		this.logger = CivLogger.getLogger(plugin.getClass(), getClass());
	}

	/**
	 * Will initialise the manager and register both commands and completions. You should only really use this if
	 * you've used {@link CommandManager#reset()} or both {@link #unregisterCommands()} and
	 * {@link #unregisterCompletions()}, otherwise there may be issues.
	 */
	public final void init() {
		registerCommands();
		registerCompletions(getCommandCompletions());
		registerContexts(getCommandContexts());
	}

	/**
	 * This is called as part of {@link CommandManager#init()} and should be overridden by an extending class to
	 * register all (or as many) commands at once.
	 */
	public void registerCommands() {

	}

	/**
	 * This is called as part of {@link CommandManager#init()} and should be overridden by an extending class to
	 * register all (or as many) completions at once, though make sure to call super.
	 *
	 * @param completions The completion manager is given. It is the same manager that can be reached via
	 *                    {@link #getCommandCompletions()}.
	 */
	public void registerCompletions(@NotNull final CommandCompletions<BukkitCommandCompletionContext> completions) {
		completions.registerCompletion("none", (context) -> List.of());
		completions.registerAsyncCompletion("allplayers", (context) ->
				Arrays.stream(Bukkit.getOfflinePlayers())
						.map(OfflinePlayer::getName)
						.filter(Objects::nonNull)
						.toList());
		completions.registerAsyncCompletion("materials", (context) ->
				Arrays.stream(Material.values())
						.map(Enum::name)
						.toList());
		completions.registerAsyncCompletion("itemMaterials", (context) ->
				Arrays.stream(Material.values())
						.filter(ItemUtils::isValidItemMaterial)
						.map(Enum::name)
						.toList());
	}

	/**
	 * This is called as part of {@link CommandManager#init()} and should be overridden by an extending class
	 * to register all (or as many) contexts at once.
	 *
	 * @param contexts The context manager is given. It is the same manager that can be reached via
	 *                 {@link #getCommandContexts()}.
	 */
	public void registerContexts(@NotNull final CommandContexts<BukkitCommandExecutionContext> contexts) {
		contexts.registerIssuerAwareContext(ConsoleCommandSender.class, (context) -> {
			if (context.getSender() instanceof final ConsoleCommandSender console) {
				return console;
			}
			throw new InvalidCommandArgument("Command can only be called from console!", false);
		});
		// Override ACF Bukkit's default behaviour of falling back to the sender's world.
		contexts.registerContext(World.class, (context) -> {
			final String firstArg = context.getFirstArg();
			final World world;
			// Test UUID
			final UUID worldUUID = UuidUtils.fromString(firstArg);
			if (worldUUID != null) {
				world = Bukkit.getWorld(worldUUID);
			}
			else {
				// Otherwise, get from name
				world = Bukkit.getWorld(firstArg);
			}
			if (world != null) {
				context.popFirstArg();
				return world;
			}
			throw new InvalidCommandArgument(MinecraftMessageKeys.INVALID_WORLD);
		});
	}

	/**
	 * Registers a new command and any attached tab completions.
	 *
	 * @param command The command instance to register.
	 * @param forceReplace Whether to force replace any existing command.
	 */
	@Override
	public final void registerCommand(@NotNull final BaseCommand command, final boolean forceReplace) {
		super.registerCommand(Objects.requireNonNull(command), forceReplace);
		this.logger.info("Command [" + command.getClass().getSimpleName() + "] registered.");
		getTabCompletions(command.getClass()).forEach((method, annotation) -> {
			if (annotation.async()) {
				getCommandCompletions().registerAsyncCompletion(annotation.value(), (context) ->
						runCommandCompletion(context, command, annotation.value(), method));
			}
			else {
				getCommandCompletions().registerCompletion(annotation.value(), (context) ->
						runCommandCompletion(context, command, annotation.value(), method));
			}
			this.logger.info("Command Completer [" + annotation.value() + "] registered.");
		});
	}

	/**
	 * Deregisters a command and any attached tab completions.
	 *
	 * @param command The command instance to register.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void unregisterCommand(@NotNull final BaseCommand command) {
		super.unregisterCommand(Objects.requireNonNull(command));
		this.logger.info("Command [" + command.getClass().getSimpleName() + "] unregistered.");
		final Map<String, CommandCompletionHandler<BukkitCommandCompletionContext>> internal;
		try {
			internal = (HashMap<String, CommandCompletionHandler<BukkitCommandCompletionContext>>)
					FieldUtils.readField(getCommandCompletions(), "completionMap", true);
		}
		catch (final Throwable exception) {
			throw new UnsupportedOperationException("Could not get internal completion map.", exception);
		}
		for (final TabComplete complete : getTabCompletions(command.getClass()).values()) {
			internal.remove(complete.value().toLowerCase(Locale.ENGLISH));
			this.logger.info("Command Completer [" + complete.value() + "] unregistered.");
		}
	}

	/**
	 * Resets all command completions.
	 */
	public final void unregisterCompletions() {
		this.completions = null;
	}

	/**
	 * Resets the manager, resetting all commands and completions.
	 */
	public final void reset() {
		unregisterCommands();
		unregisterCompletions();
	}

	// ------------------------------------------------------------
	// Tab Completions
	// ------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private List<String> runCommandCompletion(final BukkitCommandCompletionContext context,
											  final BaseCommand command,
											  final String id,
											  final Method method) {
		try {
			method.setAccessible(true);
			return switch (method.getParameterCount()) {
				case 0 -> (List<String>) method.invoke(command);
				case 1 -> (List<String>) method.invoke(command, context);
				default -> throw new UnsupportedOperationException("Unsupported number of parameters.");
			};
		}
		catch (final Throwable exception) {
			this.logger.log(Level.WARNING,
					"Could not tab complete [@" + id + "]: an error with the handler!", exception);
			return List.of();
		}
	}

	private static Map<Method, TabComplete> getTabCompletions(final Class<? extends BaseCommand> clazz) {
		final var completions = new HashMap<Method, TabComplete>();
		if (clazz == null) {
			return completions;
		}
		for (final Method method : clazz.getDeclaredMethods()) {
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			if (!List.class.isAssignableFrom(method.getReturnType())) {
				continue;
			}
			// TODO add a generic type check here when possible
			switch (method.getParameterCount()) {
				case 0:
					break;
				case 1:
					if (BukkitCommandCompletionContext.class.isAssignableFrom(method.getParameterTypes()[0])) {
						break;
					}
				default:
					continue;
			}
			final TabComplete tabComplete = method.getAnnotation(TabComplete.class);
			if (tabComplete == null) {
				continue;
			}
			if (Strings.isNullOrEmpty(tabComplete.value())) {
				continue;
			}
			completions.put(method, tabComplete);
		}
		return completions;
	}

}
