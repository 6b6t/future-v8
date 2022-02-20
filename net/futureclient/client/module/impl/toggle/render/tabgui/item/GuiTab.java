/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.item;

import com.gitlab.nuf.api.interfaces.Labeled;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.GuiTabHandler;
import com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.item.GuiItem;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class GuiTab
implements Labeled {
    private final GuiTabHandler gui;
    private ArrayList<GuiItem> mods = new ArrayList();
    private int menuHeight = 0;
    private int menuWidth = 0;
    private final String label;

    public GuiTab(GuiTabHandler gui, String label) {
        this.gui = gui;
        this.label = label;
    }

    public void drawTabMenu(Minecraft mc2, int x2, int y2) {
        this.countMenuSize(mc2);
        int boxY = y2;
        RenderMethods.drawBorderedRectReliant(x2, (float)y2 - 0.4f, (float)(x2 + this.menuWidth) + 4.5f, (float)(y2 + this.menuHeight) + 0.4f, 1.5f, 0x66000000, -2013265920);
        for (int i2 = 0; i2 < this.mods.size(); ++i2) {
            int transitionTop = this.gui.getTransition() + (this.gui.getSelectedItem() == 0 && this.gui.getTransition() < 0 ? -this.gui.getTransition() : 0);
            int transitionBottom = this.gui.getTransition() + (this.gui.getSelectedItem() == this.mods.size() - 1 && this.gui.getTransition() > 0 ? -this.gui.getTransition() : 0);
            if (this.gui.getSelectedItem() == i2) {
                RenderMethods.drawGradientBorderedRectReliant(x2, (float)(boxY + transitionTop) - 0.3f, (float)(x2 + this.menuWidth) + 4.5f, (float)(boxY + 12 + transitionBottom) + 0.3f, 1.5f, -2013265920, -1151539133, -1152530632);
            }
            mc2.fontRenderer.drawStringWithShadow(this.mods.get(i2).getToggleableModule().getLabel(), x2 + 2, y2 + this.gui.getTabHeight() * i2 + 2, this.mods.get(i2).getToggleableModule().isRunning() ? -10688445 : -5592406);
            boxY += 12;
        }
    }

    private void countMenuSize(Minecraft mc2) {
        int maxWidth = 0;
        for (GuiItem module : this.mods) {
            if (mc2.fontRenderer.getStringWidth(module.getToggleableModule().getAliases()[0]) <= maxWidth) continue;
            maxWidth = mc2.fontRenderer.getStringWidth(module.getToggleableModule().getAliases()[0]) + 4;
        }
        this.menuWidth = maxWidth;
        this.menuHeight = this.mods.size() * this.gui.getTabHeight();
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public ArrayList<GuiItem> getMods() {
        return this.mods;
    }
}

