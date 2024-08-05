package me.arycer.handcuffs.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.github.kosmx.emotes.api.events.server.ServerEmoteAPI;
import me.arycer.handcuffs.Handcuffs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class HandcuffCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("handcuff")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(new HandcuffPlayerCommand())
                        )
        );
    }
    
    private static class HandcuffPlayerCommand implements Command<CommandSource> {
        @Override
        public int run(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandContext, "player");

/*            PlayerAnimationRegistry.getAnimations().forEach((key, value) -> {
                System.out.println(player.getUUID());
                System.out.println(key.toString());
                System.out.println(value);

                try {
                    ServerEmoteAPI.playEmote(player.getUUID(), value, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });*/

            return 0;
        }
    }
}
