package vg.civcraft.mc.civmodcore.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerMoveBlockEvent extends PlayerMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	public PlayerMoveBlockEvent(final @NotNull Player player,
								final @NotNull Location from,
								final @NotNull Location to) {
		super(player, from, to);
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

}
