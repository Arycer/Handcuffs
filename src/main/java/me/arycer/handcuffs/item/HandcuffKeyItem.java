package me.arycer.handcuffs.item;

import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class HandcuffKeyItem extends Item {
    public HandcuffKeyItem() {
        super(new Properties().durability(-1).stacksTo(1));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof ServerPlayerEntity)) {
            return ActionResultType.PASS;
        }

        ServerPlayerEntity target = (ServerPlayerEntity) entity;
        Util.handcuff(target, null, HandcuffType.FREE);

        return ActionResultType.SUCCESS;
    }
}
