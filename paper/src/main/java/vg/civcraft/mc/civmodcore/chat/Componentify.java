package vg.civcraft.mc.civmodcore.chat;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Componentify {

	@NotNull
	private Component INTERNAL_addLocationWorld(@NotNull final Location location) {
		if (location.isWorldLoaded()) {
			return Component.text()
					.content(location.getWorld().getName())
					.hoverEvent(HoverEvent.showText(Component.text("World name")))
					.build();
		}
		else {
			return Component.text()
					.content("<null>")
					.color(NamedTextColor.RED)
					.hoverEvent(HoverEvent.showText(Component.text("World not specified / loaded")))
					.build();
		}
	}

	@NotNull
	public Component fullLocation(@NotNull final Location location) {
		return Component.text()
				.append(
						INTERNAL_addLocationWorld(location),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getX()))
								.color(NamedTextColor.RED)
								.hoverEvent(HoverEvent.showText(Component.text("X"))),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getY()))
								.color(NamedTextColor.GREEN)
								.hoverEvent(HoverEvent.showText(Component.text("Y"))),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getZ()))
								.color(NamedTextColor.BLUE)
								.hoverEvent(HoverEvent.showText(Component.text("Z"))),
						Component.space(),
						Component.text()
								.content(Float.toString(location.getYaw()))
								.color(NamedTextColor.GOLD)
								.hoverEvent(HoverEvent.showText(Component.text("Yaw"))),
						Component.space(),
						Component.text()
								.content(Float.toString(location.getPitch()))
								.color(NamedTextColor.AQUA)
								.hoverEvent(HoverEvent.showText(Component.text("Pitch")))
				)
				.build();
	}

	@NotNull
	public Component blockLocation(@NotNull final Location location) {
		return Component.text()
				.append(
						INTERNAL_addLocationWorld(location),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getBlockX()))
								.color(NamedTextColor.RED)
								.hoverEvent(HoverEvent.showText(Component.text("Block X"))),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getBlockY()))
								.color(NamedTextColor.GREEN)
								.hoverEvent(HoverEvent.showText(Component.text("Block Y"))),
						Component.space(),
						Component.text()
								.content(Double.toString(location.getBlockZ()))
								.color(NamedTextColor.BLUE)
								.hoverEvent(HoverEvent.showText(Component.text("Block Z")))
				)
				.build();
	}

}
