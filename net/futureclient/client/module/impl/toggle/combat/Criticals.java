/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.movement.Speed;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Criticals
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.SILENT, "Mode", "m");
    private boolean nextPacket;

    public Criticals() {
        super("Criticals", new String[]{"criticals", "crit", "crits", "critical"}, -2380220, ModuleType.COMBAT);
        this.offerProperties(this.mode);
        this.listeners.add(new Listener<PacketEvent>("criticals_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                C02PacketUseEntity c02PacketUseEntity;
                if (event.getPacket() instanceof C02PacketUseEntity && (c02PacketUseEntity = (C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
                    Speed speed = (Speed)Exeter.getInstance().getModuleManager().getModuleByAlias("speed");
                    EnumProperty speedMode = (EnumProperty)speed.getPropertyByAlias("Mode");
                    if (speed != null && speed.isRunning() && speedMode.getValue() == Speed.Mode.HOP) {
                        return;
                    }
                    Criticals.this.nextPacket = !Criticals.this.nextPacket;
                    if (Criticals.this.nextPacket && ((Criticals)Criticals.this).minecraft.thePlayer.onGround) {
                        switch ((Mode)((Object)Criticals.this.mode.getValue())) {
                            case SILENT: {
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY + (double)0.05f, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY + (double)0.012511f, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                break;
                            }
                            case JUMP: {
                                ((Criticals)Criticals.this).minecraft.thePlayer.motionY = 0.4;
                                break;
                            }
                            case SMALL: {
                                ((Criticals)Criticals.this).minecraft.thePlayer.motionY = 0.11;
                            }
                        }
                    }
                }
            }
        });
    }

    private static enum Mode {
        JUMP,
        SILENT,
        SMALL;

    }
}

