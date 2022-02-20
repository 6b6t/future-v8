/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui;

import com.gitlab.nuf.api.interfaces.Toggleable;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.item.GuiItem;
import com.gitlab.nuf.exeter.module.impl.toggle.render.tabgui.item.GuiTab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class GuiTabHandler {
    private Minecraft mc = Minecraft.getMinecraft();
    private float width = 0.7f;
    private int guiHeight = 0;
    public boolean mainMenu = true;
    public int selectedItem = 0;
    public int selectedTab = 0;
    private int tabHeight = 12;
    public final ArrayList<GuiTab> tabs = new ArrayList();
    public int transition = 0;
    public boolean visible = true;

    public GuiTabHandler() {
        List modules = Exeter.getInstance().getModuleManager().getRegistry();
        modules.sort((mod1, mod2) -> mod1.getLabel().compareTo(mod2.getLabel()));
        for (ModuleType moduleType : ModuleType.values()) {
            GuiTab guiTab = new GuiTab(this, moduleType.getLabel());
            modules.stream().forEach(module -> {
                ToggleableModule toggle;
                if (module instanceof Toggleable && (toggle = (ToggleableModule)module).getModuleType() == moduleType && !toggle.getLabel().equalsIgnoreCase("Click Gui") && !toggle.getLabel().equalsIgnoreCase("Tab Gui")) {
                    guiTab.getMods().add(new GuiItem(toggle));
                }
            });
            this.tabs.add(guiTab);
        }
        Collections.sort(this.tabs, (category1, category2) -> category1.getLabel().compareTo(category2.getLabel()));
        this.guiHeight = this.tabs.size() * this.tabHeight;
    }

    public void drawGui(int x2, int y2) {
        if (!this.visible) {
            return;
        }
        int guiWidth = 73;
        RenderMethods.drawBorderedRectReliant(x2, (float)y2 - 0.4f, x2 + guiWidth - 2, (float)(y2 + this.guiHeight) + 0.4f, 1.5f, 0x66000000, -2013265920);
        for (int i2 = 0; i2 < this.tabs.size(); ++i2) {
            int transitionBottom;
            int transitionTop;
            int n = !this.mainMenu ? 0 : (transitionTop = this.transition + (this.selectedTab == 0 && this.transition < 0 ? -this.transition : 0));
            int n2 = !this.mainMenu ? 0 : (transitionBottom = this.transition + (this.selectedTab == this.tabs.size() - 1 && this.transition > 0 ? -this.transition : 0));
            if (this.selectedTab != i2) continue;
            RenderMethods.drawGradientBorderedRectReliant(x2, (float)(i2 * 12 + y2 + transitionTop) - 0.3f, (float)(x2 + guiWidth) - 2.2f, (float)(i2 + (y2 + 12 + i2 * 11) + transitionBottom) + 0.3f, 1.5f, -2013265920, -1151539133, -1152530632);
        }
        int yOff = y2 + 2;
        for (int index = 0; index < this.tabs.size(); ++index) {
            GuiTab tab = this.tabs.get(index);
            this.mc.fontRenderer.drawStringWithShadow(tab.getLabel(), x2 + 2, yOff, -5592406);
            if (this.selectedTab == index && !this.mainMenu) {
                tab.drawTabMenu(this.mc, x2 + guiWidth, yOff - 2);
            }
            yOff += this.tabHeight;
        }
        if (this.transition > 0) {
            --this.transition;
        } else if (this.transition < 0) {
            ++this.transition;
        }
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getSelectedItem() {
        return this.selectedItem;
    }

    public int getTabHeight() {
        return this.tabHeight;
    }

    public int getTransition() {
        return this.transition;
    }
}

