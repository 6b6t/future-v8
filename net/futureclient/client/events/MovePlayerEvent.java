/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.events;

import com.gitlab.nuf.api.event.Event;
import net.minecraft.client.Minecraft;

public class MovePlayerEvent
extends Event {
    private double motionX;
    private double motionY;
    private double motionZ;
    private boolean safe;

    public MovePlayerEvent(double motionX, double motionY, double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        if (Minecraft.getMinecraft().thePlayer != null) {
            this.safe = Minecraft.getMinecraft().thePlayer.isSneaking();
        }
    }

    public double getMotionY() {
        return this.motionY;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    public boolean isSafe() {
        return this.safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }
}

