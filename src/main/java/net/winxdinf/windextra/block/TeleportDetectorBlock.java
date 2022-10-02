package net.winxdinf.windextra.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class TeleportDetectorBlock extends Block {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public TeleportDetectorBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.stateManager.getDefaultState().with(POWERED, false));
    }

    public void togglePower(BlockState state, World world, BlockPos pos, boolean power) {
        world.setBlockState(pos, (BlockState)state.with(POWERED, power), Block.NOTIFY_ALL);
        this.updateNeighbors(state, world, pos);

        //return state;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED) != false) {
            world.setBlockState(pos, (BlockState)state.with(POWERED, false), Block.NOTIFY_ALL);
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.5f);
        }
    }

    /*@Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(POWERED).booleanValue() && random.nextFloat() < 0.25f) {
            TeleportDetectorBlock.spawnParticles(state, world, pos, 0.5f);
        }
    }*/

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

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) != false ? 15 : 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(POWERED).booleanValue()) {
            return 15;
        }
        return 0;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
