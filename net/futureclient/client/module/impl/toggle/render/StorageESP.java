/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.presets.Preset;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class StorageESP
extends ToggleableModule {
    private final Property<Boolean> tags = new Property<Boolean>(true, "NameTags", "plates", "tags", "labels", "np", "nameplates", "nametag", "nt");
    private final Property<Boolean> lines = new Property<Boolean>(true, "Lines", "line", "l");
    private final Property<Boolean> chest = new Property<Boolean>(true, "Chests", "chest", "c");
    private final Property<Boolean> enderchest = new Property<Boolean>(true, "EnderChests", "enderchest", "echest");
    private final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.003f), Float.valueOf(0.001f), Float.valueOf(0.01f), "Scaling", "scale", "s");
    private final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), "Width", "w");

    public StorageESP() {
        super("Storage ESP", new String[]{"storageesp", "cesp", "sesp", "chestesp"}, ModuleType.RENDER);
        this.offerProperties(this.tags, this.chest, this.enderchest, this.lines, this.width);
        this.offsetPresets(new Preset("Cluster"){

            @Override
            public void onSet() {
                StorageESP.this.tags.setValue(true);
                StorageESP.this.lines.setValue(true);
            }
        }, new Preset("Minimal"){

            @Override
            public void onSet() {
                StorageESP.this.tags.setValue(false);
                StorageESP.this.lines.setValue(false);
            }
        });
        this.listeners.add(new Listener<RenderEvent>("chest_esp_render_listener"){

            @Override
            public void call(RenderEvent event) {
                double z2;
                double y2;
                double x2;
                TileEntity tileEntity;
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                for (Object object : ((StorageESP)StorageESP.this).minecraft.theWorld.loadedTileEntityList) {
                    tileEntity = (TileEntity)object;
                    if (!StorageESP.this.shouldDraw(tileEntity)) continue;
                    x2 = (double)tileEntity.getPos().getX() - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosX;
                    y2 = (double)tileEntity.getPos().getY() - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosY;
                    z2 = (double)tileEntity.getPos().getZ() - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosZ;
                    float[] color = StorageESP.this.getColor(tileEntity);
                    AxisAlignedBB box = new AxisAlignedBB(x2, y2, z2, x2 + 1.0, y2 + 1.0, z2 + 1.0);
                    if (tileEntity instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest)TileEntityChest.class.cast(tileEntity);
                        if (chest.adjacentChestZPos != null) {
                            box = new AxisAlignedBB(x2 + 0.0625, y2, z2 + 0.0625, x2 + 0.9375, y2 + 0.875, z2 + 1.9375);
                        } else if (chest.adjacentChestXPos != null) {
                            box = new AxisAlignedBB(x2 + 0.0625, y2, z2 + 0.0625, x2 + 1.9375, y2 + 0.875, z2 + 0.9375);
                        } else {
                            if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null) continue;
                            box = new AxisAlignedBB(x2 + 0.0625, y2, z2 + 0.0625, x2 + 0.9375, y2 + 0.875, z2 + 0.9375);
                        }
                    } else if (tileEntity instanceof TileEntityEnderChest) {
                        box = new AxisAlignedBB(x2 + 0.0625, y2, z2 + 0.0625, x2 + 0.9375, y2 + 0.875, z2 + 0.9375);
                    }
                    GlStateManager.color(color[0], color[1], color[2], 0.45f);
                    boolean bobbing = ((StorageESP)StorageESP.this).minecraft.gameSettings.viewBobbing;
                    if (((Boolean)StorageESP.this.lines.getValue()).booleanValue()) {
                        GlStateManager.pushMatrix();
                        GL11.glLineWidth((float)((Float)StorageESP.this.width.getValue()).floatValue());
                        GL11.glLoadIdentity();
                        ((StorageESP)StorageESP.this).minecraft.gameSettings.viewBobbing = false;
                        ((StorageESP)StorageESP.this).minecraft.entityRenderer.orientCamera(event.getPartialTicks());
                        GL11.glBegin((int)1);
                        GL11.glVertex3d((double)0.0, (double)((StorageESP)StorageESP.this).minecraft.thePlayer.getEyeHeight(), (double)0.0);
                        GL11.glVertex3d((double)(x2 + 0.5), (double)y2, (double)(z2 + 0.5));
                        GL11.glEnd();
                        GlStateManager.popMatrix();
                    }
                    RenderMethods.drawBox(box);
                    RenderMethods.renderCrosses(box);
                    RenderMethods.drawOutlinedBox(box);
                    ((StorageESP)StorageESP.this).minecraft.gameSettings.viewBobbing = bobbing;
                }
                for (Object object : ((StorageESP)StorageESP.this).minecraft.theWorld.loadedTileEntityList) {
                    tileEntity = (TileEntity)object;
                    if (!StorageESP.this.shouldDraw(tileEntity)) continue;
                    x2 = (double)tileEntity.getPos().getX() + 0.5 - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosX;
                    y2 = (double)tileEntity.getPos().getY() - 1.0 - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosY;
                    z2 = (double)tileEntity.getPos().getZ() + 0.5 - ((StorageESP)StorageESP.this).minecraft.getRenderManager().renderPosZ;
                    if (!((Boolean)StorageESP.this.tags.getValue()).booleanValue()) continue;
                    GlStateManager.pushMatrix();
                    StorageESP.this.renderTileEntityNameTag(tileEntity, x2, y2, z2);
                    GlStateManager.popMatrix();
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
    }

    private boolean shouldDraw(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityChest && this.chest.getValue() != false || tileEntity instanceof TileEntityEnderChest && this.enderchest.getValue() != false;
    }

    private float[] getColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            Block block = tileEntity.getBlockType();
            if (block == Blocks.chest) {
                return new float[]{0.8f, 0.7f, 0.22f};
            }
            if (block == Blocks.trapped_chest) {
                return new float[]{0.8f, 0.22f, 0.22f};
            }
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return new float[]{1.0f, 0.0f, 1.0f};
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    private void renderTileEntityNameTag(TileEntity tileEntity, double x2, double y2, double z2) {
        double tempY = y2;
        tempY += 0.7;
        double distance = this.minecraft.getRenderViewEntity().getDistance(x2 + this.minecraft.getRenderManager().viewerPosX, y2 + this.minecraft.getRenderManager().viewerPosY, z2 + this.minecraft.getRenderManager().viewerPosZ);
        int width = this.minecraft.fontRenderer.getStringWidth(this.getDisplayName(tileEntity)) / 2 + 1;
        double scale = 0.0018 + (double)((Float)this.scaling.getValue()).floatValue() * distance;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x2, (float)tempY + 1.4f, (float)z2);
        GlStateManager.rotate(-this.minecraft.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(this.minecraft.getRenderManager().playerViewX, this.minecraft.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        RenderMethods.drawBorderedRectReliant(-width - 2, -(this.minecraft.fontRenderer.FONT_HEIGHT + 1), width, 1.5f, 1.6f, 0x77000000, 0x55000000);
        GlStateManager.enableAlpha();
        this.minecraft.fontRenderer.drawStringWithShadow(this.getDisplayName(tileEntity), -width, -(this.minecraft.fontRenderer.FONT_HEIGHT - 1), -1);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private String getDisplayName(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            Block block = tileEntity.getBlockType();
            if (block == Blocks.chest) {
                return "Chest";
            }
            if (block == Blocks.trapped_chest) {
                return "Trapped Chest";
            }
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return "EnderChest";
        }
        return "Unknown";
    }
}

