package me.arycer.handcuffs.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.arycer.handcuffs.registry.ModItemRegistry;
import me.arycer.handcuffs.util.HandcuffType;
import me.arycer.handcuffs.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class HandcuffCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("handcuff")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("front")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(new HandcuffPlayerFrontCommand())
                                )
                        )
                        .then(Commands.literal("back")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(new HandcuffPlayerBackCommand())
                                )
                        )
                        .then(Commands.literal("free")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(new HandcuffPlayerFreeCommand())
                                )
                        )
                        .then(Commands.literal("two")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("otherPlayer", EntityArgument.player())
                                                .executes(new HandcuffTwoPlayersCommand())
                                        )
                                )
                        )
        );
    }
    
    private static class HandcuffPlayerFrontCommand implements Command<CommandSource> {
        @Override
        public int run(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandContext, "player");
            Util.handcuff(player, ModItemRegistry.FRONT_HANDCUFFS.get(), HandcuffType.FRONT);
            return 0;
        }
    }

    private static class HandcuffPlayerBackCommand implements Command<CommandSource> {
        @Override
        public int run(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandContext, "player");
            Util.handcuff(player, ModItemRegistry.BACK_HANDCUFFS.get(), HandcuffType.BACK);
            return 0;
        }
    }

    private static class HandcuffPlayerFreeCommand implements Command<CommandSource> {
        @Override
        public int run(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandContext, "player");
            Util.handcuff(player, null, HandcuffType.FREE);
            return 0;
        }
    }

    private static class HandcuffTwoPlayersCommand implements Command<CommandSource> {
        @Override
        public int run(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandContext, "player");
            ServerPlayerEntity otherPlayer = EntityArgument.getPlayer(commandContext, "otherPlayer");
            Util.handcuff(player, otherPlayer.getUUID(), ModItemRegistry.FRONT_HANDCUFFS.get(), HandcuffType.FRONT);
            Util.handcuff(otherPlayer, player.getUUID(), ModItemRegistry.BACK_HANDCUFFS.get(), HandcuffType.BACK);
            return 0;
        }
    }
}
