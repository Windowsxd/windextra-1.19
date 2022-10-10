package net.winxdinf.windextra.block.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.CKeyDetectorBlock;
import net.winxdinf.windextra.block.ModBlocks;
import net.winxdinf.windextra.block.entity.CKeyDetectorBlockEntity;
import net.winxdinf.windextra.block.entity.NilProjectorBlockEntity;
import net.winxdinf.windextra.item.ModItems;

public class NilProjectorBlockEntityRenderer implements BlockEntityRenderer<NilProjectorBlockEntity> {

    private static ItemStack stack = new ItemStack(Items.GHAST_TEAR, 1);
    private static ItemStack nil = new ItemStack(ModBlocks.NIL_BLOCK, 1);


    public NilProjectorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(NilProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        long time = entity.getWorld().getTime();
        double timeDialated = time + tickDelta;
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.push();
        matrices.translate(0.5, 1.1, 0.5);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(((float)timeDialated) * 20));
        itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
        matrices.pop();
        int charges = entity.getCharges();
        for (int ind = 0; ind<charges; ind++) {
            double offset = (timeDialated/15) + (ind);
            matrices.push();
            matrices.translate(0.5,0.8,0.5);
            matrices.translate(Math.cos(offset)*0.6, Math.sin(offset)*Math.sin(timeDialated/30)*0.2, Math.sin(offset)*0.6);
            matrices.scale(0.3f, 0.3f, 0.3f);
            itemRenderer.renderItem(nil, ModelTransformation.Mode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                    OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}
