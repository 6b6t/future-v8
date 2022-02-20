/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.active.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public final class AntiAim
extends Module {
    public AntiAim() {
        super("Anti Aim", new String[]{"antiaim", "aa"});
        Exeter.getInstance().getEventManager().register(new Listener<PacketEvent>("anti_aim_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
                    if (((AntiAim)AntiAim.this).minecraft.thePlayer.rotationYaw != -180.0f && ((AntiAim)AntiAim.this).minecraft.thePlayer.rotationPitch != 0.0f) {
                        packet.setYaw(((AntiAim)AntiAim.this).minecraft.thePlayer.rotationYaw);
                        packet.setPitch(((AntiAim)AntiAim.this).minecraft.thePlayer.rotationPitch);
                    }
                }
            }
        });
    }
}

