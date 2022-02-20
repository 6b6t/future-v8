/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.events.WaterMoveEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class AntiVelocity
extends ToggleableModule {
    private final NumberProperty<Integer> percent = new NumberProperty<Integer>(Integer.valueOf(0), 0, 100, "Percent", "p", "%");
    private final Property<Boolean> water = new Property<Boolean>(true, "Water", "w");

    public AntiVelocity() {
        super("AntiVelocity", new String[]{"antivelocity", "novelocity", "av", "nv", "velocity"}, -6381922, ModuleType.MOVEMENT);
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
                            explosion.setMotionX(explosion.getMotionX() * (double)((Integer)AntiVelocity.this.percent.getValue()).intValue() / 100.0);
                            explosion.setMotionY(explosion.getMotionY() * (double)((Integer)AntiVelocity.this.percent.getValue()).intValue() / 100.0);
                            explosion.setMotionZ(explosion.getMotionZ() * (double)((Integer)AntiVelocity.this.percent.getValue()).intValue() / 100.0);
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
                            entityVelocity.setMotionX(entityVelocity.getMotionX() * (Integer)AntiVelocity.this.percent.getValue() / 100);
                            entityVelocity.setMotionY(entityVelocity.getMotionY() * (Integer)AntiVelocity.this.percent.getValue() / 100);
                            entityVelocity.setMotionZ(entityVelocity.getMotionZ() * (Integer)AntiVelocity.this.percent.getValue() / 100);
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

