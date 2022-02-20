/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.world;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;

public final class SafeWalk
extends ToggleableModule {
    public SafeWalk() {
        super("Safe Walk", new String[]{"safewalk", "sw", "walk"}, -3060354, ModuleType.WORLD);
        this.listeners.add(new Listener<MovePlayerEvent>("safe_walk_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                event.setSafe(true);
            }
        });
    }
}

