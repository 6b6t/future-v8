/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.MovePlayerEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.world.Phase;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.potion.Potion;

public final class Speed
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.HOP, "Mode", "m");
    private int state;
    private int cooldownHops;
    private boolean wasOnWater = false;
    private double moveSpeed;
    private double lastDist;

    public Speed() {
        super("Speed", new String[]{"speed", "fastrun", "swiftness"}, -10629266, ModuleType.MOVEMENT);
        this.offerProperties(this.mode);
        this.listeners.add(new Listener<MotionUpdateEvent>("speed_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                Phase phase = (Phase)Exeter.getInstance().getModuleManager().getModuleByAlias("phase");
                if (phase != null && phase.isRunning()) {
                    return;
                }
                if (Speed.this.mode.getValue() == Mode.HOP) {
                    if (((ToggleableModule)Exeter.getInstance().getModuleManager().getModuleByAlias("flight")).isRunning()) {
                        return;
                    }
                    if (!PlayerHelper.isMoving() && ((Speed)Speed.this).minecraft.thePlayer.onGround) {
                        Speed.this.cooldownHops = 2;
                        Speed.this.moveSpeed = Speed.this.moveSpeed * (double)1.08f;
                        Speed.this.state = 2;
                    }
                    double xDist = ((Speed)Speed.this).minecraft.thePlayer.posX - ((Speed)Speed.this).minecraft.thePlayer.prevPosX;
                    double zDist = ((Speed)Speed.this).minecraft.thePlayer.posZ - ((Speed)Speed.this).minecraft.thePlayer.prevPosZ;
                    Speed.this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
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
                switch ((Mode)((Object)Speed.this.mode.getValue())) {
                    case HOP: {
                        if (((ToggleableModule)Exeter.getInstance().getModuleManager().getModuleByAlias("flight")).isRunning()) {
                            return;
                        }
                        if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || ((Speed)Speed.this).minecraft.thePlayer.isOnLadder() || ((Speed)Speed.this).minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = true;
                            return;
                        }
                        if (Speed.this.wasOnWater || !PlayerHelper.isPressingMoveKeybinds()) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = false;
                            return;
                        }
                        if ((((Speed)Speed.this).minecraft.thePlayer.isEating() || ((Speed)Speed.this).minecraft.thePlayer.isUsingItem()) && (((Speed)Speed.this).minecraft.gameSettings.keyBindLeft.getIsKeyPressed() || ((Speed)Speed.this).minecraft.gameSettings.keyBindRight.getIsKeyPressed())) {
                            Speed.this.moveSpeed = 0.0;
                            Speed.this.wasOnWater = false;
                            return;
                        }
                        if (((Speed)Speed.this).minecraft.thePlayer.onGround) {
                            Speed.this.state = 2;
                            ((Speed)Speed.this).minecraft.getTimer().timerSpeed = 1.0f;
                        }
                        if (Speed.this.round(((Speed)Speed.this).minecraft.thePlayer.posY - (double)((int)((Speed)Speed.this).minecraft.thePlayer.posY), 3) == Speed.this.round(0.138, 3)) {
                            ((Speed)Speed.this).minecraft.thePlayer.motionY -= 0.13;
                            event.setMotionY(event.getMotionY() - 0.13);
                            ((Speed)Speed.this).minecraft.thePlayer.posY -= 0.13;
                        }
                        if (Speed.this.state == 1 && PlayerHelper.isMoving()) {
                            Speed.this.state = 2;
                            Speed.this.moveSpeed = 1.35 * Speed.this.getBaseMoveSpeed() - 0.01;
                        } else if (Speed.this.state == 2) {
                            Speed.this.state = 3;
                            if (PlayerHelper.isMoving()) {
                                ((Speed)Speed.this).minecraft.thePlayer.motionY = 0.4;
                                event.setMotionY(0.4);
                                if (Speed.this.cooldownHops > 0) {
                                    Speed.this.cooldownHops = Speed.this.cooldownHops - 1;
                                }
                                Speed.this.moveSpeed = Speed.this.moveSpeed * 2.149;
                            }
                        } else if (Speed.this.state == 3) {
                            Speed.this.state = 4;
                            double difference = 0.66 * (Speed.this.lastDist - Speed.this.getBaseMoveSpeed());
                            Speed.this.moveSpeed = Speed.this.lastDist - difference;
                        } else {
                            if (((Speed)Speed.this).minecraft.theWorld.getCollidingBoundingBoxes(((Speed)Speed.this).minecraft.thePlayer, ((Speed)Speed.this).minecraft.thePlayer.getEntityBoundingBox().offset(0.0, ((Speed)Speed.this).minecraft.thePlayer.motionY, 0.0)).size() > 0 || ((Speed)Speed.this).minecraft.thePlayer.isCollidedVertically) {
                                Speed.this.state = 1;
                            }
                            Speed.this.moveSpeed = Speed.this.lastDist - Speed.this.lastDist / 159.0;
                        }
                        Speed.this.moveSpeed = Math.max(Speed.this.moveSpeed, Speed.this.getBaseMoveSpeed());
                        float forward = ((Speed)Speed.this).minecraft.thePlayer.movementInput.moveForward;
                        float strafe = ((Speed)Speed.this).minecraft.thePlayer.movementInput.moveStrafe;
                        float yaw = ((Speed)Speed.this).minecraft.thePlayer.rotationYaw;
                        if (forward == 0.0f && strafe == 0.0f) {
                            event.setMotionX(0.0);
                            event.setMotionZ(0.0);
                            Speed.this.moveSpeed = 0.0;
                        } else if (forward != 0.0f) {
                            if (strafe >= 1.0f) {
                                yaw += forward > 0.0f ? -45.0f : 45.0f;
                                strafe = 0.0f;
                            } else if (strafe <= -1.0f) {
                                yaw += forward > 0.0f ? 45.0f : -45.0f;
                                strafe = 0.0f;
                            }
                            if (forward > 0.0f) {
                                forward = 1.0f;
                            } else if (forward < 0.0f) {
                                forward = -1.0f;
                            }
                        }
                        double motionX = Math.cos(Math.toRadians(yaw + 90.0f));
                        double motionZ = Math.sin(Math.toRadians(yaw + 90.0f));
                        if (Speed.this.cooldownHops == 0) {
                            event.setMotionX((double)forward * Speed.this.moveSpeed * motionX + (double)strafe * Speed.this.moveSpeed * motionZ);
                            event.setMotionZ((double)forward * Speed.this.moveSpeed * motionZ - (double)strafe * Speed.this.moveSpeed * motionX);
                        }
                        ((Speed)Speed.this).minecraft.thePlayer.stepHeight = 0.6f;
                        if (forward != 0.0f || strafe != 0.0f) break;
                        event.setMotionX(0.0);
                        event.setMotionZ(0.0);
                        break;
                    }
                    case VANILLA: {
                        event.setMotionX(event.getMotionX() * 2.7);
                        event.setMotionZ(event.getMotionZ() * 2.7);
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.moveSpeed = 0.0;
        this.state = 0;
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.minecraft.thePlayer.isPotionActive(Potion.MOVE_SPEED)) {
            int amplifier = this.minecraft.thePlayer.getActivePotionEffect(Potion.MOVE_SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static enum Mode {
        HOP,
        VANILLA;

    }
}

