/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public final class Sneak
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.SILENT, "Mode", "m");

    public Sneak() {
        super("Sneak", new String[]{"sneak", "shift"}, -7874206, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.mode);
        this.listeners.add(new Listener<MotionUpdateEvent>("sneak_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)Sneak.this.mode.getValue())) {
                    case SILENT: {
                        switch (event.getTime()) {
                            case BEFORE: {
                                if (PlayerHelper.isMoving()) {
                                    Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                                    Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                                    break;
                                }
                                Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                                break;
                            }
                            case AFTER: {
                                if (!PlayerHelper.isMoving()) break;
                                Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                            }
                        }
                        break;
                    }
                    case VANILLA: {
                        Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        break;
                    }
                    case KEY: {
                        ((Sneak)Sneak.this).minecraft.gameSettings.keyBindSneak.pressed = true;
                    }
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("sneak_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                    Sneak.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Sneak)Sneak.this).minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        switch ((Mode)((Object)this.mode.getValue())) {
            case SILENT: 
            case VANILLA: {
                if (this.minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) break;
                this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                break;
            }
            case KEY: {
                if (!this.minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) break;
                this.minecraft.gameSettings.keyBindSneak.pressed = false;
            }
        }
    }

    public static enum Mode {
        SILENT,
        KEY,
        VANILLA;

    }
}

