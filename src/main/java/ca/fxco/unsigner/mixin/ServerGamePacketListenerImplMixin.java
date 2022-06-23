package ca.fxco.unsigner.mixin;

import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public ServerPlayer player;

    @Shadow
    protected abstract void detectRateSpam();


    /**
     * @author FX - PR0CESS
     * @reason stop reporting
     */
    @Overwrite
    private void handleChat(ServerboundChatPacket serverboundChatPacket, FilteredText<String> filteredText) {
        ChatDecorator chatDecorator = this.server.getChatDecorator();
        chatDecorator.decorateChat(
                this.player,
                filteredText.map(Component::literal),
                MessageSignature.unsigned(),
                false
        ).thenAcceptAsync(this::broadcastUnsignedChatMessage, this.server);
    }

    private void broadcastUnsignedChatMessage(FilteredText<PlayerChatMessage> filtered) {
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(
                    Component.empty()
                            .append("<")
                            .append(player.getDisplayName())
                            .append("> ")
                            .append(filtered.raw().serverContent()),
                    ChatType.SYSTEM
            );
        }
        this.detectRateSpam();
    }
}
