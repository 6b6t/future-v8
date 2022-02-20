/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.presets;

import com.gitlab.nuf.api.interfaces.Labeled;

public abstract class Preset
implements Labeled {
    private final String label;

    protected Preset(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public abstract void onSet();
}

