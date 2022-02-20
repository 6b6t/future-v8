/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;
import net.minecraft.network.play.server.S02PacketChat;

public final class SpamBypass
extends ToggleableModule {
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(500L), "Delay", "d");
    private final Stopwatch stopwatch = new Stopwatch();

    public SpamBypass() {
        super("SpamBypass", new String[]{"spambypass", "spam"}, -7289900, ModuleType.MISCELLANEOUS);
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

