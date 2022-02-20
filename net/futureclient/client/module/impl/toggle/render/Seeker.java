/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.AxisAlignedBB;

public final class Seeker
extends ToggleableModule {
    public Seeker() {
        super("Seeker", new String[]{"seeker", "seek", "hide"}, -7285564, ModuleType.RENDER);
        this.listeners.add(new Listener<RenderEvent>("tracers_render_listener"){

            @Override
            public void call(RenderEvent event) {
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                for (Entity entity : ((Seeker)Seeker.this).minecraft.theWorld.loadedEntityList) {
                    if (!entity.isEntityAlive() || !(entity instanceof EntityFallingBlock)) continue;
                    double x2 = Seeker.this.interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks(), ((Seeker)Seeker.this).minecraft.getRenderManager().renderPosX);
                    double y2 = Seeker.this.interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks(), ((Seeker)Seeker.this).minecraft.getRenderManager().renderPosY);
                    double z2 = Seeker.this.interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks(), ((Seeker)Seeker.this).minecraft.getRenderManager().renderPosZ);
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x2 - 0.5, y2, z2 - 0.5, x2 + 0.5, y2 + 1.0, z2 + 0.5);
                    float distance = ((Seeker)Seeker.this).minecraft.thePlayer.getDistanceToEntity(entity);
                    if (distance <= 32.0f) {
                        GlStateManager.color(1.0f, distance / 32.0f, 0.0f, 0.35f);
                    } else {
                        GlStateManager.color(0.0f, 0.9f, 0.0f, 0.35f);
                    }
                    RenderMethods.drawBox(axisAlignedBB);
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
    }

    private double interpolate(double lastI, double i2, float ticks, double ownI) {
        return lastI + (i2 - lastI) * (double)ticks - ownI;
    }
}

