package me.arycer.handcuffs.net;

import me.arycer.handcuffs.SharedModInitializer;
import me.arycer.handcuffs.net.s2c.HandcuffPlayerS2C;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
    private static SimpleChannel CHANNEL;
    private static int packetId = 0;
    private static int createId() {
        return packetId++;
    }

    public static void register() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SharedModInitializer.MOD_ID, "packet_messages"))
                .networkProtocolVersion(() -> "1.0-SNAPSHOT")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL.messageBuilder(HandcuffPlayerS2C.class, createId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HandcuffPlayerS2C::new)
                .encoder(HandcuffPlayerS2C::toBytes)
                .consumer(HandcuffPlayerS2C::handle)
                .add();
    }

    public static <Message> void sendToAllPlayers(Message message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
}
