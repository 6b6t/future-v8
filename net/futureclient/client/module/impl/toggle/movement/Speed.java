/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.MovePlayerEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.toggle.world.Phase;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;

public final class Speed
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.HOP, "Mode", "m");
    private final Property<Boolean> ice = new Property<Boolean>(true, "Ice", "i");
    private final Property<Boolean> depthstrider = new Property<Boolean>(true, "DepthStrider", "ds");
    private int stage;
    private int cooldownHops;
    private int ticks = 0;
    private boolean isSpeeding = false;
    private boolean wasOnWater;
    private double moveSpeed;
    private double lastDist;
    private double speed;
    private double slow;

    public Speed() {
        super("Speed", new String[]{"speed", "fastrun", "swiftness"}, -10629266, ModuleType.MOVEMENT);
        this.offerProperties(this.mode, this.ice, this.depthstrider);
        this.listeners.add(new Listener<MotionUpdateEvent>("speed_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                Phase phase = (Phase)Exeter.getInstance().getModuleManager().getModuleByAlias("phase");
                if (phase != null && phase.isRunning()) {
                    return;
                }
                if (Speed.this.mode.getValue() == Mode.YPORT && Speed.this.stage == 3) {
                    event.setPositionY(event.getPositionY() + 0.4);
                }
                if (Speed.this.mode.getValue() == Mode.HOP || Speed.this.mode.getValue() == Mode.GLIDE || Speed.this.mode.getValue() == Mode.YPORT) {
                    double xDist = ((Speed)Speed.this).minecraft.thePlayer.posX - ((Speed)Speed.this).minecraft.thePlayer.prevPosX;
                    double zDist = ((Speed)Speed.this).minecraft.thePlayer.posZ - ((Speed)Speed.this).minecraft.thePlayer.prevPosZ;
                    Speed.this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                }
                if (Speed.this.mode.getValue() == Mode.SHOTBOW && event.getTime() == MotionUpdateEvent.Time.BEFORE && !((Speed)Speed.this).minecraft.thePlayer.isSneaking()) {
                    switch (Speed.this.stage) {
                        case 1: {
                            event.setPositionY(event.getPositionY() + 1.0E-4);
                            Speed.this.stage = Speed.this.stage + 1;
                            break;
                        }
                        case 2: {
                            event.setPositionY(event.getPositionY() + 2.0E-4);
                            Speed.this.stage = Speed.this.stage + 1;
                            break;
                        }
                        default: {
                            Speed.this.stage = 1;
                            if (!(((Speed)Speed.this).minecraft.thePlayer.isSneaking() || ((Speed)Speed.this).minecraft.thePlayer.moveForward == 0.0f && ((Speed)Speed.this).minecraft.thePlayer.moveStrafing == 0.0f || ((Speed)Speed.this).minecraft.gameSettings.keyBindJump.isPressed())) {
                                Speed.this.stage = 1;
                                break;
                            }
                            Speed.this.moveSpeed = Speed.this.getBaseMoveSpeed();
                        }
                    }
                }
            }
        });
        this.listeners.add(new Listener<MovePlayerEvent>("speed_move_player_listener"){

            @Override
            public void call(MovePlayerEvent event) {
                Phase phase = (Phase)Exeter.getInstance().getModuleManager().getModuleByAlias("phase");
                if (phase != null && phase.isRunning()) {
                    return;
                }
                if (((Boolean)Speed.this.ice.getValue()).booleanValue()) {
                    Blocks.ice.slipperiness = 0.6f;
                    Blocks.packed_ice.slipperiness = 0.6f;
                }
                if (((Boolean)Speed.this.depthstrider.getValue()).booleanValue() && PlayerHelper.isInLiquid()) {
                    Speed.this.ticks++;
                    if (Speed.this.ticks == 4) {
                        Speed.this.setMoveSpeed(event, 0.4);
                    }
                    if (Speed.this.ticks >= 5) {
                        Speed.this.setMoveSpeed(event, 0.3);
                        Speed.this.ticks = 0;
                    }
                }
                float moveForward = ((Speed)Speed.this).minecraft.thePlayer.movementInput.moveForward;
                float moveStrafe = ((Speed)Speed.this).minecraft.thePlayer.movementInput.moveStrafe;
                float rotationYaw = ((Speed)Speed.this).minecraft.thePlayer.rotationYaw;
                block0 : switch ((Mode)((Object)Speed.this.mode.getValue())) {
                    case HOP: {
                        if (((ToggleableModule)Exeter.getInstance().getModuleManager().getModuleByAlias("flight")).isRunning()) {
                            return;
                        }
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || ((Speed)Speed.this).minecraft.thePlayer.isOnLadder() || ((Speed)Speed.this).minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = true;
                            return;
                        }
                        if (((Speed)Speed.this).minecraft.thePlayer.onGround) {
                            Speed.this.stage = 2;
                            ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                        }
                        if (Speed.round(((Speed)Speed.this).minecraft.thePlayer.posY - (double)((int)((Speed)Speed.this).minecraft.thePlayer.posY), 3) == Speed.round(0.138, 3)) {
                            ((Speed)Speed.this).minecraft.thePlayer.motionY -= 0.13;
                            event.setMotionY(event.getMotionY() - 0.13);
                            ((Speed)Speed.this).minecraft.thePlayer.posY -= 0.13;
                        }
                        if (Speed.this.stage == 1 && PlayerHelper.isMoving()) {
                            Speed.this.stage = 2;
                            Speed.this.moveSpeed = 1.35 * Speed.this.getBaseMoveSpeed() - 0.01;
                        } else if (Speed.this.stage == 2) {
                            Speed.this.stage = 3;
                            if (PlayerHelper.isMoving()) {
                                ((Speed)Speed.this).minecraft.thePlayer.motionY = 0.4;
                                event.setMotionY(0.4);
                                if (Speed.this.cooldownHops > 0) {
                                    Speed.this.cooldownHops = Speed.this.cooldownHops - 1;
                                }
                                Speed.this.moveSpeed = Speed.this.moveSpeed * 2.149;
                            }
                        } else if (Speed.this.stage == 3) {
                            Speed.this.stage = 4;
                            double difference = 0.66 * (Speed.this.lastDist - Speed.this.getBaseMoveSpeed());
                            Speed.this.moveSpeed = Speed.this.lastDist - difference;
                        } else {
                            if (((Speed)Speed.this).minecraft.theWorld.getCollidingBoundingBoxes(((Speed)Speed.this).minecraft.thePlayer, ((Speed)Speed.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, ((Speed)Speed.this).minecraft.thePlayer.motionY, 0.0)).size() > 0 || ((Speed)Speed.this).minecraft.thePlayer.isCollidedVertically) {
                                Speed.this.stage = 1;
                            }
                            Speed.this.moveSpeed = Speed.this.lastDist - Speed.this.lastDist / 159.0;
                        }
                        Speed.this.moveSpeed = Math.max(Speed.this.moveSpeed, Speed.this.getBaseMoveSpeed());
                        if (moveForward == 0.0f && moveStrafe == 0.0f) {
                            event.setMotionX(0.0);
                            event.setMotionZ(0.0);
                            Speed.this.moveSpeed = 0.0;
                        } else if (moveForward != 0.0f) {
                            if (moveStrafe >= 1.0f) {
                                rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                                moveStrafe = 0.0f;
                            } else if (moveStrafe <= -1.0f) {
                                rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                                moveStrafe = 0.0f;
                            }
                            if (moveForward > 0.0f) {
                                moveForward = 1.0f;
                            } else if (moveForward < 0.0f) {
                                moveForward = -1.0f;
                            }
                        }
                        double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
                        double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
                        if (Speed.this.cooldownHops == 0) {
                            event.setMotionX((double)moveForward * Speed.this.moveSpeed * motionX + (double)moveStrafe * Speed.this.moveSpeed * motionZ);
                            event.setMotionZ((double)moveForward * Speed.this.moveSpeed * motionZ - (double)moveStrafe * Speed.this.moveSpeed * motionX);
                        }
                        ((Speed)Speed.this).minecraft.thePlayer.stepHeight = 0.6f;
                        if (moveForward != 0.0f || moveStrafe != 0.0f) break;
                        event.setMotionX(0.0);
                        event.setMotionZ(0.0);
                        break;
                    }
                    case YPORT: {
                        if (((ToggleableModule)Exeter.getInstance().getModuleManager().getModuleByAlias("flight")).isRunning()) {
                            return;
                        }
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || !((Speed)Speed.this).minecraft.thePlayer.onGround || ((Speed)Speed.this).minecraft.thePlayer.isOnLadder() || ((Speed)Speed.this).minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = true;
                            return;
                        }
                        if (((Speed)Speed.this).minecraft.thePlayer.isCollidedHorizontally || !PlayerHelper.isMoving()) break;
                        if (((Speed)Speed.this).minecraft.thePlayer.onGround) {
                            if (Speed.this.stage == 2) {
                                Speed.this.moveSpeed = Speed.this.moveSpeed * 2.149;
                                Speed.this.stage = 3;
                            } else if (Speed.this.stage == 3) {
                                Speed.this.stage = 2;
                                double difference = 0.66 * (Speed.this.lastDist - Speed.this.getBaseMoveSpeed());
                                Speed.this.moveSpeed = Speed.this.lastDist - difference;
                            } else {
                                List collidingList = ((Speed)Speed.this).minecraft.theWorld.getCollidingBoundingBoxes(((Speed)Speed.this).minecraft.thePlayer, ((Speed)Speed.this).minecraft.thePlayer.getBoundingBox().offset(0.0, ((Speed)Speed.this).minecraft.thePlayer.motionY, 0.0));
                                if (collidingList.size() > 0 || ((Speed)Speed.this).minecraft.thePlayer.isCollidedVertically) {
                                    Speed.this.stage = 1;
                                }
                            }
                        } else {
                            ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                        }
                        Speed.this.moveSpeed = Math.max(Speed.this.moveSpeed, Speed.this.getBaseMoveSpeed());
                        Speed.this.setMoveSpeed(event, Speed.this.moveSpeed);
                        break;
                    }
                    case OLD: {
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || ((Speed)Speed.this).minecraft.thePlayer.isOnLadder() || ((Speed)Speed.this).minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
                            Speed.this.speed = 1.0;
                            Speed.this.slow = 0.0;
                            Speed.this.wasOnWater = true;
                            return;
                        }
                        if (((Speed)Speed.this).minecraft.thePlayer.onGround) {
                            Speed.this.speed = 3.11;
                            Speed.this.slow = 1.425;
                            switch (++Speed.this.ticks) {
                                case 1: {
                                    ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.375f;
                                    ((Speed)Speed.this).minecraft.thePlayer.motionX *= Speed.this.speed;
                                    ((Speed)Speed.this).minecraft.thePlayer.motionZ *= Speed.this.speed;
                                    break block0;
                                }
                                case 2: {
                                    ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.01f;
                                    ((Speed)Speed.this).minecraft.thePlayer.motionX /= Speed.this.slow;
                                    ((Speed)Speed.this).minecraft.thePlayer.motionZ /= Speed.this.slow;
                                    break block0;
                                }
                                case 3: {
                                    break block0;
                                }
                            }
                            Speed.this.ticks = 0;
                            break;
                        }
                        ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                        ((Speed)Speed.this).minecraft.thePlayer.motionX *= 0.98;
                        ((Speed)Speed.this).minecraft.thePlayer.motionZ *= 0.98;
                        Speed.this.ticks = 2;
                        break;
                    }
                    case SHOTBOW: {
                        if (((ToggleableModule)Exeter.getInstance().getModuleManager().getModuleByAlias("flight")).isRunning()) {
                            return;
                        }
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || ((Speed)Speed.this).minecraft.thePlayer.isOnLadder() || ((Speed)Speed.this).minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = true;
                            return;
                        }
                        if (((Speed)Speed.this).minecraft.thePlayer.isCollidedHorizontally || !PlayerHelper.isMoving() || !((Speed)Speed.this).minecraft.thePlayer.onGround || ((Speed)Speed.this).minecraft.thePlayer.isSneaking()) break;
                        switch (Speed.this.stage) {
                            case 1: {
                                Speed.this.moveSpeed = 0.579;
                                break;
                            }
                            case 2: {
                                Speed.this.moveSpeed = 0.66781;
                                break;
                            }
                            default: {
                                Speed.this.moveSpeed = Speed.this.getBaseMoveSpeed();
                            }
                        }
                        Speed.this.moveSpeed = Math.max(Speed.this.moveSpeed, Speed.this.getBaseMoveSpeed());
                        Speed.this.setMoveSpeed(event, Speed.this.moveSpeed);
                        break;
                    }
                    case GLIDE: {
                        if (!((Speed)Speed.this).minecraft.thePlayer.capabilities.isFlying) {
                            if (PlayerHelper.isMoving() && !PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid()) {
                                if (((Speed)Speed.this).minecraft.thePlayer.onGround && Speed.this.stage > 1) {
                                    Speed.this.stage = -5;
                                }
                                if (Speed.this.stage < 0 && PlayerHelper.isMoving()) {
                                    ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                                    if (Speed.this.stage % 2 == 0) {
                                        ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.21f;
                                    } else {
                                        ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                                    }
                                    ((Speed)Speed.this).minecraft.thePlayer.motionZ *= -0.1;
                                    ((Speed)Speed.this).minecraft.thePlayer.motionX *= -0.1;
                                } else if (Speed.this.stage == 0) {
                                    Speed.this.moveSpeed = 4.48 * Speed.this.getBaseMoveSpeed();
                                } else if (Speed.this.stage == 1) {
                                    ((Speed)Speed.this).minecraft.thePlayer.motionY = 0.4f;
                                    event.setMotionY(0.4f);
                                    ((Speed)Speed.this).minecraft.thePlayer.onGround = false;
                                    Speed.this.moveSpeed = Speed.this.moveSpeed * (double)2.149f;
                                } else if (Speed.this.stage == 2) {
                                    ((Speed)Speed.this).minecraft.thePlayer.onGround = false;
                                    double difference = (double)0.66f * (Speed.this.lastDist - Speed.this.getBaseMoveSpeed());
                                    ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.07f;
                                    Speed.this.moveSpeed = Speed.this.lastDist - difference;
                                } else {
                                    ((Speed)Speed.this).minecraft.thePlayer.onGround = false;
                                    Speed.this.moveSpeed = Speed.this.lastDist - Speed.this.lastDist / 159.0;
                                    ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.07f;
                                    Speed.this.cooldownHops++;
                                    if (Speed.this.cooldownHops > 50) {
                                        Speed.this.stage = -8;
                                        Speed.this.cooldownHops = 0;
                                    }
                                }
                                if (Speed.this.stage >= 0) {
                                    Speed.this.moveSpeed = Math.max(Speed.this.getBaseMoveSpeed(), Speed.this.moveSpeed);
                                    Speed.this.setMoveSpeed(event, Speed.this.moveSpeed);
                                    List collidingList = ((Speed)Speed.this).minecraft.theWorld.getCollidingBoundingBoxes(((Speed)Speed.this).minecraft.thePlayer, ((Speed)Speed.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, ((Speed)Speed.this).minecraft.thePlayer.motionY, 0.0));
                                    List collidingList2 = ((Speed)Speed.this).minecraft.theWorld.getCollidingBoundingBoxes(((Speed)Speed.this).minecraft.thePlayer, ((Speed)Speed.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -0.4f, 0.0));
                                    if (!(((Speed)Speed.this).minecraft.thePlayer.isCollidedVertically || collidingList.size() <= 0 && collidingList2.size() <= 0)) {
                                        float speed = -1.0E-8f;
                                        ((Speed)Speed.this).minecraft.thePlayer.motionY = speed;
                                        event.setMotionY(((Speed)Speed.this).minecraft.thePlayer.motionY);
                                    }
                                }
                                Speed.this.stage++;
                                if (Speed.this.stage <= 4) break;
                                Speed.this.stage = 4;
                                break;
                            }
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.stage = -6;
                            break;
                        }
                        Speed.this.moveSpeed = 0.0;
                        Speed.this.stage = -6;
                    }
                }
            }
        });
    }

    private void setMoveSpeed(MovePlayerEvent event, double speed) {
        double forward = this.minecraft.thePlayer.movementInput.moveForward;
        double strafe = this.minecraft.thePlayer.movementInput.moveStrafe;
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

    @Override
    protected void onEnable() {
        super.onEnable();
        if (this.mode.getValue() == Mode.YPORT) {
            this.moveSpeed = this.getBaseMoveSpeed();
            this.lastDist = 0.0;
            this.stage = 2;
        }
        if (this.mode.getValue() == Mode.SHOTBOW) {
            this.stage = 4;
            this.lastDist = 0.0;
        } else {
            this.moveSpeed = this.getBaseMoveSpeed();
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.getTimer().timerSpeed = 1.0f;
        this.moveSpeed = 0.0;
        this.stage = 0;
        Blocks.ice.slipperiness = 0.98f;
        Blocks.packed_ice.slipperiness = 0.98f;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.minecraft.thePlayer != null && this.minecraft.thePlayer.isPotionActive(Potion.MOVE_SPEED) && this.mode.getValue() != Mode.GLIDE && this.mode.getValue() != Mode.SHOTBOW) {
            int amplifier = this.minecraft.thePlayer.getActivePotionEffect(Potion.MOVE_SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static enum Mode {
        HOP,
        OLD,
        GLIDE,
        YPORT,
        SHOTBOW;

    }
}

