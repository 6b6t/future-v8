/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import net.minecraft.network.play.server.S02PacketChat;

public final class SpamBypass
extends ToggleableModule {
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(500L), "Delay", "d");
    private final Stopwatch stopwatch = new Stopwatch();

    public SpamBypass() {
        super("Spam Bypass", new String[]{"spambypass", "spam"}, -7289900, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.delay);
        this.listeners.add(new Listener<PacketEvent>("spam_bypass_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                S02PacketChat s02PacketChat;
                if (event.getPacket() instanceof S02PacketChat && (s02PacketChat = (S02PacketChat)event.getPacket()).getChatComponent().getUnformattedTextForChat().contains("'") && SpamBypass.this.stopwatch.hasCompleted((Long)SpamBypass.this.delay.getValue())) {
                    String[] message = s02PacketChat.getChatComponent().getFormattedText().split("'");
                    ((SpamBypass)SpamBypass.this).minecraft.thePlayer.sendChatMessage(message[1]);
                    SpamBypass.this.stopwatch.reset();
                }
            }
        });
    }
}

