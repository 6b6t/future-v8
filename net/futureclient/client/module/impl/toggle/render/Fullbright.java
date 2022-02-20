/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.GammaSettingEvent;
import com.gitlab.nuf.exeter.events.NightVisionEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;

public final class Fullbright
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.POTION, "Mode", "m");

    public Fullbright() {
        super("Fullbright", new String[]{"fullbright", "bright", "brightness", "fb"}, -2366720, ModuleType.RENDER);
        this.offerProperties(this.mode);
        this.listeners.add(new Listener<GammaSettingEvent>("brightness_gamma_setting_listener"){

            @Override
            public void call(GammaSettingEvent event) {
                if (Fullbright.this.mode.getValue() == Mode.GAMMA) {
                    event.setGammaSetting(1000.0f);
                }
            }
        });
        this.listeners.add(new Listener<NightVisionEvent>("brightness_night_vision_listener"){

            @Override
            public void call(NightVisionEvent event) {
                if (Fullbright.this.mode.getValue() == Mode.POTION) {
                    event.setCanceled(true);
                }
            }
        });
    }

    public static enum Mode {
        GAMMA,
        POTION;

    }
}

