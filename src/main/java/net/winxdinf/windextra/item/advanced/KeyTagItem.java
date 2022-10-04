package net.winxdinf.windextra.item.advanced;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.winxdinf.windextra.util.IEntityDataSaver;

public class KeyTagItem extends Item {
    public KeyTagItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        //entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHIME, SoundCategory.NEUTRAL, 1f, 1f / (user.world.getRandom().nextFloat() * 0.4f + 0.8f));
        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
        boolean isExempt = data.getBoolean("KeyTPExempt");
        if (!user.world.isClient) {
            if (isExempt) {
                data.putBoolean("KeyTPExempt", false);
                user.sendMessage(Text.of("Charged Nil Key Acknowledges "+entity.getName().getString()),true);
            } else {
                data.putBoolean("KeyTPExempt", true);
                user.sendMessage(Text.of("Charged Nil Key Ignores "+entity.getName().getString()),true);
            }
        }
        return ActionResult.success(user.world.isClient);
    }
}
