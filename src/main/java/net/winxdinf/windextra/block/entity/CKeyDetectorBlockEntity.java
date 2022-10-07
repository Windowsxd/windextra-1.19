package net.winxdinf.windextra.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CKeyDetectorBlockEntity extends BlockEntity {

    public CKeyDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.CKEYDETECTORENTITY, pos, state);
    }

}
