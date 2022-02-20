/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.Property;
import net.minecraft.network.play.server.S02PacketChat;

public final class AutoAccept
extends ToggleableModule {
    private final Property<Boolean> factions = new Property<Boolean>(false, "Factions", "f");
    private final Stopwatch stopwatch = new Stopwatch();

    public AutoAccept() {
        super("AutoAccept", new String[]{"autoaccept", "aa", "tpaccept"}, ModuleType.MISCELLANEOUS);
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

