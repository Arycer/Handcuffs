package me.arycer.handcuffs.listener;

import me.arycer.handcuffs.Handcuffs;
import me.arycer.handcuffs.command.HandcuffCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Handcuffs.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        HandcuffCommand.register(event.getDispatcher());
    }
}