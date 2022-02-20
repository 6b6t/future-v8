/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.event;

import com.gitlab.nuf.api.event.Event;
import com.gitlab.nuf.api.event.filter.Filter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Listener<E extends Event> {
    private final String identifier;
    private Class<E> event;
    private final List<Filter> filters = new CopyOnWriteArrayList<Filter>();

    public Listener(String identifier) {
        this.identifier = identifier;
        Type generic = this.getClass().getGenericSuperclass();
        if (generic instanceof ParameterizedType) {
            for (Type type : ((ParameterizedType)generic).getActualTypeArguments()) {
                if (!(type instanceof Class) || !Event.class.isAssignableFrom((Class)type)) continue;
                this.event = (Class)type;
                break;
            }
        }
    }

    public Class<E> getEvent() {
        return this.event;
    }

    public final String getIdentifier() {
        return this.identifier;
    }

    public final List<Filter> getFilters() {
        return this.filters;
    }

    public void addFilters(Filter ... filters) {
        for (Filter filter : filters) {
            this.filters.add(filter);
        }
    }

    public abstract void call(E var1);
}

