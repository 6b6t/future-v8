/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class Tracers
extends ToggleableModule {
    private final Property<Boolean> box = new Property<Boolean>(true, "Box", "b");
    private final Property<Boolean> spine = new Property<Boolean>(false, "Spine");
    private final Property<Boolean> items = new Property<Boolean>(false, "Items", "item", "i");
    private final Property<Boolean> lines = new Property<Boolean>(true, "Lines", "line", "l");
    private final Property<Boolean> players = new Property<Boolean>(true, "Players", "player", "p");
    private final Property<Boolean> enderpearls = new Property<Boolean>(true, "Enderpearls", "epearls", "enderpearl", "pearl");
    private final Property<Boolean> invisibles = new Property<Boolean>(true, "Invisibles", "invis", "inv", "invisible");
    private final Property<Boolean> monsters = new Property<Boolean>(false, "Monsters", "monster", "mon", "m");
    private final Property<Boolean> animals = new Property<Boolean>(false, "Animals", "animal", "ani", "a");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.OUTLINE, "Mode", "m");
    private final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), "Width", "w");

    public Tracers() {
        super("Tracers", new String[]{"tracers", "entityesp", "eesp", "esp"}, ModuleType.RENDER);
        this.offerProperties(this.box, this.players, this.spine, this.enderpearls, this.width, this.items, this.monsters, this.animals, this.lines, this.invisibles, this.mode);
        this.listeners.add(new Listener<RenderEvent>("tracers_render_listener"){

            @Override
            public void call(RenderEvent event) {
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                for (Entity entity : ((Tracers)Tracers.this).minecraft.theWorld.loadedEntityList) {
                    if (!entity.isEntityAlive() || !Tracers.this.isValidEntity(entity)) continue;
                    double x2 = Tracers.this.interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks(), ((Tracers)Tracers.this).minecraft.getRenderManager().renderPosX);
                    double y2 = Tracers.this.interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks(), ((Tracers)Tracers.this).minecraft.getRenderManager().renderPosY);
                    double z2 = Tracers.this.interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks(), ((Tracers)Tracers.this).minecraft.getRenderManager().renderPosZ);
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x2 - 0.4, y2, z2 - 0.4, x2 + 0.4, y2 + 2.0, z2 + 0.4);
                    if (!(entity instanceof EntityPlayer)) {
                        axisAlignedBB = new AxisAlignedBB(x2 - 0.4, y2, z2 - 0.4, x2 + 0.4, y2 + (double)entity.getEyeHeight() + 0.35, z2 + 0.4);
                    }
                    if (entity instanceof EntityItem) {
                        axisAlignedBB = new AxisAlignedBB(x2 - 0.16, y2 + 0.13, z2 - 0.16, x2 + 0.16, y2 + (double)entity.getEyeHeight() + 0.25, z2 + 0.16);
                    } else if (entity instanceof EntityEnderPearl) {
                        axisAlignedBB = new AxisAlignedBB(x2 - 0.16, y2 - 0.2, z2 - 0.16, x2 + 0.16, y2 + (double)entity.getEyeHeight() - 0.1, z2 + 0.16);
                    }
                    if (Exeter.getInstance().getFriendManager().isFriend(entity.getName())) {
                        GlStateManager.color(0.27f, 0.7f, 0.92f, 0.45f);
                    } else {
                        float distance = ((Tracers)Tracers.this).minecraft.thePlayer.getDistanceToEntity(entity);
                        if (distance <= 32.0f) {
                            GlStateManager.color(1.0f, distance / 32.0f, 0.0f, 0.45f);
                        } else {
                            GlStateManager.color(0.0f, 0.9f, 0.0f, 0.45f);
                        }
                    }
                    boolean bobbing = ((Tracers)Tracers.this).minecraft.gameSettings.viewBobbing;
                    if (((Boolean)Tracers.this.lines.getValue()).booleanValue()) {
                        GlStateManager.pushMatrix();
                        GL11.glLineWidth((float)((Float)Tracers.this.width.getValue()).floatValue());
                        GL11.glLoadIdentity();
                        ((Tracers)Tracers.this).minecraft.gameSettings.viewBobbing = false;
                        ((Tracers)Tracers.this).minecraft.entityRenderer.orientCamera(event.getPartialTicks());
                        GL11.glBegin((int)1);
                        GL11.glVertex3d((double)0.0, (double)((Tracers)Tracers.this).minecraft.thePlayer.getEyeHeight(), (double)0.0);
                        GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                        if (((Boolean)Tracers.this.spine.getValue()).booleanValue()) {
                            GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                            GL11.glVertex3d((double)x2, (double)(y2 + (double)entity.getEyeHeight()), (double)z2);
                        }
                        GL11.glEnd();
                        GlStateManager.popMatrix();
                    }
                    if (((Boolean)Tracers.this.box.getValue()).booleanValue() && Tracers.this.mode.getValue() != Mode.OUTLINE) {
                        float distance;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x2, y2, z2);
                        GlStateManager.rotate(-entity.rotationYaw, 0.0f, entity.height, 0.0f);
                        GlStateManager.translate(-x2, -y2, -z2);
                        if (entity instanceof EntityItem || entity instanceof EntityEnderPearl) {
                            distance = ((Tracers)Tracers.this).minecraft.thePlayer.getDistanceToEntity(entity);
                            if (distance <= 32.0f) {
                                GlStateManager.color(1.0f, distance / 32.0f, 0.0f, 0.25f);
                            } else {
                                GlStateManager.color(0.0f, 0.9f, 0.0f, 0.25f);
                            }
                            RenderMethods.drawBox(axisAlignedBB);
                        } else if (Tracers.this.mode.getValue() == Mode.FILL) {
                            if (Exeter.getInstance().getFriendManager().isFriend(entity.getName())) {
                                GlStateManager.color(0.27f, 0.7f, 0.92f, 0.15f);
                            } else {
                                distance = ((Tracers)Tracers.this).minecraft.thePlayer.getDistanceToEntity(entity);
                                if (distance <= 32.0f) {
                                    GlStateManager.color(1.0f, distance / 32.0f, 0.0f, 0.15f);
                                } else {
                                    GlStateManager.color(0.0f, 0.9f, 0.0f, 0.15f);
                                }
                            }
                            RenderMethods.drawBox(axisAlignedBB);
                        }
                        RenderMethods.drawOutlinedBox(axisAlignedBB);
                        if (Tracers.this.mode.getValue() == Mode.CROSS) {
                            RenderMethods.renderCrosses(axisAlignedBB);
                        }
                        GlStateManager.popMatrix();
                    }
                    ((Tracers)Tracers.this).minecraft.gameSettings.viewBobbing = bobbing;
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
    }

    public boolean isValidEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof IMob) {
            return this.monsters.getValue();
        }
        if (entity instanceof IAnimals) {
            return this.animals.getValue();
        }
        if (entity instanceof IMob) {
            return this.monsters.getValue();
        }
        if (entity instanceof EntityItem) {
            return this.items.getValue();
        }
        if (entity instanceof EntityEnderPearl) {
            return this.enderpearls.getValue();
        }
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (entityPlayer.equals(this.minecraft.thePlayer) || entityPlayer.isInvisible() && !this.invisibles.getValue().booleanValue()) {
                return false;
            }
            return this.players.getValue();
        }
        return false;
    }

    private double interpolate(double lastI, double i2, float ticks, double ownI) {
        return lastI + (i2 - lastI) * (double)ticks - ownI;
    }

    public void renderOne() {
        GL11.glPushAttrib((int)1048575);
        GL11.glDisable((int)3008);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)(((Float)this.width.getValue()).floatValue() * 2.0f));
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2960);
        GL11.glClear((int)1024);
        GL11.glClearStencil((int)15);
        GL11.glStencilFunc((int)512, (int)1, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1028, (int)6913);
    }

    public void renderTwo() {
        GL11.glStencilFunc((int)512, (int)0, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1028, (int)6914);
    }

    public void renderThree() {
        GL11.glStencilFunc((int)514, (int)1, (int)15);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
        GL11.glPolygonMode((int)1028, (int)6913);
    }

    public void renderFour(Entity renderEntity) {
        float[] color;
        if (renderEntity instanceof EntityLivingBase) {
            float distance;
            EntityLivingBase entity = (EntityLivingBase)renderEntity;
            color = Exeter.getInstance().getFriendManager().isFriend(entity.getName()) ? new float[]{0.27f, 0.7f, 0.92f} : ((distance = this.minecraft.thePlayer.getDistanceToEntity(entity)) <= 32.0f ? new float[]{1.0f, distance / 32.0f, 0.0f} : new float[]{0.0f, 0.9f, 0.0f});
        } else {
            float distance = this.minecraft.thePlayer.getDistanceToEntity(renderEntity);
            color = distance <= 32.0f ? new float[]{1.0f, distance / 32.0f, 0.0f} : new float[]{0.0f, 0.9f, 0.0f};
        }
        GlStateManager.color(color[0], color[1], color[2], 0.85f);
        this.renderFour();
    }

    private void renderFour() {
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)1.0f, (float)-2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public void renderFive() {
        GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
        GL11.glDisable((int)10754);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2960);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glPopAttrib();
    }

    public static enum Mode {
        FILL,
        CROSS,
        BOUNDING,
        OUTLINE;

    }
}

