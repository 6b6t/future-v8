/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.events.AirBobbingEvent;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;

public final class Glide
extends ToggleableModule {
    private final Property<Boolean> damage = new Property<Boolean>(false, "Damage", "dmg", "d");
    private final NumberProperty<Float> speed = new NumberProperty<Float>(Float.valueOf(0.01f), Float.valueOf(1.0E-4f), Float.valueOf(1.0f), "Speed", "s");

    public Glide() {
        super("Glide", new String[]{"glide", "slowfall"}, -4084276, ModuleType.MOVEMENT);
        this.offerProperties(this.damage, this.speed);
        this.listeners.add(new Listener<MotionUpdateEvent>("glide_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                ((Glide)Glide.this).minecraft.thePlayer.motionY = -((Float)Glide.this.speed.getValue()).floatValue();
                if (((Glide)Glide.this).minecraft.gameSettings.keyBindSneak.getIsKeyPressed()) {
                    ((Glide)Glide.this).minecraft.thePlayer.motionY = -0.4f;
                }
            }
        });
        this.listeners.add(new Listener<AirBobbingEvent>("glide_air_bobbing_listener"){

            @Override
            public void call(AirBobbingEvent event) {
                event.setCanceled(true);
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (this.minecraft.thePlayer == null) {
            return;
        }
        if (this.damage.getValue().booleanValue()) {
            PlayerHelper.damagePlayer();
        }
    }
}

