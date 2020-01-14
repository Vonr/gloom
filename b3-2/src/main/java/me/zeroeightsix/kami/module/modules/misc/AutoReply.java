// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.module.modules.misc;

import java.util.function.Predicate;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.network.play.server.SPacketChat;
import me.zero.alpine.listener.EventHandler;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.module.Module;

@Module.Info(name = "AutoReply", category = Module.Category.MISC, description = "automatically replies to messages")
public class AutoReply extends Module
{
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public AutoReply() {
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketChat && ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains("whispers:")) {
                Wrapper.getPlayer().sendChatMessage("/r I don't speak to Gloom fanboys.");
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}
