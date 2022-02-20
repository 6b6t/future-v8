/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C01PacketChatMessage;

public final class AntiCommand
extends ToggleableModule {
    public AntiCommand() {
        super("Anti Command", new String[]{"anticommand", "ac"}, -3764012, ModuleType.MISCELLANEOUS);
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

