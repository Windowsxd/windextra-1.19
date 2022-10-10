package net.winxdinf.windextra.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class NilProjectorBlockEntity extends BlockEntity {

    public NilProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.NILPROJECTORENTITY, pos, state);
    }

}
