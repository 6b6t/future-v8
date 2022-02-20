/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.event.filter;

import com.gitlab.nuf.api.event.Event;
import com.gitlab.nuf.api.event.Listener;

public interface Filter {
    public boolean filter(Listener var1, Event var2);
}

