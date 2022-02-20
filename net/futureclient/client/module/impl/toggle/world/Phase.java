/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.world;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.events.AirBobbingEvent;
import com.gitlab.nuf.exeter.events.BlockBoundingBoxEvent;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.events.PushOutOfBlocksEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public final class Phase
extends ToggleableModule {
    private final Property<Boolean> slow = new Property<Boolean>(false, "Slow", "s");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.SAND, "Mode", "m");
    private final Stopwatch stopwatch = new Stopwatch();

    public Phase() {
        super("Phase", new String[]{"phase", "noclip"}, -3367207, ModuleType.WORLD);
        this.offerProperties(this.mode, this.slow);
        this.listeners.add(new Listener<MovePlayerEvent>("phase_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                if (((Boolean)Phase.this.slow.getValue()).booleanValue()) {
                    event.setMotionX(event.getMotionX() * 0.4);
                    event.setMotionZ(event.getMotionZ() * 0.4);
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("phase_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                float direction = ((Phase)Phase.this).minecraft.thePlayer.rotationYaw;
                if (((Phase)Phase.this).minecraft.thePlayer.moveForward < 0.0f) {
                    direction += 180.0f;
                }
                if (((Phase)Phase.this).minecraft.thePlayer.moveStrafing > 0.0f) {
                    direction -= 90.0f * (((Phase)Phase.this).minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (((Phase)Phase.this).minecraft.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f));
                }
                if (((Phase)Phase.this).minecraft.thePlayer.moveStrafing < 0.0f) {
                    direction += 90.0f * (((Phase)Phase.this).minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (((Phase)Phase.this).minecraft.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f));
                }
                double x2 = Math.cos(Math.toRadians(direction + 90.0f)) * 0.2;
                double z2 = Math.sin(Math.toRadians(direction + 90.0f)) * 0.2;
                double ix2 = (double)Phase.this.minecraft.getRenderViewEntity().getDirectionFacing().getDirectionVec().getX() * 0.1;
                double iz2 = (double)Phase.this.minecraft.getRenderViewEntity().getDirectionFacing().getDirectionVec().getZ() * 0.1;
                Phase.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Phase)Phase.this).minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                switch ((Mode)((Object)Phase.this.mode.getValue())) {
                    case SAND: {
                        ((Phase)Phase.this).minecraft.thePlayer.motionY = 0.0;
                        if (((Phase)Phase.this).minecraft.gameSettings.keyBindJump.getIsKeyPressed()) {
                            ((Phase)Phase.this).minecraft.thePlayer.motionY = 0.3;
                            break;
                        }
                        if (!((Phase)Phase.this).minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) break;
                        ((Phase)Phase.this).minecraft.thePlayer.motionY = -0.3;
                        break;
                    }
                    case PARA: {
                        ((Phase)Phase.this).minecraft.thePlayer.motionY = 0.0;
                        if (!((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || PlayerHelper.isInsideBlock()) break;
                        if (PlayerHelper.getFacingWithProperCapitals().equalsIgnoreCase("EAST")) {
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + 0.5, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX + 1.0, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ);
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            break;
                        }
                        if (PlayerHelper.getFacingWithProperCapitals().equalsIgnoreCase("WEST")) {
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX - 0.5, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX - 1.0, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ);
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            break;
                        }
                        if (PlayerHelper.getFacingWithProperCapitals().equalsIgnoreCase("NORTH")) {
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ - 0.5, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ - 1.0);
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            break;
                        }
                        if (!PlayerHelper.getFacingWithProperCapitals().equalsIgnoreCase("SOUTH")) break;
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + 0.5, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                        ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + 1.0);
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                        break;
                    }
                    case VERTICAL: {
                        switch (event.getTime()) {
                            case AFTER: {
                                Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY - 0.05, ((Phase)Phase.this).minecraft.thePlayer.posZ, true));
                                if (((Phase)Phase.this).minecraft.thePlayer.isCollidedVertically || !PlayerHelper.isInsideBlock()) break;
                                Phase.this.setRunning(false);
                            }
                        }
                    }
                    case SKIP: {
                        if (!Phase.this.stopwatch.hasCompleted(150L) || !((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally) break;
                        double[] yOffsets = new double[]{-0.025f, -0.028571428997176036, -0.033333333830038704, -0.04000000059604645, -0.05f, -0.06666666766007741, -0.1f, -0.2f, -0.04000000059604645, -0.033333333830038704, -0.028571428997176036, -0.025f};
                        for (int index = 0; index < yOffsets.length; ++index) {
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY + yOffsets[index], ((Phase)Phase.this).minecraft.thePlayer.posZ, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + x2 * (double)index, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + z2 * (double)index, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                        }
                        Phase.this.stopwatch.reset();
                        break;
                    }
                    case DOOR: {
                        if (!((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || PlayerHelper.isInsideBlock() || !Phase.this.stopwatch.hasCompleted(200L)) break;
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY + 0.05, ((Phase)Phase.this).minecraft.thePlayer.posZ, true));
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + ix2 * 4.0, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + iz2 * 4.0, true));
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ, true));
                        Phase.this.stopwatch.reset();
                        break;
                    }
                    case TEST: {
                        ((Phase)Phase.this).minecraft.thePlayer.motionY = 0.0;
                        if (!((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || !Phase.this.stopwatch.hasCompleted(2000L)) break;
                        Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, ((Phase)Phase.this).minecraft.thePlayer.posY + 2.0, ((Phase)Phase.this).minecraft.thePlayer.posZ, true));
                        Phase.this.stopwatch.reset();
                    }
                }
                Phase.this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(((Phase)Phase.this).minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        });
        this.listeners.add(new Listener<AirBobbingEvent>("phase_air_bobbing_listener"){

            @Override
            public void call(AirBobbingEvent event) {
                event.setCanceled(true);
            }
        });
        this.listeners.add(new Listener<PushOutOfBlocksEvent>("phase_push_out_of_blocks_listener"){

            @Override
            public void call(PushOutOfBlocksEvent event) {
                event.setCanceled(true);
            }
        });
        this.listeners.add(new Listener<BlockBoundingBoxEvent>("phase_block_bounding_box_listener"){

            @Override
            public void call(BlockBoundingBoxEvent event) {
                switch ((Mode)((Object)Phase.this.mode.getValue())) {
                    case SAND: 
                    case PARA: 
                    case VERTICAL: 
                    case TEST: {
                        event.setBoundingBox(null);
                    }
                }
            }
        });
    }

    public static enum Mode {
        SAND,
        PARA,
        VERTICAL,
        SKIP,
        DOOR,
        TEST;

    }
}

