/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item.properties;

import com.gitlab.nuf.api.minecraft.render.CustomFont;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.ClickGui;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item.Button;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class BooleanButton
extends Button {
    private Property property;

    public BooleanButton(Property property) {
        super(property.getAliases()[0]);
        this.property = property;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderMethods.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 2002577475 : -1721964477) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        ClickGui.getClickGui().guiFont.drawString(this.getLabel(), this.x + 2.3f, this.y - 1.7f, CustomFont.FontType.SHADOW_THIN, this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.property.setValue((Boolean)this.property.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean)this.property.getValue();
    }
}

