package me.arycer.handcuffs.client.model;

import me.arycer.handcuffs.item.BaseHandcuffsItem;
import me.arycer.handcuffs.util.Util;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandcuffsItemModel extends AnimatedGeoModel<BaseHandcuffsItem> {
    @Override
    public ResourceLocation getModelLocation(BaseHandcuffsItem handcuffsItem) {
        return Util.getResourceLocation("geo/handcuffs.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BaseHandcuffsItem handcuffsItem) {
        return Util.getResourceLocation("textures/item/handcuffs.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BaseHandcuffsItem handcuffsItem) {
        return Util.getResourceLocation("animations/handcuffs.animation.json");
    }
}
