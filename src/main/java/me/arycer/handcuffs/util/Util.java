package me.arycer.handcuffs.util;

import me.arycer.handcuffs.SharedModInitializer;
import me.arycer.handcuffs.net.MessageHandler;
import me.arycer.handcuffs.net.s2c.HandcuffPlayerS2C;
import me.arycer.handcuffs.registry.ModItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class Util {
    public static final Predicate<ItemStack> IS_HANDCUFFS = itemStack -> itemStack.getItem() == ModItemRegistry.FRONT_HANDCUFFS.get() || itemStack.getItem() == ModItemRegistry.BACK_HANDCUFFS.get();
    public static ResourceLocation getResourceLocation(String path) {
        return new ResourceLocation(SharedModInitializer.MOD_ID, path);
    }

    public static boolean isHandcuffed(PlayerEntity player) {
        return isHandcuffsItem(player.getItemInHand(Hand.OFF_HAND));
    }

    public static void handcuff(ServerPlayerEntity player, @Nullable UUID otherPlayerUUID, @Nullable Item item, HandcuffType handcuffType) {
        clearHandcuffs(player);
        player.setItemInHand(Hand.OFF_HAND, item != null ? new ItemStack(item) : ItemStack.EMPTY);

        if (otherPlayerUUID != null) {
            MessageHandler.sendToAllPlayers(new HandcuffPlayerS2C(handcuffType, player.getUUID(), otherPlayerUUID));
        } else {
            MessageHandler.sendToAllPlayers(new HandcuffPlayerS2C(handcuffType, player));
        }

        if (!(player instanceof IHandcuffAccessor)) {
            return;
        }

        IHandcuffAccessor accessor = (IHandcuffAccessor) player;
        if (handcuffType == HandcuffType.FREE) {
            removeHandcuffs(accessor, player);
        } else {
            accessor.handcuffs$setHandcuffType(handcuffType);
            if (otherPlayerUUID != null) {
                setHandcuffedPlayer(player, accessor, otherPlayerUUID, handcuffType);
            }
        }
    }

    private static void removeHandcuffs(IHandcuffAccessor accessor, ServerPlayerEntity player) {
        String handcuffedString = accessor.handcuffs$getHandcuffedUUID();
        accessor.handcuffs$setHandcuffedUUID("");
        UUID handcuffedUUID;

        try {
            handcuffedUUID = UUID.fromString(handcuffedString);
        } catch (IllegalArgumentException e) {
            return;
        }

        PlayerEntity handcuffedPlayer = player.level.getPlayerByUUID(handcuffedUUID);
        if (handcuffedPlayer instanceof IHandcuffAccessor) {
            IHandcuffAccessor handcuffedAccessor = (IHandcuffAccessor) handcuffedPlayer;
            handcuffedAccessor.handcuffs$setHandcuffedUUID("");
        }
    }

    private static void setHandcuffedPlayer(ServerPlayerEntity player, IHandcuffAccessor accessor, UUID otherPlayerUUID, HandcuffType handcuffType) {
        accessor.handcuffs$setHandcuffedUUID(otherPlayerUUID.toString());

        PlayerEntity otherPlayer = player.level.getPlayerByUUID(otherPlayerUUID);
        if (!(otherPlayer instanceof IHandcuffAccessor)) {
            return;
        }

        IHandcuffAccessor otherAccessor = (IHandcuffAccessor) otherPlayer;
        otherAccessor.handcuffs$setHandcuffType(handcuffType);

        if (handcuffType == HandcuffType.FREE) {
            otherAccessor.handcuffs$setHandcuffedUUID("");
        } else {
            otherAccessor.handcuffs$setHandcuffedUUID(player.getUUID().toString());
        }
    }

    public static void handcuff(ServerPlayerEntity player, Item item, HandcuffType front) {
        handcuff(player, null, item, front);
    }

    public static void clearHandcuffs(ServerPlayerEntity player) {
        player.inventory.clearOrCountMatchingItems(IS_HANDCUFFS, player.inventory.items.size(), player.inventory);
        player.inventory.setChanged();
    }

    public static boolean isHandcuffsItem(ItemStack itemStack) {
        return itemStack.getItem() == ModItemRegistry.BACK_HANDCUFFS.get() || itemStack.getItem() == ModItemRegistry.FRONT_HANDCUFFS.get();
    }
}
