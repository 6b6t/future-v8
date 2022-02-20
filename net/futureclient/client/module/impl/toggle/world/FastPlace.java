/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.world;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.TickEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;

public final class FastPlace
extends ToggleableModule {
    private final NumberProperty<Integer> delay = new NumberProperty<Integer>(Integer.valueOf(0), 0, 10, "Delay", "d");

    public FastPlace() {
        super("FastPlace", new String[]{"fastplace", "fp", "place"}, -3370796, ModuleType.WORLD);
        this.offerProperties(this.delay);
        this.listeners.add(new Listener<TickEvent>("fast_place_tick_listener"){

            @Override
            public void call(TickEvent event) {
                ((FastPlace)FastPlace.this).minecraft.rightClickDelayTimer = (Integer)FastPlace.this.delay.getValue();
            }
        });
    }
}

