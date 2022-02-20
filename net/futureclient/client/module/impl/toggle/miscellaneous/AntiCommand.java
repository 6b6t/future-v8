/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C01PacketChatMessage;

public final class AntiCommand
extends ToggleableModule {
    public AntiCommand() {
        super("AntiCommand", new String[]{"anticommand", "ac"}, -3764012, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<PacketEvent>("anti_command_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                C01PacketChatMessage c01PacketChatMessage;
                if (event.getPacket() instanceof C01PacketChatMessage && (c01PacketChatMessage = (C01PacketChatMessage)event.getPacket()).getMessage().startsWith("/")) {
                    String message = String.format("\u08c7%s", c01PacketChatMessage.getMessage());
                    c01PacketChatMessage.setMessage(message);
                }
            }
        });
    }
}

