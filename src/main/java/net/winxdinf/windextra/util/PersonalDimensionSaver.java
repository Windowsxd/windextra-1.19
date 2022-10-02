package net.winxdinf.windextra.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class PersonalDimensionSaver extends PersistentState {

    private Integer PocketSequence = 0;

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putInt("PocketSequence", PocketSequence);
        return tag;
    }

}
