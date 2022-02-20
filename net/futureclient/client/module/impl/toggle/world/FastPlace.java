/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.world;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.TickEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;

public final class FastPlace
extends ToggleableModule {
    public FastPlace() {
        super("Fast Place", new String[]{"fastplace", "fp", "place"}, -3370796, ModuleType.WORLD);
        this.listeners.add(new Listener<TickEvent>("fast_place_tick_listener"){

            @Override
            public void call(TickEvent event) {
                ((FastPlace)FastPlace.this).minecraft.rightClickDelayTimer = 0;
            }
        });
    }
}

