/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.events.AirBobbingEvent;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;

public final class Flight
extends ToggleableModule {
    private final Property<Boolean> damage = new Property<Boolean>(false, "Damage", "dmg", "d");
    private final NumberProperty<Double> speed = new NumberProperty<Double>(Double.valueOf(2.0), 1.0, 10.0, "Speed", "s");
    private boolean wasFlying;

    public Flight() {
        super("Flight", new String[]{"flight", "fly"}, -2186401, ModuleType.MOVEMENT);
        this.offerProperties(this.damage, this.speed);
        this.listeners.add(new Listener<MotionUpdateEvent>("flight_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (!((Flight)Flight.this).minecraft.thePlayer.capabilities.isFlying) {
                    ((Flight)Flight.this).minecraft.thePlayer.capabilities.isFlying = true;
                }
                if (((Flight)Flight.this).minecraft.inGameHasFocus) {
                    if (((Flight)Flight.this).minecraft.gameSettings.keyBindJump.getIsKeyPressed()) {
                        ((Flight)Flight.this).minecraft.thePlayer.motionY = 0.4;
                    }
                    if (((Flight)Flight.this).minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) {
                        ((Flight)Flight.this).minecraft.thePlayer.motionY = -0.4;
                    }
                }
            }
        });
        this.listeners.add(new Listener<AirBobbingEvent>("flight_air_bobbing_listener"){

            @Override
            public void call(AirBobbingEvent event) {
                event.setCanceled(true);
            }
        });
        this.listeners.add(new Listener<MovePlayerEvent>("flight_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                if (!PlayerHelper.isPressingMoveKeybinds()) {
                    event.setMotionX(0.0);
                    event.setMotionZ(0.0);
                    return;
                }
                event.setMotionX(event.getMotionX() * (Double)Flight.this.speed.getValue());
                event.setMotionZ(event.getMotionZ() * (Double)Flight.this.speed.getValue());
            }
        });
    }

    @Override
    protected void onEnable() {
        if (this.minecraft.thePlayer != null) {
            this.wasFlying = this.minecraft.thePlayer.capabilities.isFlying;
        }
        super.onEnable();
        if (this.damage.getValue().booleanValue()) {
            PlayerHelper.damagePlayer();
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.thePlayer.capabilities.isFlying = this.wasFlying;
    }
}

