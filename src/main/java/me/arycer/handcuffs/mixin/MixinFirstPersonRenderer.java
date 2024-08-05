package me.arycer.handcuffs.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.IHandcuffAccessor;
import me.arycer.handcuffs.util.Util;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonRenderer.class)
public abstract class MixinFirstPersonRenderer {
    @Inject(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/FirstPersonRenderer;renderPlayerArm(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IFFLnet/minecraft/util/HandSide;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void renderArmWithItem(AbstractClientPlayerEntity player, float p_228405_2_, float p_228405_3_, Hand p_228405_4_, float p_228405_5_, ItemStack p_228405_6_, float p_228405_7_, MatrixStack matrices, IRenderTypeBuffer p_228405_9_, int p_228405_10_, CallbackInfo ci) {
        if (!Util.isHandcuffed(player) ||!(player instanceof IHandcuffAccessor)) {
            return;
        }

        IHandcuffAccessor accessor = (IHandcuffAccessor) player;
        HandcuffType type = accessor.handcuffs$getHandcuffType();

        if (type != HandcuffType.FREE) {
            ci.cancel();
        }
    }
}
