/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.events;

import com.gitlab.nuf.api.event.Event;

public class NightVisionEvent
extends Event {
    private final Type type;

    public NightVisionEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        TIME,
        VISUAL;

    }
}

