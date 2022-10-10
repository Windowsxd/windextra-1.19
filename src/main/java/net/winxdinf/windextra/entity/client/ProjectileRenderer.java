package net.winxdinf.windextra.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.ModBlocks;
import net.winxdinf.windextra.entity.projectile;
import net.winxdinf.windextra.item.ModItems;

public class ProjectileRenderer extends EntityRenderer<projectile> {
    public static final ItemStack stack = new ItemStack(ModBlocks.NIL_BLOCK);
    public double AngleBetween2Points(Vec2f a, Vec2f b) {
        double mA = Math.sqrt(a.x*a.x + a.y*a.y);
        double mB = Math.sqrt(b.x*b.x + b.y*b.y);
        double dp = a.x * b.x + a.y * b.y;
        double milted = Math.abs(mA)*Math.abs(mB);
        double resultRad = Math.acos(dp/milted);

        return resultRad;
    }
    public ProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(projectile entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.translate(0,0.125,0);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(entity.getYaw()));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.getPitch()-90f));
        matrices.scale(0.5f,0.5f,0.5f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
        matrices.pop();
    }
    @Override
    public Identifier getTexture(projectile entity) {
        return null;
    }


}
