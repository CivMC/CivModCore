package vg.civcraft.mc.civmodcore.events;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.inventory.InventoryUtils;

/**
 * Here's a bunch of static methods to make dealing with Bukkit events a little bit easier.
 *
 * @author Protonull
 */
@UtilityClass
public class EventUtils {

	/**
	 * Updates the item used in the given {@link PlayerInteractEvent} to the given item. This utility assumes that the
	 * interaction was done with the player's hands.
	 *
	 * @param event The interact event.
	 * @param item The item to set.
	 */
	public void setPlayerInteractItem(@NotNull final PlayerInteractEvent event,
									  final ItemStack item) {
		event.getPlayer().getInventory().setItem(Objects.requireNonNull(event.getHand()), item);
	}

	/**
	 * When {@link InventoryCloseEvent} is fired, the viewer is not removed from the viewer's list. This is a
	 * convenience method to determine whether, on close, there'll be any remaining viewers of the inventory.
	 *
	 * @param event The close event.
	 * @return Returns true if the inventory has other viewers.
	 */
	public boolean wasInventoryFullyClosed(@NotNull final InventoryCloseEvent event) {
		return !InventoryUtils.hasOtherViewersOtherThan(event.getInventory(), event.getPlayer());
	}

	/**
	 * Given that {@link PrepareItemCraftEvent} cannot be cancelled in the traditional sense, we need to instead
	 * nullify its result.
	 *
	 * @param event The prepare event.
	 */
	public void cancelPrepareItemCraftEvent(@NotNull final PrepareItemCraftEvent event) {
		event.getInventory().setResult(null);
	}

	/**
	 * Determines whether a given {@link PlayerMoveEvent} represents a player moving to another block. Be aware that
	 * this does not check the {@link org.bukkit.World} of the "from" or "to" locations since a world change would use
	 * a different event.
	 *
	 * @param event The move event.
	 * @return Returns true if the player moved to a different block.
	 */
	public boolean didPlayerMoveBlocks(@NotNull final PlayerMoveEvent event) {
		final Location formerLocation = event.getFrom();
		final Location latterLocation = event.getTo();
		return formerLocation.getBlockX() != latterLocation.getBlockX()
				|| formerLocation.getBlockY() != latterLocation.getBlockY()
				|| formerLocation.getBlockZ() != latterLocation.getBlockZ();
	}

}
