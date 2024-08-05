package me.arycer.handcuffs.util;

public interface IHandcuffAccessor {
    HandcuffType handcuffs$getHandcuffType();
    void handcuffs$setHandcuffType(HandcuffType type);

    String handcuffs$getHandcuffedUUID();
    void handcuffs$setHandcuffedUUID(String uuid);
}
