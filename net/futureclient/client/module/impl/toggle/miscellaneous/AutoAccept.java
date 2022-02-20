/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.network.play.server.S02PacketChat;

public final class AutoAccept
extends ToggleableModule {
    private final Property<Boolean> factions = new Property<Boolean>(false, "Factions", "f");
    private final Stopwatch stopwatch = new Stopwatch();

    public AutoAccept() {
        super("Auto Accept", new String[]{"autoaccept", "aa", "tpaccept"}, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.factions);
        this.listeners.add(new Listener<PacketEvent>("auto_accept_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof S02PacketChat) {
                    S02PacketChat chat = (S02PacketChat)event.getPacket();
                    String message = chat.getChatComponent().getUnformattedTextForChat();
                    Exeter.getInstance().getFriendManager().getRegistry().forEach(friend -> {
                        if (message.contains(friend.getLabel()) || message.contains(friend.getAlias())) {
                            if (AutoAccept.this.stopwatch.hasCompleted(500L)) {
                                if (message.contains("has requested to teleport")) {
                                    ((AutoAccept)AutoAccept.this).minecraft.thePlayer.sendChatMessage("/tpaccept");
                                }
                                if (message.contains("has invited you to") && ((Boolean)AutoAccept.this.factions.getValue()).booleanValue()) {
                                    ((AutoAccept)AutoAccept.this).minecraft.thePlayer.sendChatMessage("/f join " + friend.getAlias());
                                }
                            }
                            AutoAccept.this.stopwatch.reset();
                        }
                    });
                }
            }
        });
    }
}

