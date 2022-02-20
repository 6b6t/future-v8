/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.SprintingAttackEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous.Sneak;
import com.gitlab.nuf.exeter.module.impl.toggle.movement.NoSlow;
import com.gitlab.nuf.exeter.properties.Property;

public final class Sprint
extends ToggleableModule {
    private final Property<Boolean> keepSprint = new Property<Boolean>(true, "KeepSprint", "sprint", "ks", "keep");

    public Sprint() {
        super("Sprint", new String[]{"sprint", "autosprint", "as"}, -11698479, ModuleType.MOVEMENT);
        this.offerProperties(this.keepSprint);
        this.listeners.add(new Listener<MotionUpdateEvent>("sprint_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (Sprint.this.canSprint()) {
                    ((Sprint)Sprint.this).minecraft.thePlayer.setSprinting(true);
                }
            }
        });
        this.listeners.add(new Listener<SprintingAttackEvent>("sprint_sprinting_attack_listener"){

            @Override
            public void call(SprintingAttackEvent event) {
                if (((Boolean)Sprint.this.keepSprint.getValue()).booleanValue()) {
                    event.setCanceled(true);
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.minecraft.thePlayer.setSprinting(false);
    }

    private boolean canSprint() {
        NoSlow noSlow = (NoSlow)Exeter.getInstance().getModuleManager().getModuleByAlias("noslow");
        Sneak sneak = (Sneak)Exeter.getInstance().getModuleManager().getModuleByAlias("sneak");
        return (noSlow != null && noSlow.isRunning() || !this.minecraft.thePlayer.isBlocking()) && (noSlow != null && noSlow.isRunning() || !this.minecraft.thePlayer.isEating()) && (sneak != null && sneak.isRunning() || !this.minecraft.thePlayer.isSneaking()) && !this.minecraft.thePlayer.isCollidedHorizontally && (double)this.minecraft.thePlayer.moveForward > 0.0 && this.minecraft.thePlayer.getFoodStats().getFoodLevel() > 6;
    }
}

