//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\aesthetical\Documents\Minecraft\decomped\mappings\1.8.9"!

/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.events.InputEvent;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;

public final class WorldeditESP
extends ToggleableModule {
    private Position position1;
    private Position position2;

    public WorldeditESP() {
        super("Worldedit ESP", new String[]{"worldeditesp", "wee", "weesp"}, -2835568, ModuleType.RENDER);
        this.listeners.add(new Listener<RenderEvent>("worldedit_esp_render_listener"){

            @Override
            public void call(RenderEvent event) {
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                if (WorldeditESP.this.position1 != null && WorldeditESP.this.position2 != null) {
                    double x2 = (double)WorldeditESP.this.position1.getX() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosX;
                    double y2 = (double)WorldeditESP.this.position1.getY() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosY;
                    double z2 = (double)WorldeditESP.this.position1.getZ() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosZ;
                    double x1 = (double)WorldeditESP.this.position2.getX() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosX;
                    double y1 = (double)WorldeditESP.this.position2.getY() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosY;
                    double z1 = (double)WorldeditESP.this.position2.getZ() - ((WorldeditESP)WorldeditESP.this).minecraft.getRenderManager().renderPosZ;
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x2, y2, z2, x1, y1, z1);
                    GlStateManager.color(0.1f, 0.1f, 0.3f, 1.0f);
                    RenderMethods.renderCrosses(axisAlignedBB);
                    RenderMethods.drawOutlinedBox(axisAlignedBB);
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
        this.listeners.add(new Listener<InputEvent>("worldedit_esp_input_listener"){

            @Override
            public void call(InputEvent event) {
                switch (event.getType()) {
                    case MOUSE_LEFT_CLICK: {
                        if (((WorldeditESP)WorldeditESP.this).minecraft.thePlayer.inventory.getCurrentItem() == null || !(((WorldeditESP)WorldeditESP.this).minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) || ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver == null || ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) break;
                        WorldeditESP.this.position1 = new Position(((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getX(), ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getY(), ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getZ());
                        break;
                    }
                    case MOUSE_RIGHT_CLICK: {
                        if (((WorldeditESP)WorldeditESP.this).minecraft.thePlayer.inventory.getCurrentItem() == null || !(((WorldeditESP)WorldeditESP.this).minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) || ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver == null || ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) break;
                        WorldeditESP.this.position2 = new Position(((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getX(), ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getY() + 1, ((WorldeditESP)WorldeditESP.this).minecraft.objectMouseOver.getBlockPos().getZ());
                    }
                }
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.position2 = null;
        this.position1 = null;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.position2 = null;
        this.position1 = null;
    }

    public class Position {
        private int x;
        private int y;
        private int z;

        public Position(int x2, int y2, int z2) {
            this.x = x2;
            this.y = y2;
            this.z = z2;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }
    }
}

