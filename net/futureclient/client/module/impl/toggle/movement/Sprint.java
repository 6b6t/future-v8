/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.SprintingAttackEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.Property;

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

    public boolean canSprint() {
        return !this.minecraft.thePlayer.isSneaking() && this.minecraft.gameSettings.keyBindForward.getIsKeyPressed() && !this.minecraft.thePlayer.isCollidedHorizontally && (double)this.minecraft.thePlayer.moveForward > 0.0 && this.minecraft.thePlayer.getFoodStats().getFoodLevel() > 6;
    }
}

