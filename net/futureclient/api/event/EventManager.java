/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.event;

import com.gitlab.nuf.api.event.Event;
import com.gitlab.nuf.api.event.Listener;
import java.util.List;

public interface EventManager {
    public void register(Listener var1);

    public void unregister(Listener var1);

    public void clear();

    public void dispatch(Event var1);

    public List<Listener> getListeners();
}

