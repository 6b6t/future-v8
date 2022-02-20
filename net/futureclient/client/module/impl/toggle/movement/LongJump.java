/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import java.util.List;
import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.MovePlayerEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public final class LongJump
extends ToggleableModule {
    private final NumberProperty<Float> boost = new NumberProperty<Float>(Float.valueOf(4.48f), Float.valueOf(1.0f), Float.valueOf(20.0f), "Boost", "b");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.VIRTUE, "Mode", "m");
    private int stage;
    private int lastHDistance;
    private int airTicks;
    private int headStart;
    private int groundTicks;
    private double moveSpeed;
    private double lastDist;
    private boolean isSpeeding;

    public LongJump() {
        super("LongJump", new String[]{"longjump", "jump", "lj"}, -9868951, ModuleType.MOVEMENT);
        this.offerProperties(this.boost, this.mode);
        this.listeners.add(new Listener<MovePlayerEvent>("long_jump_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                if (LongJump.this.mode.getValue() == Mode.DIREKT) {
                    return;
                }
                if (LongJump.this.mode.getValue() == Mode.VIRTUE && (((LongJump)LongJump.this).minecraft.thePlayer.moveForward != 0.0f || ((LongJump)LongJump.this).minecraft.thePlayer.moveStrafing != 0.0f && !PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid())) {
                    if (LongJump.this.stage == 0) {
                        LongJump.this.moveSpeed = (double)((Float)LongJump.this.boost.getValue()).floatValue() * LongJump.this.getBaseMoveSpeed();
                    } else if (LongJump.this.stage == 1) {
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionY = 0.42;
                        event.setMotionY(0.42);
                        LongJump.this.moveSpeed = LongJump.this.moveSpeed * 2.149;
                    } else if (LongJump.this.stage == 2) {
                        double difference = 0.66 * (LongJump.this.lastDist - LongJump.this.getBaseMoveSpeed());
                        LongJump.this.moveSpeed = LongJump.this.lastDist - difference;
                    } else {
                        LongJump.this.moveSpeed = LongJump.this.lastDist - LongJump.this.lastDist / 159.0;
                    }
                    LongJump.this.moveSpeed = Math.max(LongJump.this.getBaseMoveSpeed(), LongJump.this.moveSpeed);
                    LongJump.this.setMoveSpeed(event, LongJump.this.moveSpeed);
                    List collidingList = ((LongJump)LongJump.this).minecraft.theWorld.getCollidingBoundingBoxes(((LongJump)LongJump.this).minecraft.thePlayer, ((LongJump)LongJump.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, ((LongJump)LongJump.this).minecraft.thePlayer.motionY, 0.0));
                    List collidingList2 = ((LongJump)LongJump.this).minecraft.theWorld.getCollidingBoundingBoxes(((LongJump)LongJump.this).minecraft.thePlayer, ((LongJump)LongJump.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -0.4, 0.0));
                    if (!(((LongJump)LongJump.this).minecraft.thePlayer.isCollidedVertically || collidingList.size() <= 0 && collidingList2.size() <= 0)) {
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionY = -0.001;
                        event.setMotionY(-0.001);
                    }
                    LongJump.this.stage = LongJump.this.stage + 1;
                } else if (LongJump.this.stage > 0) {
                    LongJump.this.setRunning(false);
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("long_jump_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)LongJump.this.mode.getValue())) {
                    case VIRTUE: {
                        if (event.getTime() == MotionUpdateEvent.Time.BEFORE) {
                            if (((LongJump)LongJump.this).minecraft.thePlayer.moveForward != 0.0f || ((LongJump)LongJump.this).minecraft.thePlayer.moveStrafing != 0.0f) {
                                double xDist = ((LongJump)LongJump.this).minecraft.thePlayer.posX - ((LongJump)LongJump.this).minecraft.thePlayer.prevPosX;
                                double zDist = ((LongJump)LongJump.this).minecraft.thePlayer.posZ - ((LongJump)LongJump.this).minecraft.thePlayer.prevPosZ;
                                LongJump.this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                                break;
                            }
                            event.setCanceled(true);
                            break;
                        }
                    }
                    case DIREKT: {
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || event.getTime() != MotionUpdateEvent.Time.BEFORE) break;
                        if (((LongJump)LongJump.this).minecraft.thePlayer.onGround) {
                            LongJump.this.lastHDistance = 0;
                        }
                        float direction = ((LongJump)LongJump.this).minecraft.thePlayer.rotationYaw + (float)(((LongJump)LongJump.this).minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (((LongJump)LongJump.this).minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (((LongJump)LongJump.this).minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (((LongJump)LongJump.this).minecraft.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f)) : 0.0f) - (((LongJump)LongJump.this).minecraft.thePlayer.moveStrafing < 0.0f ? -90.0f * (((LongJump)LongJump.this).minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (((LongJump)LongJump.this).minecraft.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f)) : 0.0f);
                        float xDir = (float)Math.cos((double)(direction + 90.0f) * Math.PI / 180.0);
                        float zDir = (float)Math.sin((double)(direction + 90.0f) * Math.PI / 180.0);
                        if (!((LongJump)LongJump.this).minecraft.thePlayer.isCollidedVertically) {
                            LongJump.this.airTicks++;
                            LongJump.this.isSpeeding = true;
                            if (((LongJump)LongJump.this).minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) {
                                ((LongJump)LongJump.this).minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.0, 2.147483647E9, 0.0, false));
                            }
                            LongJump.this.groundTicks = 0;
                            if (!((LongJump)LongJump.this).minecraft.thePlayer.isCollidedVertically) {
                                if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.07190068807140403) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.35f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.10306193759436909) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.55f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.13395038817442878) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.67f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.16635183030382) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.69f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.19088711097794803) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.71f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.21121925191528862) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.2f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.11979897632390576) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.93f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.18758479151225355) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.72f;
                                } else if (((LongJump)LongJump.this).minecraft.thePlayer.motionY == -0.21075983825251726) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= (double)0.76f;
                                }
                                if (((LongJump)LongJump.this).minecraft.thePlayer.motionY < -0.2 && ((LongJump)LongJump.this).minecraft.thePlayer.motionY > -0.24) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= 0.7;
                                }
                                if (((LongJump)LongJump.this).minecraft.thePlayer.motionY < -0.25 && ((LongJump)LongJump.this).minecraft.thePlayer.motionY > -0.32) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= 0.8;
                                }
                                if (((LongJump)LongJump.this).minecraft.thePlayer.motionY < -0.35 && ((LongJump)LongJump.this).minecraft.thePlayer.motionY > -0.8) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= 0.98;
                                }
                                if (((LongJump)LongJump.this).minecraft.thePlayer.motionY < -0.8 && ((LongJump)LongJump.this).minecraft.thePlayer.motionY > -1.6) {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionY *= 0.99;
                                }
                            }
                            ((LongJump)LongJump.this).minecraft.getTimer().timerSpeed = 0.8f;
                            double[] speedVals = new double[]{0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237};
                            if (((LongJump)LongJump.this).minecraft.gameSettings.keyBindForward.pressed) {
                                try {
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionX = (double)xDir * speedVals[LongJump.this.airTicks - 1] * 3.0;
                                    ((LongJump)LongJump.this).minecraft.thePlayer.motionZ = (double)zDir * speedVals[LongJump.this.airTicks - 1] * 3.0;
                                }
                                catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                                break;
                            }
                            ((LongJump)LongJump.this).minecraft.thePlayer.motionX = 0.0;
                            ((LongJump)LongJump.this).minecraft.thePlayer.motionZ = 0.0;
                            break;
                        }
                        ((LongJump)LongJump.this).minecraft.getTimer().timerSpeed = 1.0f;
                        LongJump.this.airTicks = 0;
                        LongJump.this.groundTicks++;
                        LongJump.this.headStart--;
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionX /= 13.0;
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionZ /= 13.0;
                        if (LongJump.this.groundTicks == 1) {
                            LongJump.this.updatePosition(((LongJump)LongJump.this).minecraft.thePlayer.posX, ((LongJump)LongJump.this).minecraft.thePlayer.posY, ((LongJump)LongJump.this).minecraft.thePlayer.posZ);
                            LongJump.this.updatePosition(((LongJump)LongJump.this).minecraft.thePlayer.posX + 0.0624, ((LongJump)LongJump.this).minecraft.thePlayer.posY, ((LongJump)LongJump.this).minecraft.thePlayer.posZ);
                            LongJump.this.updatePosition(((LongJump)LongJump.this).minecraft.thePlayer.posX, ((LongJump)LongJump.this).minecraft.thePlayer.posY + 0.419, ((LongJump)LongJump.this).minecraft.thePlayer.posZ);
                            LongJump.this.updatePosition(((LongJump)LongJump.this).minecraft.thePlayer.posX + 0.0624, ((LongJump)LongJump.this).minecraft.thePlayer.posY, ((LongJump)LongJump.this).minecraft.thePlayer.posZ);
                            LongJump.this.updatePosition(((LongJump)LongJump.this).minecraft.thePlayer.posX, ((LongJump)LongJump.this).minecraft.thePlayer.posY + 0.419, ((LongJump)LongJump.this).minecraft.thePlayer.posZ);
                            break;
                        }
                        if (LongJump.this.groundTicks <= 2) break;
                        LongJump.this.groundTicks = 0;
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionX = (double)xDir * 0.3;
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionZ = (double)zDir * 0.3;
                        ((LongJump)LongJump.this).minecraft.thePlayer.motionY = 0.424f;
                    }
                }
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.headStart = 4;
        this.groundTicks = 0;
        this.stage = 0;
    }

    public void updatePosition(double x, double y, double z) {
        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, this.minecraft.thePlayer.onGround));
    }

    public Block getBlock(BlockPos pos) {
        return this.minecraft.theWorld.getBlockState(pos).getBlock();
    }

    private double getDistance(EntityPlayer player, double distance) {
        List boundingBoxes = player.worldObj.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().addCoord(0.0, -distance, 0.0));
        if (boundingBoxes.isEmpty()) {
            return 0.0;
        }
        double y = 0.0;
        for (AxisAlignedBB boundingBox : boundingBoxes) {
            if (!(boundingBox.maxY > y)) continue;
            y = boundingBox.maxY;
        }
        return player.posY - y;
    }

    private void setMoveSpeed(MovePlayerEvent event, double speed) {
        MovementInput movementInput = this.minecraft.thePlayer.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = this.minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setMotionX(0.0);
            event.setMotionZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setMotionX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setMotionZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.minecraft.thePlayer != null && this.minecraft.thePlayer.isPotionActive(Potion.MOVE_SPEED)) {
            int amplifier = this.minecraft.thePlayer.getActivePotionEffect(Potion.MOVE_SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static enum Mode {
        VIRTUE,
        DIREKT;

    }
}

