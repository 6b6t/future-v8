/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.item;

import com.gitlab.nuf.api.interfaces.Labeled;

public class Item
implements Labeled {
    private final String label;
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    public Item(String label) {
        this.label = label;
    }

    public void setLocation(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
    }

    @Override
    public final String getLabel() {
        return this.label;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

