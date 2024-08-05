package me.arycer.handcuffs.item;

import software.bernie.geckolib3.network.GeckoLibNetwork;

public class FrontHandcuffsItem extends LockedHandcuffsItem {
    public FrontHandcuffsItem() {
        super();
        GeckoLibNetwork.registerSyncable(this);
    }
}
