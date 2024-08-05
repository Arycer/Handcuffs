package me.arycer.handcuffs.mixin;

import com.mojang.authlib.GameProfile;
import me.arycer.handcuffs.registry.ModItemRegistry;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.arycer.handcuffs.listener.ModEvents.isHandcuffsItem;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {
    @Shadow @Final public PlayerInventory inventory;

    @Inject(method = "drop(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/ItemEntity;", at = @At("TAIL"), cancellable = true)
    private void onDrop(ItemStack stack, boolean p_146097_2_, boolean p_146097_3_, CallbackInfoReturnable<ItemEntity> cir) {
        if (isHandcuffsItem(stack)) {
            this.inventory.offhand.set(0, stack.getItem() == ModItemRegistry.BACK_HANDCUFFS.get() ?
                    new ItemStack(ModItemRegistry.BACK_HANDCUFFS.get()) :
                    new ItemStack(ModItemRegistry.FRONT_HANDCUFFS.get())
            );
            this.inventory.setChanged();
            cir.setReturnValue(null);
        }
    }
}
