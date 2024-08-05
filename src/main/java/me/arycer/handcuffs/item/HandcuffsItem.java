package me.arycer.handcuffs.item;

import me.arycer.handcuffs.registry.ModItemRegistry;
import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.UUID;

public class HandcuffsItem extends BaseHandcuffsItem {
    // handcuffer, last interaction
    private final HashMap<UUID, UUID> handcuffInteractions = new HashMap<>();

    public HandcuffsItem() {
        super();
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof ServerPlayerEntity)) {
            return ActionResultType.PASS;
        }

        ServerPlayerEntity target = (ServerPlayerEntity) entity;
        Vector3d playerLook = player.getLookAngle();
        Vector3d targetLook = target.getLookAngle();

        Vector3f playerLookVec = new Vector3f((float) playerLook.x, (float) playerLook.y, (float) playerLook.z);
        Vector3f targetLookVec = new Vector3f((float) targetLook.x, (float) targetLook.y, (float) targetLook.z);

        float dotProduct = playerLookVec.dot(targetLookVec);

        if (player.isCrouching() && this.handcuffInteractions.containsKey(player.getUUID())) {
            UUID lastInteraction = this.handcuffInteractions.get(player.getUUID());
            if (!lastInteraction.equals(target.getUUID())) {
                if (dotProduct < 0) { // front
                    Util.handcuff(target, lastInteraction, ModItemRegistry.FRONT_HANDCUFFS.get(), HandcuffType.FRONT);
                } else { // back
                    Util.handcuff(target, lastInteraction, ModItemRegistry.BACK_HANDCUFFS.get(), HandcuffType.BACK);
                }
                this.handcuffInteractions.remove(player.getUUID());
            }
        } else if (player.isCrouching()) {
            this.handcuffInteractions.put(player.getUUID(), target.getUUID());
        }

        if (dotProduct < 0) { // front
            Util.handcuff(target, ModItemRegistry.FRONT_HANDCUFFS.get(), HandcuffType.FRONT);
        } else { // back
            Util.handcuff(target, ModItemRegistry.BACK_HANDCUFFS.get(), HandcuffType.BACK);
        }

        return ActionResultType.SUCCESS;
    }
}
