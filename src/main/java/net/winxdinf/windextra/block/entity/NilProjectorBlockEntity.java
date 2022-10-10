package net.winxdinf.windextra.block.entity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.winxdinf.windextra.Windextra;

public class NilProjectorBlockEntity extends BlockEntity {
    public static final Identifier PROJECTOR_PACKET = new Identifier(Windextra.MODID, "projectorpacket");

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 70;
    private int charges = 0;
    private int maxCharges = 6;
    private static int ticker = 0;

    public NilProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.NILPROJECTORENTITY, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch(index) {
                    case 0: return NilProjectorBlockEntity.this.progress;
                    case 1: return NilProjectorBlockEntity.this.maxProgress;
                    case 2: return NilProjectorBlockEntity.this.charges;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: NilProjectorBlockEntity.this.progress = value;
                    case 1: NilProjectorBlockEntity.this.maxProgress = value;
                    case 2: NilProjectorBlockEntity.this.charges = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    public int getCharges() {
        return this.charges;
    }
    public void setCharges(int charge) {
        this.charges = charge;
        if (!this.world.isClient()) {
            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, PROJECTOR_PACKET, createDataPacket(this));
            }
        }
        markDirty(this.world, this.getPos(), this.getWorld().getBlockState(this.getPos()));
    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("progress", progress);
        nbt.putInt("charges", charges);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("progress");
        charges = nbt.getInt("charges");
    }


    public static PacketByteBuf createDataPacket(NilProjectorBlockEntity entity) {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeInt(entity.charges);
        packet.writeBlockPos(entity.getPos());

        return packet;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, NilProjectorBlockEntity entity) {
        if (world.isClient()) {return;}
        int charges = entity.getCharges();
        ticker++;
        if (ticker%20 == 0) {
            for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, PROJECTOR_PACKET, createDataPacket(entity));
            }
        }
        if (entity.charges != entity.maxCharges) {
            entity.progress++;
        }

        if (entity.progress >= entity.maxProgress) {
            entity.progress = 0;
            if (charges >= entity.maxCharges) {} else {
                entity.setCharges(charges+1);
            }
        }
        markDirty(world, blockPos, blockState);

    }
}
