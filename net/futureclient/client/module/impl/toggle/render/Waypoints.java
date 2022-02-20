/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.interfaces.Labeled;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class Waypoints
extends ToggleableModule {
    private final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), "Width", "w");
    private final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.003f), Float.valueOf(0.001f), Float.valueOf(0.01f), "Scaling", "scale", "s");
    private final List<Point> points = new ArrayList<Point>();

    public Waypoints() {
        super("Waypoints", new String[]{"waypoints", "waypoint", "points", "point", "wp"}, ModuleType.RENDER);
        this.offerProperties(this.width, this.scaling);
        Exeter.getInstance().getCommandManager().register(new Command(new String[]{"waypointsadd", "waypointadd", "pointadd", "pointsadd", "wpadd", "wadd", "padd"}, new Argument[]{new Argument("label"), new Argument("x"), new Argument("y"), new Argument("z")}){

            @Override
            public String dispatch() {
                int z2;
                int y2;
                int x2;
                String name = this.getArgument("label").getValue();
                Point point = new Point(name, x2 = Integer.parseInt(this.getArgument("x").getValue()), y2 = Integer.parseInt(this.getArgument("y").getValue()), z2 = Integer.parseInt(this.getArgument("z").getValue()));
                if (!Waypoints.this.isValidPoint(point)) {
                    Waypoints.this.points.add(point);
                }
                return String.format("Added waypoint &e%s&7.", point.getLabel());
            }
        });
        Exeter.getInstance().getCommandManager().register(new Command(new String[]{"waypointsremove", "waypointremove", "pointremove", "pointsremove", "wpremove", "wremove", "premove"}, new Argument[]{new Argument("label")}){

            @Override
            public String dispatch() {
                String name = this.getArgument("label").getValue();
                Point point = Waypoints.this.getPoint(name);
                if (point == null) {
                    return "Invalid waypoint entered.";
                }
                if (Waypoints.this.isValidPoint(point)) {
                    Waypoints.this.points.remove(point);
                }
                return String.format("Removed waypoint &e%s&7.", point.getLabel());
            }
        });
        this.listeners.add(new Listener<RenderEvent>("waypoints_render_listener"){

            @Override
            public void call(RenderEvent event) {
                double z2;
                double y2;
                double x2;
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                for (Point point : Waypoints.this.points) {
                    x2 = (double)point.getX() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosX;
                    y2 = (double)point.getY() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosY;
                    z2 = (double)point.getZ() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosZ;
                    GlStateManager.color(0.7f, 0.1f, 0.2f, 0.7f);
                    boolean bobbing = ((Waypoints)Waypoints.this).minecraft.gameSettings.viewBobbing;
                    GL11.glLineWidth((float)((Float)Waypoints.this.width.getValue()).floatValue());
                    GL11.glLoadIdentity();
                    ((Waypoints)Waypoints.this).minecraft.gameSettings.viewBobbing = false;
                    ((Waypoints)Waypoints.this).minecraft.entityRenderer.orientCamera(event.getPartialTicks());
                    GL11.glBegin((int)1);
                    GL11.glVertex3d((double)0.0, (double)((Waypoints)Waypoints.this).minecraft.thePlayer.getEyeHeight(), (double)0.0);
                    GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                    GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                    GL11.glVertex3d((double)x2, (double)(y2 + 2.0), (double)z2);
                    GL11.glEnd();
                    ((Waypoints)Waypoints.this).minecraft.gameSettings.viewBobbing = bobbing;
                }
                for (Point point : Waypoints.this.points) {
                    x2 = (double)point.getX() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosX;
                    y2 = (double)point.getY() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosY;
                    z2 = (double)point.getZ() - ((Waypoints)Waypoints.this).minecraft.getRenderManager().renderPosZ;
                    GlStateManager.pushMatrix();
                    Waypoints.this.renderPointNameTag(point, x2, y2, z2);
                    GlStateManager.popMatrix();
                }
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
    }

    private boolean isValidPoint(Point point) {
        for (Point p2 : this.points) {
            if (p2.getX() != point.getX() || p2.getY() != point.getY() || p2.getZ() != point.getZ()) continue;
            return true;
        }
        return false;
    }

    private Point getPoint(String name) {
        for (Point point : this.points) {
            if (!point.getLabel().equalsIgnoreCase(name)) continue;
            return point;
        }
        return null;
    }

    private void renderPointNameTag(Point point, double x2, double y2, double z2) {
        double tempY = y2;
        tempY += 0.7;
        double distance = this.minecraft.getRenderViewEntity().getDistance(x2 + this.minecraft.getRenderManager().viewerPosX, y2 + this.minecraft.getRenderManager().viewerPosY, z2 + this.minecraft.getRenderManager().viewerPosZ);
        int width = this.minecraft.fontRenderer.getStringWidth(point.getLabel()) / 2 + 1;
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
        RenderMethods.drawBorderedRectReliant(-width - 2, -(this.minecraft.fontRenderer.FONT_HEIGHT + 1), width, 1.5f, 1.6f, 0x77000000, -1435496416);
        GlStateManager.enableAlpha();
        this.minecraft.fontRenderer.drawStringWithShadow(point.getLabel(), -width, -(this.minecraft.fontRenderer.FONT_HEIGHT - 1), -5592406);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    public class Point
    implements Labeled {
        private final String label;
        private final int x;
        private final int y;
        private final int z;

        public Point(String label, int x2, int y2, int z2) {
            this.label = label;
            this.x = x2;
            this.y = y2;
            this.z = z2;
        }

        @Override
        public String getLabel() {
            return this.label;
        }

        public int getZ() {
            return this.z;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }
    }
}

