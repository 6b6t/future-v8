/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.api.event.filter;

import me.friendly.api.event.Event;
import me.friendly.api.event.Listener;

public interface Filter {
    public boolean filter(Listener var1, Event var2);
}

