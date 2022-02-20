/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.geom.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.WorldHelper;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.BlockRendererEvent;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import com.sun.javafx.geom.Vec3d;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class Search
extends ToggleableModule {
    private final NumberProperty<Integer> range = new NumberProperty<Integer>(Integer.valueOf(64), 1, 128, "Range", "reach", "r");
    private final Property<Boolean> lines = new Property<Boolean>(false, "Lines", "line", "l");
    private final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), "Width", "w");
    private final List<Block> blocks = new ArrayList<Block>();
    private final List<Vec3d> vec3ds = new CopyOnWriteArrayList<Vec3d>();

    public Search() {
        super("Search", new String[]{"search"}, -4747052, ModuleType.RENDER);
        this.offerProperties(this.range, this.width, this.lines);
        this.blocks.add(Block.getBlockById(15));
        this.blocks.add(Block.getBlockById(16));
        this.listeners.add(new Listener<RenderEvent>("search_render_listener"){

            @Override
            public void call(RenderEvent event) {
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                Search.this.setTag(String.format("%s \u00a77%s", Search.this.getLabel(), Search.this.blocks.size()));
                for (Vec3d vec3d : Search.this.vec3ds) {
                    if (((Search)Search.this).minecraft.thePlayer.getDistance(vec3d.x, vec3d.y, vec3d.z) > (double)((Integer)Search.this.range.getValue()).intValue()) {
                        Search.this.vec3ds.remove(vec3d);
                        continue;
                    }
                    double x2 = vec3d.x - ((Search)Search.this).minecraft.getRenderManager().renderPosX;
                    double y2 = vec3d.y - ((Search)Search.this).minecraft.getRenderManager().renderPosY;
                    double z2 = vec3d.z - ((Search)Search.this).minecraft.getRenderManager().renderPosZ;
                    AxisAlignedBB box = new AxisAlignedBB(x2, y2, z2, x2 + 1.0, y2 + 1.0, z2 + 1.0);
                    float[] color = Search.this.getColor(WorldHelper.getBlock(vec3d.x, vec3d.y, vec3d.z));
                    GlStateManager.color(color[0], color[1], color[2], 0.25f);
                    boolean bobbing = ((Search)Search.this).minecraft.gameSettings.viewBobbing;
                    if (((Boolean)Search.this.lines.getValue()).booleanValue()) {
                        GlStateManager.pushMatrix();
                        GL11.glLineWidth((float)((Float)Search.this.width.getValue()).floatValue());
                        GL11.glLoadIdentity();
                        ((Search)Search.this).minecraft.gameSettings.viewBobbing = false;
                        ((Search)Search.this).minecraft.entityRenderer.orientCamera(event.getPartialTicks());
                        GL11.glBegin((int)1);
                        GL11.glVertex3d((double)0.0, (double)((Search)Search.this).minecraft.thePlayer.getEyeHeight(), (double)0.0);
                        GL11.glVertex3d((double)(x2 + 0.5), (double)y2, (double)(z2 + 0.5));
                        GL11.glEnd();
                        GlStateManager.popMatrix();
                    }
                    RenderMethods.drawBox(box);
                    GlStateManager.color(color[0], color[1], color[2], 0.7f);
                    RenderMethods.drawOutlinedBox(box);
                    ((Search)Search.this).minecraft.gameSettings.viewBobbing = bobbing;
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
        this.listeners.add(new Listener<BlockRendererEvent>("search_block_renderer_listener"){

            @Override
            public void call(BlockRendererEvent event) {
                Vec3d blockPos = new Vec3d((double)event.getBlockPos().getX(), (double)event.getBlockPos().getY(), (double)event.getBlockPos().getZ());
                if (Search.this.blocks.contains(event.getBlock()) && !Search.this.isValid(blockPos)) {
                    Search.this.vec3ds.add(blockPos);
                }
            }
        });
        Exeter.getInstance().getCommandManager().register(new Command(new String[]{"search", "s"}, new Argument[]{new Argument("block|clear")}){

            @Override
            public String dispatch() {
                String argument = this.getArgument("block|clear").getValue();
                if (argument.equalsIgnoreCase("clear")) {
                    Search.this.blocks.clear();
                    Search.this.vec3ds.clear();
                    return "Cleared &eSearch&7 list.";
                }
                Block block = Block.getBlockById(Integer.parseInt(argument));
                if (block == null) {
                    return "That block could not be found.";
                }
                if (Search.this.blocks.contains(block)) {
                    Search.this.blocks.remove(block);
                    Search.this.vec3ds.clear();
                    this.minecraft.renderGlobal.loadRenderers();
                    return String.format("Removed &e%s&7 from the &eSearch&7 list.", Block.getIdFromBlock(block));
                }
                Search.this.blocks.add(block);
                Search.this.vec3ds.clear();
                this.minecraft.renderGlobal.loadRenderers();
                return String.format("Added &e%s&7 to the &eSearch&7 list.", Block.getIdFromBlock(block));
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.minecraft.renderGlobal.loadRenderers();
    }

    private float[] getColor(Block block) {
        switch (Block.getIdFromBlock(block)) {
            case 56: 
            case 57: {
                return new float[]{0.27f, 0.7f, 0.92f};
            }
            case 14: 
            case 41: {
                return new float[]{0.8f, 0.7f, 0.3f};
            }
            case 15: 
            case 42: {
                return new float[]{0.4f, 0.4f, 0.4f};
            }
            case 129: 
            case 133: {
                return new float[]{0.1f, 0.5f, 0.1f};
            }
            case 73: 
            case 74: 
            case 152: {
                return new float[]{0.6f, 0.1f, 0.1f};
            }
            case 21: 
            case 22: {
                return new float[]{0.2f, 0.2f, 0.8f};
            }
            case 16: {
                return new float[]{0.0f, 0.0f, 0.0f};
            }
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    private boolean isValid(Vec3d block) {
        for (Vec3d vec3d : this.vec3ds) {
            if (vec3d.x != block.x || vec3d.y != block.y || vec3d.z != block.z) continue;
            return true;
        }
        return false;
    }
}

