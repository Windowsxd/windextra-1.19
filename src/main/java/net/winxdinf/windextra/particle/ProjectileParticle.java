package net.winxdinf.windextra.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.winxdinf.windextra.block.ModBlocks;

import java.util.Random;

public class ProjectileParticle extends SpriteBillboardParticle {

    protected ProjectileParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, x,y,z, xd, yd, zd);
        this.velocityX = xd;
        this.velocityY = yd;
        this.velocityZ = zd;
        this.maxAge = 15;
        this.setSpriteForAge(spriteSet);
    }



    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
            return new ProjectileParticle(world,x,y,z, this.sprites, dx, dy, dz);
        }

    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final ItemStack stack = new ItemStack(ModBlocks.NIL_BLOCK);

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

        Quaternion quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        float scale = Math.abs(((float)this.age/(float)this.maxAge)-1f)*0.5f;


        //quaternion = new Quaternion(new Vec3f(f,g,h), 0f, false);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.translate(f,g,h);
        matrixStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(new Random().nextFloat()*(float)Math.PI));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(new Random().nextFloat()*(float)Math.PI));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(new Random().nextFloat()*(float)Math.PI));
        matrixStack.scale(scale, scale, scale);


        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, 15, OverlayTexture.DEFAULT_UV, matrixStack, immediate, 1);
        matrixStack.pop();
        immediate.draw();
    }

}
