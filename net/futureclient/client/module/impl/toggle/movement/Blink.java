/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.lwjgl.opengl.GL11
 */
package me.friendly.exeter.module.impl.toggle.movement;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.render.RenderMethods;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.events.RenderEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.Property;
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
                    double x = crumb.getX() - ((Blink)Blink.this).minecraft.getRenderManager().renderPosX;
                    double y = crumb.getY() - (double)((Blink)Blink.this).minecraft.thePlayer.height + 3.0 - ((Blink)Blink.this).minecraft.getRenderManager().renderPosY;
                    double z = crumb.getZ() - ((Blink)Blink.this).minecraft.getRenderManager().renderPosZ;
                    GL11.glVertex3d((double)x, (double)y, (double)z);
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

    private boolean isRecorded(double x, double y, double z) {
        Iterator<Crumb> iterator = this.crumbs.iterator();
        if (iterator.hasNext()) {
            Crumb crumb = iterator.next();
            return crumb.getX() == x && crumb.getY() == y && crumb.getZ() == z;
        }
        return false;
    }

    private class Crumb {
        private final double x;
        private final double y;
        private final double z;

        public Crumb(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
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

