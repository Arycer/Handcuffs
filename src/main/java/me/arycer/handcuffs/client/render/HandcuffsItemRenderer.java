package me.arycer.handcuffs.client.render;

import me.arycer.handcuffs.client.model.HandcuffsItemModel;
import me.arycer.handcuffs.item.BaseHandcuffsItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandcuffsItemRenderer extends GeoItemRenderer<BaseHandcuffsItem> {
	public HandcuffsItemRenderer() {
		super(new HandcuffsItemModel());
	}
}