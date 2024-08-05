package me.arycer.handcuffs.client;


import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import me.arycer.handcuffs.util.Reference;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static me.arycer.handcuffs.SharedModInitializer.MOD_ID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModInitializer {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.ANIMATION, 42, ClientModInitializer::registerPlayerAnimation);
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayerEntity player) {
        return new ModifierLayer<>();
    }
}
