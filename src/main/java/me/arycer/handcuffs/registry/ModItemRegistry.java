package me.arycer.handcuffs.registry;

import me.arycer.handcuffs.SharedModInitializer;
import me.arycer.handcuffs.item.BackHandcuffsItem;
import me.arycer.handcuffs.item.FrontHandcuffsItem;
import me.arycer.handcuffs.item.HandcuffKeyItem;
import me.arycer.handcuffs.item.HandcuffsItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SharedModInitializer.MOD_ID);
    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", HandcuffsItem::new);
    public static final RegistryObject<Item> BACK_HANDCUFFS = ITEMS.register("handcuffs_back", FrontHandcuffsItem::new);
    public static final RegistryObject<Item> FRONT_HANDCUFFS = ITEMS.register("handcuffs_front", BackHandcuffsItem::new);
    public static final RegistryObject<Item> KEY = ITEMS.register("handcuff_key", HandcuffKeyItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
