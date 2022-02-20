/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;

public final class FastLadder
extends ToggleableModule {
    private final NumberProperty<Double> speed = new NumberProperty<Double>(Double.valueOf(1.9), 1.1, 10.0, "Speed", "s");
    private final Property<Boolean> stop = new Property<Boolean>(true, "Stop", "s");

    public FastLadder() {
        super("Fast Ladder", new String[]{"fastladder", "ladder", "ladders", "fl"}, -3037363, ModuleType.MOVEMENT);
        this.offerProperties(this.stop);
        this.listeners.add(new Listener<MovePlayerEvent>("fast_ladder_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                if (!((FastLadder)FastLadder.this).minecraft.thePlayer.onGround && ((FastLadder)FastLadder.this).minecraft.thePlayer.isOnLadder()) {
                    event.setMotionY(event.getMotionY() * (Double)FastLadder.this.speed.getValue());
                    if (((Boolean)FastLadder.this.stop.getValue()).booleanValue() && (((FastLadder)FastLadder.this).minecraft.currentScreen != null || ((FastLadder)FastLadder.this).minecraft.thePlayer.isSneaking())) {
                        event.setMotionX(0.0);
                        event.setMotionY(0.0);
                        event.setMotionZ(0.0);
                    }
                }
            }
        });
    }
}

