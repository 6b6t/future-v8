/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.StepEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.movement.Speed;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import java.util.List;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Step
extends ToggleableModule {
    private final NumberProperty<Float> height = new NumberProperty<Float>(Float.valueOf(1.1f), Float.valueOf(1.1f), Float.valueOf(10.0f), "Height", "h");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.INSTANT, "Mode", "m");
    private final Stopwatch stopwatch = new Stopwatch();

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
                if (!PlayerHelper.isMoving() || !((Step)Step.this).minecraft.thePlayer.onGround || PlayerHelper.isInLiquid() || PlayerHelper.isInsideBlock() || PlayerHelper.isOnLiquid() || !((Step)Step.this).minecraft.thePlayer.isCollidedHorizontally || !((Step)Step.this).minecraft.thePlayer.isCollidedVertically || ((Step)Step.this).minecraft.thePlayer.isOnLadder()) {
                    return;
                }
                switch (event.getTime()) {
                    case BEFORE: {
                        event.setHeight(((Float)Step.this.height.getValue()).floatValue());
                        break;
                    }
                    case AFTER: {
                        if (!Step.this.stopwatch.hasCompleted(60L)) break;
                        Step.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Step)Step.this).minecraft.thePlayer.posX, ((Step)Step.this).minecraft.thePlayer.posY + 0.41, ((Step)Step.this).minecraft.thePlayer.posZ, true));
                        Step.this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((Step)Step.this).minecraft.thePlayer.posX, ((Step)Step.this).minecraft.thePlayer.posY + 0.75, ((Step)Step.this).minecraft.thePlayer.posZ, true));
                        Step.this.stopwatch.reset();
                    }
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("step_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)Step.this.mode.getValue())) {
                    case PRE: {
                        List boxes = ((Step)Step.this).minecraft.theWorld.getCollidingBoundingBoxes(((Step)Step.this).minecraft.thePlayer, ((Step)Step.this).minecraft.thePlayer.getEntityBoundingBox().expand(1.0, 0.0, 1.0));
                        if (boxes.isEmpty() || !PlayerHelper.isMoving() || ((Step)Step.this).minecraft.gameSettings.keyBindJump.getIsKeyPressed() || !((Step)Step.this).minecraft.thePlayer.onGround || !Step.this.stopwatch.hasCompleted(250L)) break;
                        ((Step)Step.this).minecraft.thePlayer.motionY = 0.4;
                        Step.this.stopwatch.reset();
                        break;
                    }
                    case INSTANT: {
                        if (!((Step)Step.this).minecraft.thePlayer.isCollidedHorizontally || !((Step)Step.this).minecraft.thePlayer.onGround || PlayerHelper.isInsideBlock() || PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid()) break;
                        event.setCanceled(true);
                        break;
                    }
                    case VANILLA: {
                        ((Step)Step.this).minecraft.thePlayer.stepHeight = ((Float)Step.this.height.getValue()).floatValue();
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.thePlayer.stepHeight = 0.5f;
    }

    public static enum Mode {
        PRE,
        INSTANT,
        VANILLA;

    }
}

