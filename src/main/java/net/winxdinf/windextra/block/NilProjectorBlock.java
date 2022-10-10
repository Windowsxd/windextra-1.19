package net.winxdinf.windextra.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.entity.CKeyDetectorBlockEntity;
import net.winxdinf.windextra.block.entity.ModBlockEntity;
import net.winxdinf.windextra.block.entity.NilProjectorBlockEntity;
import net.winxdinf.windextra.entity.ModEntities;
import net.winxdinf.windextra.entity.projectile;

import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class NilProjectorBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 1, 7, 9, 14, 9), Block.createCuboidShape(1, 0, 1, 15, 1, 15), BooleanBiFunction.OR);
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    public static final BooleanProperty POWERED = Properties.POWERED;
    public NilProjectorBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) {
            return;
        }
        if (state.get(POWERED).booleanValue()) {
            this.updateNeighbors(state, world, pos);
            /*float f = state.get(POWERED) ? 0.6f : 0.5f;
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, f);
            world.createAndScheduleBlockTick(pos, state.getBlock(), 8);*/
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }


    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }

        boolean power = state.get(POWERED);

        if (power == false && world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, (BlockState)state.with(POWERED, true), Block.NOTIFY_LISTENERS);
            NilProjectorBlockEntity entity = (NilProjectorBlockEntity)world.getBlockEntity(pos);
            int charges = entity.getCharges();
            if (charges > 0) {
                entity.setCharges(charges - 1);

                ProjectileEntity projectileEntity = new projectile(ModEntities.NIL_PROJECTILE, world);
                projectileEntity.setVelocity(0f, 1f, 0f, 1.5f, 1f);
                projectileEntity.setPos(pos.getX()+0.5, pos.getY()+1.1,pos.getZ()+0.5);
                world.spawnEntity(projectileEntity);


            }
        } else if (power == true && world.isReceivingRedstonePower(pos) != true) {
            world.setBlockState(pos, (BlockState)state.with(POWERED, false), Block.NOTIFY_LISTENERS);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NilProjectorBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntity.NILPROJECTORENTITY, NilProjectorBlockEntity::tick);
    }

}
