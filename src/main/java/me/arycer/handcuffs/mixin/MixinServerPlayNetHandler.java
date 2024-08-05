package me.arycer.handcuffs.mixin;

import me.arycer.handcuffs.util.Util;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class MixinServerPlayNetHandler {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void handlePlayerAction(CPlayerDiggingPacket p_147345_1_, CallbackInfo ci) {
        CPlayerDiggingPacket.Action cplayerdiggingpacket$action = p_147345_1_.getAction();
        if (cplayerdiggingpacket$action == CPlayerDiggingPacket.Action.SWAP_ITEM_WITH_OFFHAND && Util.isHandcuffsItem(this.player.getItemInHand(Hand.OFF_HAND))) {
            ci.cancel();
        }
    }
}
