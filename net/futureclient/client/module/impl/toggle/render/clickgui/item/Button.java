/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item;

import com.gitlab.nuf.api.interfaces.Labeled;
import com.gitlab.nuf.api.minecraft.render.CustomFont;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.ClickGui;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.Panel;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class Button
extends Item
implements Labeled {
    private boolean state;

    public Button(String label) {
        super(label);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderMethods.drawGradientRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 2000402499 : 1429977155) : (!this.isHovering(mouseX, mouseY) ? 0x33555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? -1438926781 : -1721964477) : (!this.isHovering(mouseX, mouseY) ? 0x55555555 : -1722460843));
        ClickGui.getClickGui().guiFont.drawString(this.getLabel(), this.x + 2.3f, this.y - 2.0f, CustomFont.FontType.SHADOW_THIN, this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.state = !this.state;
            this.toggle();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    protected boolean isHovering(int mouseX, int mouseY) {
        for (Panel panel : ClickGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }
}

