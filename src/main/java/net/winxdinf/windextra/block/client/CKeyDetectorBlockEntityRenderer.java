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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.winxdinf.windextra.block.CKeyDetectorBlock;
import net.winxdinf.windextra.block.entity.CKeyDetectorBlockEntity;
import net.winxdinf.windextra.block.entity.PearlDetectorBlockEntity;
import net.winxdinf.windextra.item.ModItems;

public class CKeyDetectorBlockEntityRenderer implements BlockEntityRenderer<CKeyDetectorBlockEntity> {

    private static ItemStack stack = new ItemStack(ModItems.NIL_KEY, 1);
    private static ItemStack poweredStack = new ItemStack(ModItems.CHARGED_NIL_KEY, 1);


    public CKeyDetectorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(CKeyDetectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.push();
        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 20.0;
        matrices.translate(0.5, 0.5+offset, 0.5);

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 4));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 2));
        var ChosenStack = stack;
        if (entity.getCachedState().get(CKeyDetectorBlock.POWERED) == true) {
            ChosenStack = poweredStack;
        }
        matrices.scale(0.5f, 0.5f, 0.5f);

        itemRenderer.renderItem(ChosenStack, ModelTransformation.Mode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}
