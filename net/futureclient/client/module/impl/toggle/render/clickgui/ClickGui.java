/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui;

import com.gitlab.nuf.api.interfaces.Toggleable;
import com.gitlab.nuf.api.minecraft.render.CustomFont;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.Panel;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item.ModuleButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public final class ClickGui
extends GuiScreen {
    private static ClickGui clickGui;
    public final CustomFont guiFont = new CustomFont("Segoe UI", 18.0f);
    private final ArrayList<Panel> panels = new ArrayList();

    public ClickGui() {
        if (this.getPanels().isEmpty()) {
            this.load();
        }
    }

    public static ClickGui getClickGui() {
        return clickGui == null ? (clickGui = new ClickGui()) : clickGui;
    }

    private void load() {
        int x2 = -84;
        for (final ModuleType moduleType : ModuleType.values()) {
            this.panels.add(new Panel(moduleType.getLabel(), x2 += 90, 4, true){

                @Override
                public void setupItems() {
                    Exeter.getInstance().getModuleManager().getRegistry().forEach(module -> {
                        ToggleableModule toggleableModule;
                        if (module instanceof Toggleable && !module.getLabel().equalsIgnoreCase("Tab Gui") && !module.getLabel().equalsIgnoreCase("Click Gui") && (toggleableModule = (ToggleableModule)module).getModuleType().equals((Object)moduleType)) {
                            this.addButton(new ModuleButton(toggleableModule));
                        }
                    });
                }
            });
        }
        this.panels.add(new Panel("Always Active", x2 += 90, 4, true){

            @Override
            public void setupItems() {
                Exeter.getInstance().getModuleManager().getRegistry().forEach(module -> {
                    if (!(module instanceof Toggleable || module.getLabel().equalsIgnoreCase("Tab Gui") || module.getLabel().equalsIgnoreCase("Click Gui"))) {
                        this.addButton(new ModuleButton((Module)module));
                    }
                });
            }
        });
        this.panels.forEach(panel -> panel.getItems().sort((item1, item2) -> item1.getLabel().compareTo(item2.getLabel())));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Panel> getPanels() {
        return this.panels;
    }
}

