/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.events;

import com.gitlab.nuf.api.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class RenderGameInfoEvent
extends Event {
    private ScaledResolution scaledResolution;

    public RenderGameInfoEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
}

