/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.events;

import me.friendly.api.event.Event;

public class RenderEvent
extends Event {
    private float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

