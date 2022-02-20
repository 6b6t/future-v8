/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.event;

public class Event {
    private boolean canceled = false;

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

