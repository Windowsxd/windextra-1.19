package net.winxdinf.windextra.util;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.winxdinf.windextra.Windextra;
import org.apache.logging.log4j.core.jmx.Server;

import static net.winxdinf.windextra.block.ModBlocks.NIL_BLOCK;
import static net.winxdinf.windextra.block.ModBlocks.NIL_SCAFFOLD;

public class BoxGenner {
    public BlockState randomNilBlockState(BlockPos pos) {
        double randomNumber = Math.random();
        double posind = Math.max(1.5d * ((33d-pos.getY())/33d) - 0.5d, 0d);
        if (pos.getY() == 0) {
            return NIL_SCAFFOLD.getDefaultState();
        } else if (pos.getY() == 33) {
            return NIL_BLOCK.getDefaultState();
        } else {
            if (randomNumber < posind) {
                return NIL_SCAFFOLD.getDefaultState();
            } else {
                return NIL_BLOCK.getDefaultState();
            }
        }
    }
    public void generateBox(int FLineUUIDWasFound, ServerWorld world) {
        for (int blockY = 0; blockY <= 33; blockY++) {
            for (int blockZ = -17; blockZ <= 16; blockZ++) {
                for (int blockX = -17; blockX <= 16; blockX++) {
                    BlockPos rawBlockPos = new BlockPos(blockX, blockY, blockZ);
                    if (blockY == 0 || blockY == 33) {
                        world.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), randomNilBlockState(rawBlockPos));
                    } else if (blockX == -17 || blockZ == -17 || blockX == 16 || blockZ == 16) {
                        world.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), randomNilBlockState(rawBlockPos));
                    }
                }
            }
        }
    }
}
