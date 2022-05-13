package vg.civcraft.mc.civmodcore.world.locations.chunkmeta;

import java.util.Objects;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.CivModCorePlugin;
import vg.civcraft.mc.civmodcore.world.locations.chunkmeta.block.BlockBasedChunkMeta;

public class XZWCoord implements Comparable<XZWCoord> {

	/**
	 * Chunk x-coord
	 */
	protected int x;

	/**
	 * Chunk z-coord
	 */
	protected int z;

	/**
	 * Internal ID of the world the chunk is in
	 */
	protected short worldID;

	public XZWCoord(final int x,
					final int z,
					final short worldID) {
		this.x = x;
		this.z = z;
		this.worldID = worldID;
	}

	/**
	 * @return Returns the internal world ID.
	 */
	public short getWorldID() {
		return this.worldID;
	}

	/**
	 * @return Returns the chunk X coordinate.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @return Returns the chunk Z coordinate.
	 */
	public int getZ() {
		return this.z;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d):%d", getX(), getZ(), getWorldID());
	}

	@Override
	public int compareTo(final XZWCoord other) {
		final int worldComp = Short.compare(getWorldID(), other.getWorldID());
		if (worldComp != 0) {
			return worldComp;
		}
		final int xComp = Integer.compare(getX(), other.getX());
		if (xComp != 0) {
			return xComp;
		}
		return Integer.compare(getZ(), other.getZ());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getZ(), getWorldID());
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof final XZWCoord other) {
			return getWorldID() == other.getWorldID()
					&& getX() == other.getX()
					&& getZ() == other.getZ();
		}
		return false;
	}

	/**
	 * Creates a new chunk coordinate based on a given block location and world id. The location's world will be
	 * ignored.
	 *
	 * @param location The given [standard, block-relative] location.
	 * @param worldID The world id for the location.
	 * @return Returns a new chunk coordinate.
	 */
	public static @NotNull XZWCoord fromLocation(final @NotNull Location location,
												 final short worldID) {
		return new XZWCoord(
				BlockBasedChunkMeta.toChunkCoord(location.getBlockX()),
				BlockBasedChunkMeta.toChunkCoord(location.getBlockZ()),
				worldID);
	}

	/**
	 * Creates a new chunk coordinate based on a given block location.
	 *
	 * @param location The given [standard, block-relative] location.
	 * @return Returns a new chunk coordinate.
	 */
	public static @NotNull XZWCoord fromLocation(final @NotNull Location location) {
		return fromLocation(
				location,
				CivModCorePlugin.getInstance().getWorldIdManager().getInternalWorldId(location.getWorld()));
	}

	/**
	 * Creates a new chunk coordinate based on a given chunk.
	 *
	 * @param chunk The given chunk.
	 * @return Returns a new chunk coordinate.
	 */
	public static @NotNull XZWCoord fromChunk(final @NotNull Chunk chunk) {
		return new XZWCoord(
				chunk.getX(),
				chunk.getZ(),
				CivModCorePlugin.getInstance().getWorldIdManager().getInternalWorldId(chunk.getWorld()));
	}

}
