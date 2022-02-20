/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.lwjgl.opengl.GL11
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.Property;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.opengl.GL11;

public final class Blink
extends ToggleableModule {
    private final Property<Boolean> breadcrumbs = new Property<Boolean>(true, "Breadcrumbs", "b", "crumbs");
    private final List<Packet> reservedPackets = new ArrayList<Packet>();
    private final List<Crumb> crumbs = new ArrayList<Crumb>();

    public Blink() {
        super("Blink", new String[]{"blink", "fakelag"}, -8887814, ModuleType.MOVEMENT);
        this.offerProperties(this.breadcrumbs);
        this.listeners.add(new Listener<MotionUpdateEvent>("blink_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (((Boolean)Blink.this.breadcrumbs.getValue()).booleanValue() && !Blink.this.isRecorded(((Blink)Blink.this).minecraft.thePlayer.posX, ((Blink)Blink.this).minecraft.thePlayer.posY, ((Blink)Blink.this).minecraft.thePlayer.posZ)) {
                    Blink.this.crumbs.add(new Crumb(((Blink)Blink.this).minecraft.thePlayer.posX, ((Blink)Blink.this).minecraft.thePlayer.posY, ((Blink)Blink.this).minecraft.thePlayer.posZ));
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("blink_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging) {
                    Blink.this.reservedPackets.add(event.getPacket());
                    event.setCanceled(true);
                }
            }
        });
        this.listeners.add(new Listener<RenderEvent>("blink_render_listener"){

            @Override
            public void call(RenderEvent event) {
                if (!((Boolean)Blink.this.breadcrumbs.getValue()).booleanValue()) {
                    return;
                }
                GlStateManager.pushMatrix();
                RenderMethods.enableGL3D();
                GL11.glColor4f((float)0.27f, (float)0.7f, (float)0.27f, (float)1.0f);
                GL11.glBegin((int)3);
                Blink.this.crumbs.forEach(crumb -> {
                    double x2 = crumb.getX() - ((Blink)Blink.this).minecraft.getRenderManager().renderPosX;
                    double y2 = crumb.getY() - (double)((Blink)Blink.this).minecraft.thePlayer.height + 3.0 - ((Blink)Blink.this).minecraft.getRenderManager().renderPosY;
                    double z2 = crumb.getZ() - ((Blink)Blink.this).minecraft.getRenderManager().renderPosZ;
                    GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                });
                GL11.glEnd();
                RenderMethods.disableGL3D();
                GlStateManager.popMatrix();
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        EntityOtherPlayerMP entity = new EntityOtherPlayerMP(this.minecraft.theWorld, new GameProfile(this.minecraft.thePlayer.getUniqueID(), this.minecraft.thePlayer.getCommandSenderEntity().getName()));
        this.minecraft.theWorld.addEntityToWorld(-1337, entity);
        entity.setPositionAndRotation(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.getEntityBoundingBox().minY, this.minecraft.thePlayer.posZ, this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.rotationPitch);
        entity.onLivingUpdate();
        this.crumbs.clear();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.reservedPackets.forEach(packet -> this.minecraft.getNetHandler().addToSendQueue((Packet)packet));
        this.reservedPackets.clear();
        this.crumbs.clear();
        this.minecraft.theWorld.removeEntityFromWorld(-1337);
    }

    private boolean isRecorded(double x2, double y2, double z2) {
        Iterator<Crumb> iterator = this.crumbs.iterator();
        if (iterator.hasNext()) {
            Crumb crumb = iterator.next();
            return crumb.getX() == x2 && crumb.getY() == y2 && crumb.getZ() == z2;
        }
        return false;
    }

    private class Crumb {
        private final double x;
        private final double y;
        private final double z;

        public Crumb(double x2, double y2, double z2) {
            this.x = x2;
            this.y = y2;
            this.z = z2;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}

