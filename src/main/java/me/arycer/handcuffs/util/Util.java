package me.arycer.handcuffs;

import net.minecraft.util.ResourceLocation;

public class Util {
    public static ResourceLocation getResourceLocation(String path) {
        return new ResourceLocation(Handcuffs.MOD_ID, path);
    }
}
