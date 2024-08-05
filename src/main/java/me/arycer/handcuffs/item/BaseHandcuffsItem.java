package me.arycer.handcuffs.item;

import me.arycer.handcuffs.client.render.HandcuffsItemRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.Callable;

public class HandcuffsItem extends Item implements IAnimatable, ISyncable {
		private static final int ACTIVATE_ANIM_STATE = 0;
    	public final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    	public HandcuffsItem() {
			super(new Properties()
					.durability(-1)
					.stacksTo(1)
					.setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
				private final HandcuffsItemRenderer renderer = new HandcuffsItemRenderer();
				@Override
				public ItemStackTileEntityRenderer call() throws Exception {
					return renderer;
				}
			}));

			GeckoLibNetwork.registerSyncable(this);
    	}
    
    	@Override
    	public void registerControllers(final AnimationData data) {
			// Do nothing
    	}
    
    	@Override
    	public AnimationFactory getFactory() {
    		return this.factory;
    	}

		@Override
		public void onAnimationSync(int id, int state) {
			// Do nothing
		}
}