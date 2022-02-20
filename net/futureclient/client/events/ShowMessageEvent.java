/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.events;

import me.friendly.api.event.Event;

public class ShowMessageEvent
extends Event {
    private String message;

    public ShowMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

