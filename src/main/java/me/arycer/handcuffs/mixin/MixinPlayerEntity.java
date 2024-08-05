package me.arycer.handcuffs.mixin;

import me.arycer.handcuffs.net.MessageHandler;
import me.arycer.handcuffs.net.s2c.HandcuffPlayerS2C;
import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.IHandcuffAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IHandcuffAccessor {
    @Unique
    private static final DataParameter<String> HANDCUFF_STATUS = EntityDataManager.defineId(MixinPlayerEntity.class, DataSerializers.STRING);

    @Unique
    private static final DataParameter<String> HANDCUFFED_PLAYER = EntityDataManager.defineId(MixinPlayerEntity.class, DataSerializers.STRING);

    @Unique
    private final PlayerEntity handcuffs$player = (PlayerEntity) (Object) this;

    @Unique
    private boolean handcuffs$sentHandcuffPacket = false;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci) {
        nbt.putString("HandcuffStatus", this.handcuffs$getHandcuffType().name());
        nbt.putString("HandcuffedPlayer", this.handcuffs$getHandcuffedUUID());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci) {
        try {
            this.handcuffs$setHandcuffType(HandcuffType.valueOf(nbt.getString("HandcuffStatus").toUpperCase()));
        } catch (IllegalArgumentException e) {
            this.handcuffs$setHandcuffType(HandcuffType.FREE);
        }

        try {
            this.handcuffs$setHandcuffedUUID(nbt.getString("HandcuffedPlayer"));
        } catch (IllegalArgumentException e) {
            this.handcuffs$setHandcuffedUUID("");
        }
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(HANDCUFF_STATUS, HandcuffType.FREE.name());
        this.entityData.define(HANDCUFFED_PLAYER, "");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!this.level.isClientSide && !this.handcuffs$sentHandcuffPacket) {
            MessageHandler.sendToAllPlayers(new HandcuffPlayerS2C(this.handcuffs$getHandcuffType(), handcuffs$player));

            if (handcuffs$player.getServer() != null) {
                for (ServerPlayerEntity player : handcuffs$player.getServer().getPlayerList().getPlayers()) {
                    if (!(player instanceof IHandcuffAccessor) || player == handcuffs$player) {
                        continue;
                    }

                    IHandcuffAccessor accessor = (IHandcuffAccessor) player;
                    MessageHandler.sendToAllPlayers(new HandcuffPlayerS2C(accessor.handcuffs$getHandcuffType(), player));
                }
            }

            this.handcuffs$sentHandcuffPacket = true;
        }
    }

    @Override
    public HandcuffType handcuffs$getHandcuffType() {
        try {
            return HandcuffType.valueOf(this.entityData.get(HANDCUFF_STATUS).toUpperCase());
        } catch (IllegalArgumentException e) {
            return HandcuffType.FREE;
        }
    }

    @Override
    public void handcuffs$setHandcuffType(HandcuffType type) {
        this.entityData.set(HANDCUFF_STATUS, type.name());
    }

    @Override
    public String handcuffs$getHandcuffedUUID() {
        try {
            return this.entityData.get(HANDCUFFED_PLAYER);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void handcuffs$setHandcuffedUUID(@Nonnull String uuid) {
        this.entityData.set(HANDCUFFED_PLAYER, uuid);
    }
}
