/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import net.minecraft.block.BlockAir;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class NoFall
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.DAMAGE, "Mode", "m");

    public NoFall() {
        super("NoFall", new String[]{"nofall", "0fall", "nf"}, -12727218, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.mode);
        this.listeners.add(new Listener<PacketEvent>("no_fall_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (NoFall.this.mode.getValue() == Mode.DAMAGE && event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
                    if (((NoFall)NoFall.this).minecraft.thePlayer.fallDistance > 3.0f) {
                        packet.setOnGround(true);
                    }
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("no_fall_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)NoFall.this.mode.getValue())) {
                    case INFINITE: {
                        NoFall.this.setTag("InfiniteJump");
                        ((NoFall)NoFall.this).minecraft.thePlayer.onGround = true;
                        break;
                    }
                    case DAMAGE: {
                        NoFall.this.setTag("NoFall");
                        break;
                    }
                    case RECONNECT: {
                        NoFall.this.setTag("ReconnectFall");
                        if (PlayerHelper.getBlockBelowPlayer(4.0) instanceof BlockAir) break;
                        ServerData serverData = NoFall.this.minecraft.getCurrentServerData();
                        ((NoFall)NoFall.this).minecraft.theWorld.sendQuittingDisconnectingPacket();
                        NoFall.this.minecraft.displayGuiScreen(new GuiConnecting(null, NoFall.this.minecraft, serverData));
                        NoFall.this.setRunning(false);
                        break;
                    }
                    case DISCONNECT: {
                        NoFall.this.setTag("DisconnectFall");
                        if (PlayerHelper.getBlockBelowPlayer(4.0) instanceof BlockAir) break;
                        ((NoFall)NoFall.this).minecraft.theWorld.sendQuittingDisconnectingPacket();
                        NoFall.this.setRunning(false);
                    }
                }
            }
        });
    }

    public static enum Mode {
        DAMAGE,
        INFINITE,
        RECONNECT,
        DISCONNECT;

    }
}

