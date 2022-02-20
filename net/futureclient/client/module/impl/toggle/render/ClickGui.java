/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;

public final class ClickGui
extends ToggleableModule {
    public ClickGui() {
        super("Click Gui", new String[]{"clickgui"}, ModuleType.RENDER);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.minecraft.displayGuiScreen(com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.ClickGui.getClickGui());
        this.setRunning(false);
    }
}

