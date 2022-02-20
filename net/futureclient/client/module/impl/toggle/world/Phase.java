/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.world;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.events.AirBobbingEvent;
import me.friendly.exeter.events.BlockBoundingBoxEvent;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.MovePlayerEvent;
import me.friendly.exeter.events.PushOutOfBlocksEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Phase
extends ToggleableModule {
    private final Property<Boolean> slow = new Property<Boolean>(false, "Slow", "s");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.PACKET, "Mode", "m");
    private final Stopwatch stopwatch = new Stopwatch();
    private int delay;

    public Phase() {
        super("Phase", new String[]{"phase", "noclip"}, -3367207, ModuleType.WORLD);
        this.offerProperties(this.mode, this.slow);
        this.listeners.add(new Listener<MovePlayerEvent>("phase_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                if (((Boolean)Phase.this.slow.getValue()).booleanValue()) {
                    event.setMotionX(event.getMotionX() * 0.3);
                    event.setMotionZ(event.getMotionZ() * 0.3);
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
                double x = Math.cos(Math.toRadians(direction + 90.0f)) * 0.2;
                double z = Math.sin(Math.toRadians(direction + 90.0f)) * 0.2;
                double ix = (double)Phase.this.minecraft.getRenderViewEntity().getDirectionFacing().getDirectionVec().getX() * 0.1;
                double iz = (double)Phase.this.minecraft.getRenderViewEntity().getDirectionFacing().getDirectionVec().getZ() * 0.1;
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
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + x * (double)index, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + z * (double)index, ((Phase)Phase.this).minecraft.thePlayer.onGround));
                        }
                        Phase.this.stopwatch.reset();
                        break;
                    }
                    case NEW: {
                        switch (event.getTime()) {
                            case BEFORE: {
                                if (((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally) {
                                    Phase.this.delay++;
                                }
                                if (((Phase)Phase.this).minecraft.thePlayer.motionY >= (double)0.3f) {
                                    ((Phase)Phase.this).minecraft.thePlayer.motionY = -0.3f;
                                }
                                if (((Phase)Phase.this).minecraft.thePlayer.onGround) {
                                    // empty if block
                                }
                                if (!((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || ((Phase)Phase.this).minecraft.thePlayer.moveForward == 0.0f) break;
                                double mx2 = Math.cos(Math.toRadians(((Phase)Phase.this).minecraft.thePlayer.rotationYaw + 90.0f));
                                double mz2 = Math.sin(Math.toRadians(((Phase)Phase.this).minecraft.thePlayer.rotationYaw + 90.0f));
                                double x2 = (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveForward * 0.3 * mx2 + (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
                                double z2 = (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveForward * 0.3 * mz2 - (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
                                if (Phase.this.delay < 3) break;
                                ((Phase)Phase.this).minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + x2, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + z2, true));
                                for (int i = 0; i < 1; ++i) {
                                    ((Phase)Phase.this).minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + x2, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + z2, true));
                                }
                                ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX + x2, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + z2);
                                Phase.this.delay = 0;
                            }
                        }
                        break;
                    }
                    case PACKET: {
                        if (!event.getTime().equals((Object)MotionUpdateEvent.Time.AFTER) || !((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally) break;
                        if (!((Phase)Phase.this).minecraft.thePlayer.isOnLadder()) {
                            double multiplier = 0.3;
                            double mx = Math.cos(Math.toRadians(((Phase)Phase.this).minecraft.thePlayer.rotationYaw + 90.0f));
                            double mz = Math.sin(Math.toRadians(((Phase)Phase.this).minecraft.thePlayer.rotationYaw + 90.0f));
                            double xOff = (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveForward * multiplier * mx + (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveStrafe * multiplier * mz;
                            double zOff = (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveForward * multiplier * mz - (double)((Phase)Phase.this).minecraft.thePlayer.movementInput.moveStrafe * multiplier * mx;
                            Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX + xOff, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + zOff, false));
                            for (int i = 1; i < 10; ++i) {
                                Phase.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Phase)Phase.this).minecraft.thePlayer.posX, 8.988465674311579E307, ((Phase)Phase.this).minecraft.thePlayer.posZ, false));
                            }
                            ((Phase)Phase.this).minecraft.thePlayer.setPosition(((Phase)Phase.this).minecraft.thePlayer.posX + xOff, ((Phase)Phase.this).minecraft.thePlayer.posY, ((Phase)Phase.this).minecraft.thePlayer.posZ + zOff);
                        }
                        return;
                    }
                }
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
                    case PACKET: {
                        if (!PlayerHelper.isInsideBlock() || !((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || event.getBoundingBox() == null || !(event.getBoundingBox().maxY > ((Phase)Phase.this).minecraft.thePlayer.getEntityBoundingBox().minY)) break;
                        event.setBoundingBox(null);
                        break;
                    }
                    case SAND: {
                        if (!PlayerHelper.isInsideBlock() || event.getBoundingBox() == null) break;
                        event.setBoundingBox(null);
                        break;
                    }
                    case PARA: 
                    case NEW: {
                        if (!PlayerHelper.isInsideBlock() || !((Phase)Phase.this).minecraft.thePlayer.isCollidedHorizontally || event.getBoundingBox() == null || !(event.getBoundingBox().maxY > ((Phase)Phase.this).minecraft.thePlayer.getEntityBoundingBox().minY)) break;
                        event.setBoundingBox(null);
                        break;
                    }
                    case VERTICAL: {
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
        PACKET,
        NEW;

    }
}

