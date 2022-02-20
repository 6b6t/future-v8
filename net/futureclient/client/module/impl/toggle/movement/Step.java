/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.StepEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.toggle.movement.Speed;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Step
extends ToggleableModule {
    private final NumberProperty<Float> height = new NumberProperty<Float>(Float.valueOf(1.1f), Float.valueOf(1.1f), Float.valueOf(10.0f), "Height", "h");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.PACKET, "Mode", "m");
    private final Stopwatch stopwatch = new Stopwatch();
    private int fix;
    private double oldY;

    public Step() {
        super("Step", new String[]{"step", "autojump"}, ModuleType.MOVEMENT);
        this.offerProperties(this.height, this.mode);
        this.listeners.add(new Listener<StepEvent>("step_step_listener"){

            @Override
            public void call(StepEvent event) {
                Speed speed = (Speed)Exeter.getInstance().getModuleManager().getModuleByAlias("speed");
                EnumProperty mode = (EnumProperty)speed.getPropertyByAlias("Mode");
                if (speed != null && speed.isRunning() && mode.getValue() == Speed.Mode.HOP) {
                    return;
                }
                if (!PlayerHelper.isMoving() || !((Step)Step.this).minecraft.thePlayer.onGround || PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || !((Step)Step.this).minecraft.thePlayer.isCollidedHorizontally || !((Step)Step.this).minecraft.thePlayer.isCollidedVertically || ((Step)Step.this).minecraft.thePlayer.isOnLadder()) {
                    return;
                }
                switch (event.getTime()) {
                    case BEFORE: {
                        if (Step.this.fix != 0) break;
                        Step.this.oldY = ((Step)Step.this).minecraft.thePlayer.posY;
                        event.setHeight(Step.this.canStep() ? ((Float)Step.this.height.getValue()).floatValue() : 0.5f);
                        break;
                    }
                    case AFTER: {
                        double offset = ((Step)Step.this).minecraft.thePlayer.getEntityBoundingBox().minY - Step.this.oldY;
                        if (!(offset > 0.6) || Step.this.fix != 0 || !Step.this.canStep() || !Step.this.stopwatch.hasCompleted(65L)) break;
                        Step.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Step)Step.this).minecraft.thePlayer.posX, ((Step)Step.this).minecraft.thePlayer.posY + 0.42, ((Step)Step.this).minecraft.thePlayer.posZ, true));
                        Step.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Step)Step.this).minecraft.thePlayer.posX, ((Step)Step.this).minecraft.thePlayer.posY + 0.75, ((Step)Step.this).minecraft.thePlayer.posZ, true));
                        Step.this.fix = 2;
                    }
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("step_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)Step.this.mode.getValue())) {
                    case PACKET: {
                        if (event.getTime() != MotionUpdateEvent.Time.BEFORE) break;
                        if (event.getPositionY() - event.getOldPositionY() >= 0.75 && !((Step)Step.this).minecraft.theWorld.getCollidingBoundingBoxes(((Step)Step.this).minecraft.thePlayer, ((Step)Step.this).minecraft.thePlayer.getEntityBoundingBox().addCoord(0.0, -0.1, 0.0)).isEmpty()) {
                            Step.this.stopwatch.reset();
                        }
                        if (Step.this.fix <= 0) break;
                        event.setCanceled(true);
                        Step.this.fix--;
                        break;
                    }
                    case VANILLA: {
                        ((Step)Step.this).minecraft.thePlayer.stepHeight = ((Float)Step.this.height.getValue()).floatValue();
                    }
                }
            }
        });
    }

    private boolean canStep() {
        return !PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid() && this.minecraft.thePlayer.onGround && !this.minecraft.gameSettings.keyBindJump.getIsKeyPressed() && this.minecraft.thePlayer.isCollidedVertically && this.minecraft.thePlayer.isCollidedHorizontally;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.thePlayer.stepHeight = 0.5f;
    }

    public static enum Mode {
        PACKET,
        VANILLA;

    }
}

