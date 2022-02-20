/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.api.event;

import java.util.List;
import me.friendly.api.event.Event;
import me.friendly.api.event.Listener;

public interface EventManager {
    public void register(Listener var1);

    public void unregister(Listener var1);

    public void clear();

    public void dispatch(Event var1);

    public List<Listener> getListeners();
}

