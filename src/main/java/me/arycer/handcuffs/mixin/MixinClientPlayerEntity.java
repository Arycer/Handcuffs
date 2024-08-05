package me.arycer.handcuffs.mixin;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.arycer.handcuffs.listener.ModEvents.isHandcuffsItem;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {
    @Inject(method = "drop(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onDrop(ItemStack itemStack, boolean p_146097_2_, boolean p_146097_3_, CallbackInfoReturnable<ItemEntity> cir) {
        if (isHandcuffsItem(itemStack)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "drop(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onDrop(ItemStack itemStack, boolean p_146097_2_, CallbackInfoReturnable<ItemEntity> cir) {
        if (isHandcuffsItem(itemStack)) {
            cir.setReturnValue(null);
        }
    }
}
