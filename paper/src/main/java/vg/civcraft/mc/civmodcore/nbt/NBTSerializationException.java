package vg.civcraft.mc.civmodcore.nbt;

import java.io.Serial;
import lombok.experimental.StandardException;
import vg.civcraft.mc.civmodcore.nbt.wrappers.NBTCompound;

/**
 * Exception that ought to be used within {@link NBTSerializable#toNBT(NBTCompound)} and
 * {@link NBTSerializable#fromNBT(NBTCompound)}.
 */
@StandardException
public class NBTSerializationException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 606023177729327630L;

}
