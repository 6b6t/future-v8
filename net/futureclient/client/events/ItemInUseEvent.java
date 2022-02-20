/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.events;

import me.friendly.api.event.Event;

public class ItemInUseEvent
extends Event {
    private float speed;

    public ItemInUseEvent(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

