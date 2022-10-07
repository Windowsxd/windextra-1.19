package net.winxdinf.windextra.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PearlDetectorBlockEntity extends BlockEntity {

    public PearlDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.PEARLDETECTORENTITY, pos, state);
    }

}
