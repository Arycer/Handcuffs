package me.arycer.handcuffs.mixin;

import me.arycer.handcuffs.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;sendOpenInventory()V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void handleKeybinds(CallbackInfo ci) {
        if (this.player != null && Util.isHandcuffed(this.player)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/inventory/InventoryScreen;<init>(Lnet/minecraft/entity/player/PlayerEntity;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void handleKeybinds_(CallbackInfo ci) {
        if (this.player != null && Util.isHandcuffed(this.player)) {
            ci.cancel();
        }
    }
}
