package me.arycer.handcuffs;

import me.arycer.handcuffs.net.MessageHandler;
import me.arycer.handcuffs.registry.ModItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SharedModInitializer.MOD_ID)
public class SharedModInitializer {
    public static final String MOD_ID = "handcuffs";
    public static final Logger LOGGER = LogManager.getLogger();

    public SharedModInitializer() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MessageHandler.register();
        ModItemRegistry.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
