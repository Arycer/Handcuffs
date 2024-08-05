package me.arycer.handcuffs.net.s2c;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.arycer.handcuffs.SharedModInitializer;
import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static me.arycer.handcuffs.util.Reference.HANDCUFFS_BACK;
import static me.arycer.handcuffs.util.Reference.HANDCUFFS_FRONT;

public class HandcuffPlayerS2C {
    private final HandcuffType type;
    private final String playerUUID;
    private final String otherPlayerUUID;

    public HandcuffPlayerS2C(HandcuffType type, String playerUUID, String otherPlayerUUID) {
        this.type = type;
        this.playerUUID = playerUUID;
        this.otherPlayerUUID = otherPlayerUUID;
    }

    public HandcuffPlayerS2C(HandcuffType type, UUID playerUUID, UUID otherPlayerUUID) {
        this(type, playerUUID.toString(), otherPlayerUUID.toString());
    }

    public HandcuffPlayerS2C(HandcuffType type, PlayerEntity player, PlayerEntity otherPlayer) {
        this(type, player.getUUID(), otherPlayer.getUUID());
    }

    public HandcuffPlayerS2C(HandcuffType type, PlayerEntity player) {
        this(type, player.getStringUUID(), "");
    }

    public HandcuffPlayerS2C(PacketBuffer buf) {
        this.type = buf.readEnum(HandcuffType.class);
        this.playerUUID = buf.readUtf();
        this.otherPlayerUUID = buf.readUtf();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeEnum(type);
        buf.writeUtf(playerUUID);
        buf.writeUtf(otherPlayerUUID);
    }

    @SuppressWarnings("unchecked")
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            final ClientWorld world = Minecraft.getInstance().level;
            if (world == null) {
                HandcuffPlayerS2C.warnUnable("World is null");
                return;
            }

            final UUID playerUUID = UUID.fromString(this.playerUUID);
            final PlayerEntity player = world.getPlayerByUUID(playerUUID);

            if (!(player instanceof AbstractClientPlayerEntity)) {
                HandcuffPlayerS2C.warnUnable("Player is not an instance of AbstractClientPlayerEntity");
                return;
            }

            final AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) player;
            final PlayerAnimationAccess.PlayerAssociatedAnimationData playerAssociatedData = PlayerAnimationAccess.getPlayerAssociatedData(abstractClientPlayerEntity);
            final ModifierLayer<IAnimation> animationModLayer = (ModifierLayer<IAnimation>) playerAssociatedData.get(Reference.ANIMATION);

            if (animationModLayer == null) {
                HandcuffPlayerS2C.warnUnable("AnimationModLayer is null");
                return;
            }

            if (this.type == HandcuffType.FREE) {
                animationModLayer.setAnimation(null);
                return;
            }

            final ResourceLocation animationResourceLocation = this.type == HandcuffType.BACK ? HANDCUFFS_BACK : HANDCUFFS_FRONT;
            final KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(animationResourceLocation);

            if (keyframeAnimation == null) {
                HandcuffPlayerS2C.warnUnable("KeyframeAnimation is null");
                return;
            }

            animationModLayer.setAnimation(new KeyframeAnimationPlayer(keyframeAnimation));
        });

        return true;
    }

    private static void warnUnable(String reason) {
        SharedModInitializer.LOGGER.warn("Unable to play animation: {}", reason);
    }
}