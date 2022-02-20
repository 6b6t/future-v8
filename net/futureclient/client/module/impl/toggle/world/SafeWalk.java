/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.world;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.MovePlayerEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;

public final class SafeWalk
extends ToggleableModule {
    public SafeWalk() {
        super("SafeWalk", new String[]{"safewalk", "sw", "walk"}, -3060354, ModuleType.WORLD);
        this.listeners.add(new Listener<MovePlayerEvent>("safe_walk_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                event.setSafe(true);
            }
        });
    }
}

