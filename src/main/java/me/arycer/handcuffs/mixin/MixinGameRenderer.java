package me.arycer.handcuffs.mixin;

import me.arycer.handcuffs.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(
            method = "bobView",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bobView(CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Util.isHandcuffed(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }
}
