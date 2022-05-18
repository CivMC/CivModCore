package vg.civcraft.mc.civmodcore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CustomEventMapper implements Listener {

	/**
	 * Glue map for {@link PlayerMoveBlockEvent}.
	 * @param event The event to map from.
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void detectPlayerMoveBlock(final PlayerMoveEvent event) {
		if (EventUtils.didPlayerMoveBlocks(event)) {
			final var blockMoveEvent = new PlayerMoveBlockEvent(event.getPlayer(), event.getFrom(), event.getTo());
			blockMoveEvent.callEvent();
			event.setCancelled(blockMoveEvent.isCancelled());
		}
	}

}
