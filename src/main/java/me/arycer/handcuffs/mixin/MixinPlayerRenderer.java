package me.arycer.handcuffs.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.arycer.handcuffs.util.IHandcuffAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends EntityRenderer<PlayerEntity> {

    protected PlayerRendererMixin(EntityRendererManager renderManager) {
        super(renderManager);
    }
    private static final ConcurrentHashMap<UUID, UUID> leashers = new ConcurrentHashMap<>();

    @Inject(method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", at = @At("HEAD"))
    public void onRender(AbstractClientPlayerEntity player, float p_225623_2_, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225623_6_, CallbackInfo ci) {
        if (!(player instanceof IHandcuffAccessor)) {
            return;
        }

        IHandcuffAccessor handcuffsPlayer = (IHandcuffAccessor) player;
        String handcuffedUuidString = handcuffsPlayer.handcuffs$getHandcuffedUUID();
        UUID handcuffedUUID;

        try {
            handcuffedUUID = UUID.fromString(handcuffedUuidString);
        } catch (IllegalArgumentException e) {
            return;
        }

        PlayerEntity handcuffedPlayer = player.level.getPlayerByUUID(handcuffedUUID);
        if (handcuffedPlayer != null) {
            UUID playerUUID = player.getUUID();
            UUID existingLeash = leashers.get(handcuffedUUID);

            if (existingLeash == null) {
                leashers.put(playerUUID, handcuffedUUID);
                renderLeash(player, partialTicks, matrixStack, buffer, handcuffedPlayer);
            } else if (existingLeash.equals(playerUUID)) {
                renderLeash(player, partialTicks, matrixStack, buffer, handcuffedPlayer);
            }
        }
    }

    private <E extends Entity> void renderLeash(PlayerEntity player, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, E leashHolder) {
        matrixStack.pushPose();

        Vector3d leashHolderPosition = leashHolder.getRopeHoldPosition(partialTicks);
        double bodyRotation = (double)(MathHelper.lerp(partialTicks, player.yBodyRot, player.yBodyRotO) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vector3d leashOffset = player.getRopeHoldPosition(partialTicks).subtract(player.position());
        double offsetX = Math.cos(bodyRotation) * leashOffset.z + Math.sin(bodyRotation) * leashOffset.x;
        double offsetZ = Math.sin(bodyRotation) * leashOffset.z - Math.cos(bodyRotation) * leashOffset.x;
        double interpolatedPosX = MathHelper.lerp(partialTicks, player.xo, player.getX()) + offsetX;
        double interpolatedPosY = MathHelper.lerp(partialTicks, player.yo, player.getY()) + leashOffset.y;
        double interpolatedPosZ = MathHelper.lerp(partialTicks, player.zo, player.getZ()) + offsetZ;

        matrixStack.translate(offsetX, leashOffset.y, offsetZ);

        float deltaX = (float)(leashHolderPosition.x - interpolatedPosX);
        float deltaY = (float)(leashHolderPosition.y - interpolatedPosY);
        float deltaZ = (float)(leashHolderPosition.z - interpolatedPosZ);
        float leashWidth = 0.025F;

        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.leash());
        Matrix4f matrix = matrixStack.last().pose();

        float distanceSqrt = MathHelper.fastInvSqrt(deltaX * deltaX + deltaZ * deltaZ) * leashWidth / 2.0F;
        float f5 = deltaZ * distanceSqrt;
        float f6 = deltaX * distanceSqrt;

        BlockPos playerEyePos = new BlockPos(player.getEyePosition(partialTicks));
        BlockPos leashHolderEyePos = new BlockPos(leashHolder.getEyePosition(partialTicks));

        int playerBlockLight = this.getBlockLightLevel(player, playerEyePos);
        int leashHolderBlockLight = this.entityRenderDispatcher.getRenderer(leashHolder).getBlockLightLevel(leashHolder, leashHolderEyePos);
        int playerSkyLight = player.level.getBrightness(LightType.SKY, playerEyePos);
        int leashHolderSkyLight = player.level.getBrightness(LightType.SKY, leashHolderEyePos);

        renderSide(vertexBuilder, matrix, deltaX, deltaY, deltaZ, playerBlockLight, leashHolderBlockLight, playerSkyLight, leashHolderSkyLight, leashWidth, leashWidth, f5, f6);
        renderSide(vertexBuilder, matrix, deltaX, deltaY, deltaZ, playerBlockLight, leashHolderBlockLight, playerSkyLight, leashHolderSkyLight, leashWidth, 0.0F, f5, f6);

        matrixStack.popPose();
    }

    private static void renderSide(IVertexBuilder vertexBuilder, Matrix4f matrix, float deltaX, float deltaY, float deltaZ, int playerBlockLight, int leashHolderBlockLight, int playerSkyLight, int leashHolderSkyLight, float leashWidth1, float leashWidth2, float f5, float f6) {
        int i = 24;

        for (int j = 0; j < 24; ++j) {
            float f = (float)j / 23.0F;
            int k = (int)MathHelper.lerp(f, (float)playerBlockLight, (float)leashHolderBlockLight);
            int l = (int)MathHelper.lerp(f, (float)playerSkyLight, (float)leashHolderSkyLight);
            int i1 = LightTexture.pack(k, l);
            addVertexPair(vertexBuilder, matrix, i1, deltaX, deltaY, deltaZ, leashWidth1, leashWidth2, 24, j, false, f5, f6);
            addVertexPair(vertexBuilder, matrix, i1, deltaX, deltaY, deltaZ, leashWidth1, leashWidth2, 24, j + 1, true, f5, f6);
        }
    }

    private static void addVertexPair(IVertexBuilder vertexBuilder, Matrix4f matrix, int light, float deltaX, float deltaY, float deltaZ, float leashWidth1, float leashWidth2, int segments, int segment, boolean swap, float f5, float f6) {
        float r = 0.5F;
        float g = 0.5F;
        float b = 0.5F;
        if (segment % 2 == 0) {
            r *= 0.7F;
            g *= 0.7F;
            b *= 0.7F;
        }

        float f3 = (float)segment / (float)segments;
        float f4 = deltaX * f3;
        float f5_adjusted = deltaY > 0.0F ? deltaY * f3 * f3 : deltaY - deltaY * (1.0F - f3) * (1.0F - f3);
        float f6_adjusted = deltaZ * f3;
        if (!swap) {
            vertexBuilder.vertex(matrix, f4 + f5, f5_adjusted + leashWidth1 - leashWidth2, f6_adjusted - f6).color(r, g, b, 1.0F).uv2(light).endVertex();
        }

        vertexBuilder.vertex(matrix, f4 - f5, f5_adjusted + leashWidth2, f6_adjusted + f6).color(r, g, b, 1.0F).uv2(light).endVertex();
        if (swap) {
            vertexBuilder.vertex(matrix, f4 + f5, f5_adjusted + leashWidth1 - leashWidth2, f6_adjusted - f6).color(r, g, b, 1.0F).uv2(light).endVertex();
        }
    }
}
