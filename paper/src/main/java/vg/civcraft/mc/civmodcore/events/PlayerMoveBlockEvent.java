package vg.civcraft.mc.civmodcore.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @deprecated Please listen to {@link PlayerMoveEvent} instead and check for
 *             {@link PlayerMoveEvent#hasExplicitlyChangedBlock()}.
 */
@Deprecated
public class PlayerMoveBlockEvent extends PlayerMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	public PlayerMoveBlockEvent(Player player, Location from, Location to) {
		super(player, from, to);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
