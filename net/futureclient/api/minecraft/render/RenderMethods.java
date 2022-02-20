/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.api.minecraft.render;

import java.awt.Color;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class RenderMethods {
    public static FloatBuffer matModelView = GLAllocation.createDirectFloatBuffer(16);
    public static FloatBuffer matProjection = GLAllocation.createDirectFloatBuffer(16);

    public static Color rainbow(long offset, float fade) {
        float hue = (float)(System.nanoTime() + offset) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c2 = new Color((int)color);
        return new Color((float)c2.getRed() / 255.0f * fade, (float)c2.getGreen() / 255.0f * fade, (float)c2.getBlue() / 255.0f * fade, (float)c2.getAlpha() / 255.0f);
    }

    public static Color blend(Color color1, Color color2, float ratio) {
        float rat = 1.0f - ratio;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color = new Color(rgb1[0] * ratio + rgb2[0] * rat, rgb1[1] * ratio + rgb2[1] * rat, rgb1[2] * ratio + rgb2[2] * rat);
        return color;
    }

    public static double getDiff(double lastI, double i2, float ticks, double ownI) {
        return lastI + (i2 - lastI) * (double)ticks - ownI;
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void enableGL3D() {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4353);
        GL11.glDisable((int)2896);
    }

    public static void disableGL3D() {
        GL11.glEnable((int)2896);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawTriangle(int x2, int y2, int type, int size, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)r2, (float)g2, (float)b2, (float)alpha);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)1.0f);
        GL11.glShadeModel((int)7425);
        switch (type) {
            case 0: {
                GL11.glBegin((int)2);
                GL11.glVertex2d((double)x2, (double)(y2 + size));
                GL11.glVertex2d((double)(x2 + size), (double)(y2 - size));
                GL11.glVertex2d((double)(x2 - size), (double)(y2 - size));
                GL11.glEnd();
                GL11.glBegin((int)4);
                GL11.glVertex2d((double)x2, (double)(y2 + size));
                GL11.glVertex2d((double)(x2 + size), (double)(y2 - size));
                GL11.glVertex2d((double)(x2 - size), (double)(y2 - size));
                GL11.glEnd();
                break;
            }
            case 1: {
                GL11.glBegin((int)2);
                GL11.glVertex2d((double)x2, (double)y2);
                GL11.glVertex2d((double)x2, (double)(y2 + size / 2));
                GL11.glVertex2d((double)(x2 + size + size / 2), (double)y2);
                GL11.glEnd();
                GL11.glBegin((int)4);
                GL11.glVertex2d((double)x2, (double)y2);
                GL11.glVertex2d((double)x2, (double)(y2 + size / 2));
                GL11.glVertex2d((double)(x2 + size + size / 2), (double)y2);
                GL11.glEnd();
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                GL11.glBegin((int)2);
                GL11.glVertex2d((double)x2, (double)y2);
                GL11.glVertex2d((double)((double)x2 + (double)size * 1.25), (double)(y2 - size / 2));
                GL11.glVertex2d((double)((double)x2 + (double)size * 1.25), (double)(y2 + size / 2));
                GL11.glEnd();
                GL11.glBegin((int)4);
                GL11.glVertex2d((double)((double)x2 + (double)size * 1.25), (double)(y2 - size / 2));
                GL11.glVertex2d((double)x2, (double)y2);
                GL11.glVertex2d((double)((double)x2 + (double)size * 1.25), (double)(y2 + size / 2));
                GL11.glEnd();
            }
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)lineWidth);
    }

    public static int applyTexture(int texId, int width, int height, ByteBuffer pixels, boolean linear, boolean repeat) {
        GL11.glBindTexture((int)3553, (int)texId);
        GL11.glTexParameteri((int)3553, (int)10241, (int)(linear ? 9729 : 9728));
        GL11.glTexParameteri((int)3553, (int)10240, (int)(linear ? 9729 : 9728));
        GL11.glTexParameteri((int)3553, (int)10242, (int)(repeat ? 10497 : 10496));
        GL11.glTexParameteri((int)3553, (int)10243, (int)(repeat ? 10497 : 10496));
        GL11.glPixelStorei((int)3317, (int)1);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)width, (int)height, (int)0, (int)6408, (int)5121, (ByteBuffer)pixels);
        return texId;
    }

    public static void drawLine(float x2, float y2, float x1, float y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public static void drawRect(Rectangle rectangle, int color) {
        RenderMethods.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, int color) {
        RenderMethods.enableGL2D();
        RenderMethods.glColor(color);
        RenderMethods.drawRect(x2, y2, x1, y1);
        RenderMethods.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x1, float y1, float width, int internalColor, int borderColor) {
        RenderMethods.enableGL2D();
        RenderMethods.glColor(internalColor);
        RenderMethods.drawRect(x2 + width, y2 + width, x1 - width, y1 - width);
        RenderMethods.glColor(borderColor);
        RenderMethods.drawRect(x2 + width, y2, x1 - width, y2 + width);
        RenderMethods.drawRect(x2, y2, x2 + width, y1);
        RenderMethods.drawRect(x1 - width, y2, x1, y1);
        RenderMethods.drawRect(x2 + width, y1 - width, x1 - width, y1);
        RenderMethods.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x1, float y1, int insideC, int borderC) {
        RenderMethods.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderMethods.drawVLine(x2 *= 2.0f, y2 *= 2.0f, (y1 *= 2.0f) - 1.0f, borderC);
        RenderMethods.drawVLine((x1 *= 2.0f) - 1.0f, y2, y1, borderC);
        RenderMethods.drawHLine(x2, x1 - 1.0f, y2, borderC);
        RenderMethods.drawHLine(x2, x1 - 2.0f, y1 - 1.0f, borderC);
        RenderMethods.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderMethods.disableGL2D();
    }

    public static void drawBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int inside, int border) {
        RenderMethods.enableGL2D();
        RenderMethods.drawRect(x2, y2, x1, y1, inside);
        RenderMethods.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderMethods.disableGL2D();
    }

    public static void drawGradientBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        RenderMethods.enableGL2D();
        RenderMethods.drawGradientRect(x2, y2, x1, y1, top, bottom);
        RenderMethods.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderMethods.disableGL2D();
    }

    public static void drawRoundedRect(float x2, float y2, float x1, float y1, int borderC, int insideC) {
        RenderMethods.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderMethods.drawVLine(x2 *= 2.0f, (y2 *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderMethods.drawVLine((x1 *= 2.0f) - 1.0f, y2 + 1.0f, y1 - 2.0f, borderC);
        RenderMethods.drawHLine(x2 + 2.0f, x1 - 3.0f, y2, borderC);
        RenderMethods.drawHLine(x2 + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderMethods.drawHLine(x2 + 1.0f, x2 + 1.0f, y2 + 1.0f, borderC);
        RenderMethods.drawHLine(x1 - 2.0f, x1 - 2.0f, y2 + 1.0f, borderC);
        RenderMethods.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderMethods.drawHLine(x2 + 1.0f, x2 + 1.0f, y1 - 2.0f, borderC);
        RenderMethods.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderMethods.disableGL2D();
    }

    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x2 = rectangle.x;
        float y2 = rectangle.y;
        float x1 = rectangle.x + rectangle.width;
        float y1 = rectangle.y + rectangle.height;
        RenderMethods.enableGL2D();
        RenderMethods.glColor(internalColor);
        RenderMethods.drawRect(x2 + width, y2 + width, x1 - width, y1 - width);
        RenderMethods.glColor(borderColor);
        RenderMethods.drawRect(x2 + 1.0f, y2, x1 - 1.0f, y2 + width);
        RenderMethods.drawRect(x2, y2, x2 + width, y1);
        RenderMethods.drawRect(x1 - width, y2, x1, y1);
        RenderMethods.drawRect(x2 + 1.0f, y1 - width, x1 - 1.0f, y1);
        RenderMethods.disableGL2D();
    }

    public static void drawGradientRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderMethods.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderMethods.glColor(topColor);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        RenderMethods.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderMethods.disableGL2D();
    }

    public static void drawGradientHRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderMethods.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderMethods.glColor(topColor);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x2, (float)y1);
        RenderMethods.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderMethods.disableGL2D();
    }

    public static void drawGradientRect(double x2, double y2, double x22, double y22, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        RenderMethods.glColor(col1);
        GL11.glVertex2d((double)x22, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        RenderMethods.glColor(col2);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x22, (double)y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradientBorderedRect(double x2, double y2, double x22, double y22, float l1, int col1, int col2, int col3) {
        RenderMethods.enableGL2D();
        GL11.glPushMatrix();
        RenderMethods.glColor(col1);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x22, (double)y22);
        GL11.glVertex2d((double)x22, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x22, (double)y2);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x22, (double)y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderMethods.drawGradientRect(x2, y2, x22, y22, col2, col3);
        RenderMethods.disableGL2D();
    }

    public static void drawStrip(int x2, int y2, float width, double angle, float points, float radius, int color) {
        float yc2;
        float xc2;
        float a2;
        int i2;
        float f1 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated((double)x2, (double)y2, (double)0.0);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f1);
        GL11.glLineWidth((float)width);
        if (angle > 0.0) {
            GL11.glBegin((int)3);
            i2 = 0;
            while ((double)i2 < angle) {
                a2 = (float)((double)i2 * (angle * Math.PI / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)radius);
                yc2 = (float)(Math.sin(a2) * (double)radius);
                GL11.glVertex2f((float)xc2, (float)yc2);
                ++i2;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin((int)3);
            i2 = 0;
            while ((double)i2 > angle) {
                a2 = (float)((double)i2 * (angle * Math.PI / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)(-radius));
                yc2 = (float)(Math.sin(a2) * (double)(-radius));
                GL11.glVertex2f((float)xc2, (float)yc2);
                --i2;
            }
            GL11.glEnd();
        }
        RenderMethods.disableGL2D();
        GL11.glDisable((int)3479);
        GL11.glPopMatrix();
    }

    public static void drawHLine(float x2, float y2, float x1, int y1) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        RenderMethods.drawRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x2, float y2, float x1, int y1) {
        if (x1 < y2) {
            float var5 = y2;
            y2 = x1;
            x1 = var5;
        }
        RenderMethods.drawRect(x2, y2 + 1.0f, x2 + 1.0f, x1, y1);
    }

    public static void drawHLine(float x2, float y2, float x1, int y1, int y22) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        RenderMethods.drawGradientRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1, y22);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, float r2, float g2, float b2, float a2) {
        RenderMethods.enableGL2D();
        GL11.glColor4f((float)r2, (float)g2, (float)b2, (float)a2);
        RenderMethods.drawRect(x2, y2, x1, y1);
        RenderMethods.disableGL2D();
    }

    public static void drawRect(float x2, float y2, float x1, float y1) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glEnd();
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        var10.startDrawingQuads();
        var10.addVertex(left, bottom, 0.0);
        var10.addVertex(right, bottom, 0.0);
        var10.addVertex(right, top, 0.0);
        var10.addVertex(left, top, 0.0);
        var9.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(float cx, float cy2, float r2, int num_segments, int c2) {
        r2 *= 2.0f;
        cx *= 2.0f;
        cy2 *= 2.0f;
        float f2 = (float)(c2 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c2 >> 16 & 0xFF) / 255.0f;
        float f22 = (float)(c2 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c2 & 0xFF) / 255.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p2 = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x2 = r2;
        float y2 = 0.0f;
        RenderMethods.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f22, (float)f3, (float)f2);
        GL11.glBegin((int)2);
        for (int ii2 = 0; ii2 < num_segments; ++ii2) {
            GL11.glVertex2f((float)(x2 + cx), (float)(y2 + cy2));
            float t = x2;
            x2 = p2 * x2 - s * y2;
            y2 = s * t + p2 * y2;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderMethods.disableGL2D();
    }

    public static void drawFullCircle(int cx, int cy2, double r2, int c2) {
        r2 *= 2.0;
        cx *= 2;
        cy2 *= 2;
        float f2 = (float)(c2 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c2 >> 16 & 0xFF) / 255.0f;
        float f22 = (float)(c2 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c2 & 0xFF) / 255.0f;
        RenderMethods.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f22, (float)f3, (float)f2);
        GL11.glBegin((int)6);
        for (int i2 = 0; i2 <= 360; ++i2) {
            double x2 = Math.sin((double)i2 * Math.PI / 180.0) * r2;
            double y2 = Math.cos((double)i2 * Math.PI / 180.0) * r2;
            GL11.glVertex2d((double)((double)cx + x2), (double)((double)cy2 + y2));
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderMethods.disableGL2D();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float)redRGB;
        float green = 0.003921569f * (float)greenRGB;
        float blue = 0.003921569f * (float)blueRGB;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
    }

    public static void renderCrosses(AxisAlignedBB box) {
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glEnd();
    }
}

