package me.arycer.handcuffs.mixin;

import com.mojang.authlib.GameProfile;
import me.arycer.handcuffs.util.IHandcuffAccessor;
import me.arycer.handcuffs.util.Util;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends PlayerEntity{
    @Unique
    private final ClientPlayerEntity handcuffs$player = (ClientPlayerEntity) (Object) this;

    public MixinClientPlayerEntity(World p_i241920_1_, BlockPos p_i241920_2_, float p_i241920_3_, GameProfile p_i241920_4_) {
        super(p_i241920_1_, p_i241920_2_, p_i241920_3_, p_i241920_4_);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void onDrop(boolean p_225609_1_, CallbackInfoReturnable<Boolean> cir) {
        if (Util.isHandcuffsItem(this.inventory.getItem(this.inventory.selected))) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!Util.isHandcuffed(this)) {
            return;
        }

        if (!(handcuffs$player instanceof IHandcuffAccessor)) {
            return;
        }

        IHandcuffAccessor accessor = (IHandcuffAccessor) handcuffs$player;
        String handcuffedUUID = accessor.handcuffs$getHandcuffedUUID();
        UUID uuid;

        try {
            uuid = UUID.fromString(handcuffedUUID);
        } catch (IllegalArgumentException e) {
            return;
        }

        PlayerEntity handcuffedPlayer = this.level.getPlayerByUUID(uuid);
        if (handcuffedPlayer == null) {
            return;
        }

        if (handcuffedPlayer.distanceTo(this) > 5) {
            double x = handcuffedPlayer.getX() - this.getX();
            double z = handcuffedPlayer.getZ() - this.getZ();
            double distance = Math.sqrt(x * x + z * z);
            double speed = 0.1;
            double xSpeed = x / distance * speed;
            double zSpeed = z / distance * speed;
            this.setDeltaMovement(xSpeed, 0, zSpeed);
        }
    }
}
