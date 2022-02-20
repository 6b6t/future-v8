/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import me.friendly.api.event.Listener;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.toggle.combat.KillAura;
import me.friendly.exeter.module.impl.toggle.movement.Speed;
import me.friendly.exeter.properties.EnumProperty;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Criticals
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.PACKET, "Mode", "m");

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
                    if (speed != null && speed.isRunning() && speedMode.getValue() == Speed.Mode.HOP || speed != null && speed.isRunning() && speedMode.getValue() == Speed.Mode.YPORT) {
                        return;
                    }
                    if (((Criticals)Criticals.this).minecraft.thePlayer.onGround && !KillAura.shouldCrit) {
                        switch ((Mode)((Object)Criticals.this.mode.getValue())) {
                            case PACKET: {
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY + (double)0.05f, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY + (double)0.012511f, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                                Criticals.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Criticals)Criticals.this).minecraft.thePlayer.posX, ((Criticals)Criticals.this).minecraft.thePlayer.posY, ((Criticals)Criticals.this).minecraft.thePlayer.posZ, false));
                            }
                        }
                    }
                }
            }
        });
    }

    private static enum Mode {
        PACKET;

    }
}

