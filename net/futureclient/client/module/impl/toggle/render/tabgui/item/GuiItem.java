/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.item;

import com.gitlab.nuf.exeter.module.ToggleableModule;

public class GuiItem {
    private final ToggleableModule toggleableModule;

    public GuiItem(ToggleableModule toggleableModule) {
        this.toggleableModule = toggleableModule;
    }

    public ToggleableModule getToggleableModule() {
        return this.toggleableModule;
    }
}

