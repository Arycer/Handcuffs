package me.arycer.handcuffs.item;

import software.bernie.geckolib3.network.GeckoLibNetwork;

public class BackHandcuffsItem extends LockedHandcuffsItem {
    public BackHandcuffsItem() {
        super();
        GeckoLibNetwork.registerSyncable(this);
    }
}
