/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.CloseInventoryEvent;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public final class XCarry
extends ToggleableModule {
    public XCarry() {
        super("X Carry", new String[]{"xcarry", "morecarry"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<PacketEvent>("x_carry_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C0DPacketCloseWindow) {
                    C0DPacketCloseWindow packet = (C0DPacketCloseWindow)event.getPacket();
                    event.setCanceled(packet.getWindowId() == 0);
                }
            }
        });
        this.listeners.add(new Listener<CloseInventoryEvent>("xcarry_close_inventory_listener"){

            @Override
            public void call(CloseInventoryEvent event) {
                event.setCanceled(true);
            }
        });
    }
}

