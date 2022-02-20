/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.events.RenderHandEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Freecam
extends ToggleableModule {
    private final NumberProperty<Double> speed = new NumberProperty<Double>(Double.valueOf(2.0), 1.0, 10.0, "Speed", "s");
    private double startX;
    private double startY;
    private double startZ;
    private float yaw;
    private float pitch;

    public Freecam() {
        super("Freecam", new String[]{"freecam", "camera"}, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.speed);
        this.listeners.add(new Listener<MotionUpdateEvent>("freecam_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                List boxes = ((Freecam)Freecam.this).minecraft.theWorld.getCollidingBoundingBoxes(((Freecam)Freecam.this).minecraft.thePlayer, ((Freecam)Freecam.this).minecraft.thePlayer.getEntityBoundingBox().expand(0.5, 0.5, 0.5));
                boolean bl2 = ((Freecam)Freecam.this).minecraft.thePlayer.noClip = !boxes.isEmpty();
                if (!((Freecam)Freecam.this).minecraft.thePlayer.capabilities.isFlying) {
                    ((Freecam)Freecam.this).minecraft.thePlayer.capabilities.isFlying = true;
                }
                if (((Freecam)Freecam.this).minecraft.inGameHasFocus) {
                    if (((Freecam)Freecam.this).minecraft.gameSettings.keyBindJump.getIsKeyPressed()) {
                        ((Freecam)Freecam.this).minecraft.thePlayer.motionY = 0.4;
                    }
                    if (((Freecam)Freecam.this).minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) {
                        ((Freecam)Freecam.this).minecraft.thePlayer.motionY = -0.4;
                    }
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("freecam_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C03PacketPlayer && !event.isCanceled()) {
                    event.setCanceled(true);
                }
            }
        });
        this.listeners.add(new Listener<MovePlayerEvent>("freecam_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                event.setMotionX(event.getMotionX() * (Double)Freecam.this.speed.getValue());
                event.setMotionZ(event.getMotionZ() * (Double)Freecam.this.speed.getValue());
            }
        });
        this.listeners.add(new Listener<RenderHandEvent>("freecam_render_hand_listener"){

            @Override
            public void call(RenderHandEvent event) {
                event.setCanceled(true);
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.minecraft.renderGlobal.loadRenderers();
        this.startX = this.minecraft.thePlayer.posX;
        this.startY = this.minecraft.thePlayer.posY;
        this.startZ = this.minecraft.thePlayer.posZ;
        this.yaw = this.minecraft.thePlayer.rotationYaw;
        this.pitch = this.minecraft.thePlayer.rotationPitch;
        EntityOtherPlayerMP entity = new EntityOtherPlayerMP(this.minecraft.theWorld, new GameProfile(this.minecraft.thePlayer.getUniqueID(), this.minecraft.thePlayer.getCommandSenderEntity().getName()));
        this.minecraft.theWorld.addEntityToWorld(-1337, entity);
        entity.setPositionAndRotation(this.startX, this.minecraft.thePlayer.getEntityBoundingBox().minY, this.startZ, this.yaw, this.pitch);
        entity.setSneaking(this.minecraft.thePlayer.isSneaking());
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.renderGlobal.loadRenderers();
        this.minecraft.thePlayer.setPositionAndRotation(this.startX, this.startY, this.startZ, this.yaw, this.pitch);
        this.minecraft.thePlayer.noClip = false;
        this.minecraft.theWorld.removeEntityFromWorld(-1337);
        this.minecraft.thePlayer.capabilities.isFlying = false;
    }
}

