/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.events.WaterMoveEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class AntiVelocity
extends ToggleableModule {
    private final NumberProperty<Integer> percent = new NumberProperty<Integer>(Integer.valueOf(0), -100, 100, "Percent", "p", "%");
    private final Property<Boolean> water = new Property<Boolean>(true, "Water", "w");

    public AntiVelocity() {
        super("Anti Velocity", new String[]{"antivelocity", "novelocity", "av", "nv", "velocity"}, -6381922, ModuleType.MOVEMENT);
        this.offerProperties(this.percent, this.water);
        this.listeners.add(new Listener<PacketEvent>("anti_velocity_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                S12PacketEntityVelocity entityVelocity;
                AntiVelocity.this.setTag((Integer)AntiVelocity.this.percent.getValue() > 0 ? String.format("Velocity \u00a77%s", ((Integer)AntiVelocity.this.percent.getValue()).toString()) : AntiVelocity.this.getLabel());
                if (event.getPacket() instanceof S27PacketExplosion) {
                    S27PacketExplosion explosion = (S27PacketExplosion)event.getPacket();
                    switch ((Integer)AntiVelocity.this.percent.getValue()) {
                        case 100: {
                            break;
                        }
                        case 0: {
                            event.setCanceled(true);
                            break;
                        }
                        default: {
                            explosion.setMotionX(explosion.getMotionX() / 8000.0 / (double)((Integer)AntiVelocity.this.percent.getValue()).intValue());
                            explosion.setMotionY(explosion.getMotionY() / 8000.0 / (double)((Integer)AntiVelocity.this.percent.getValue()).intValue());
                            explosion.setMotionZ(explosion.getMotionZ() / 8000.0 / (double)((Integer)AntiVelocity.this.percent.getValue()).intValue());
                            break;
                        }
                    }
                } else if (event.getPacket() instanceof S12PacketEntityVelocity && (entityVelocity = (S12PacketEntityVelocity)event.getPacket()).getEntityID() == ((AntiVelocity)AntiVelocity.this).minecraft.thePlayer.getEntityId()) {
                    switch ((Integer)AntiVelocity.this.percent.getValue()) {
                        case 100: {
                            break;
                        }
                        case 0: {
                            event.setCanceled(true);
                            break;
                        }
                        default: {
                            entityVelocity.setMotionX(entityVelocity.getMotionX() / 8000 / (Integer)AntiVelocity.this.percent.getValue());
                            entityVelocity.setMotionY(entityVelocity.getMotionY() / 8000 / (Integer)AntiVelocity.this.percent.getValue());
                            entityVelocity.setMotionZ(entityVelocity.getMotionZ() / 8000 / (Integer)AntiVelocity.this.percent.getValue());
                        }
                    }
                }
            }
        });
        this.listeners.add(new Listener<WaterMoveEvent>("anti_velocity_water_move_listener"){

            @Override
            public void call(WaterMoveEvent event) {
                if (((Boolean)AntiVelocity.this.water.getValue()).booleanValue()) {
                    event.setCanceled(true);
                }
            }
        });
    }
}

