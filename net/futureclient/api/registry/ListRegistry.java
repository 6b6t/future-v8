/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.registry;

import java.util.List;

public class ListRegistry<T> {
    protected List<T> registry;

    public void register(T element) {
        this.registry.add(element);
    }

    public void unregister(T element) {
        this.registry.remove(element);
    }

    public void clear() {
        this.registry.clear();
    }

    public List<T> getRegistry() {
        return this.registry;
    }
}

