package me.arycer.handcuffs.listener;

import me.arycer.handcuffs.SharedModInitializer;
import me.arycer.handcuffs.command.HandcuffCommand;
import me.arycer.handcuffs.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SharedModInitializer.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        HandcuffCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent event) {
        if (Util.isHandcuffed(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
            if (Util.isHandcuffed(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClickInput(InputEvent.ClickInputEvent event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        if (Util.isHandcuffed(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
