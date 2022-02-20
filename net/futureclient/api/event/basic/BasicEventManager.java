/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.api.event.basic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.friendly.api.event.Event;
import me.friendly.api.event.EventManager;
import me.friendly.api.event.Listener;
import me.friendly.api.event.filter.Filter;
import net.minecraft.client.Minecraft;

public final class BasicEventManager
implements EventManager {
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    @Override
    public void register(Listener listener) {
        if (!this.has(listener) && listener != null) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void unregister(Listener listener) {
        if (this.has(listener) && listener != null) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public void clear() {
        if (!this.listeners.isEmpty()) {
            this.listeners.clear();
        }
    }

    @Override
    public void dispatch(Event event) {
        this.listeners.forEach(listener -> {
            if (this.filter((Listener)listener, event) && listener.getEvent() == event.getClass() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                listener.call(event);
            }
        });
    }

    @Override
    public List<Listener> getListeners() {
        return this.listeners;
    }

    public Listener getListener(String identifier) {
        if (!this.listeners.isEmpty()) {
            for (Listener listener : this.listeners) {
                if (!listener.getIdentifier().equalsIgnoreCase(identifier)) continue;
                return listener;
            }
        }
        return null;
    }

    private boolean filter(Listener listener, Event event) {
        List<Filter> filters = listener.getFilters();
        if (!filters.isEmpty()) {
            for (Filter filter : filters) {
                if (filter.filter(listener, event)) continue;
                return false;
            }
        }
        return true;
    }

    private boolean has(Listener listener) {
        return this.listeners.contains(listener);
    }
}

