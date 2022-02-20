/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.events.TickEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import net.minecraft.util.EnumParticleTypes;

public final class Trails
extends ToggleableModule {
    private final EnumProperty<ParticleType> particleType = new EnumProperty<ParticleType>(ParticleType.BARRIER, "Particle");
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(250L), 1L, 10000L, "Delay", "d");
    private final NumberProperty<Double> xOffset = new NumberProperty<Double>(Double.valueOf(0.0), -10.0, 10.0, "X Offset", "xoffset", "xo");
    private final NumberProperty<Double> yOffset = new NumberProperty<Double>(Double.valueOf(2.7), -10.0, 10.0, "Y Offset", "yoffset", "yo");
    private final NumberProperty<Double> zOffset = new NumberProperty<Double>(Double.valueOf(0.0), -10.0, 10.0, "Z Offset", "zoffset", "zo");
    private final Stopwatch stopwatch = new Stopwatch();

    public Trails() {
        super("Trails", new String[]{"trails"}, ModuleType.RENDER);
        this.offerProperties(this.delay, this.particleType, this.xOffset, this.yOffset, this.zOffset);
        this.listeners.add(new Listener<TickEvent>("trails_tick_listener"){

            @Override
            public void call(TickEvent event) {
                if (Trails.this.stopwatch.hasCompleted((Long)Trails.this.delay.getValue())) {
                    ((Trails)Trails.this).minecraft.theWorld.spawnParticle(((ParticleType)((Object)((Trails)Trails.this).particleType.getValue())).particleType, ((Trails)Trails.this).minecraft.thePlayer.posX + (Double)Trails.this.xOffset.getValue(), ((Trails)Trails.this).minecraft.thePlayer.posY + (Double)Trails.this.yOffset.getValue(), ((Trails)Trails.this).minecraft.thePlayer.posZ + (Double)Trails.this.zOffset.getValue(), 0.0, 0.0, 0.0, new int[0]);
                    Trails.this.stopwatch.reset();
                }
            }
        });
    }

    public static enum ParticleType {
        BARRIER(EnumParticleTypes.BARRIER),
        CLOUD(EnumParticleTypes.CLOUD),
        CRIT(EnumParticleTypes.CRIT),
        EXPLOSION_NORMAL(EnumParticleTypes.EXPLOSION_NORMAL),
        EXPLOSION_LARGE(EnumParticleTypes.EXPLOSION_LARGE),
        EXPLOSION_HUGE(EnumParticleTypes.EXPLOSION_HUGE),
        DRIP_LAVA(EnumParticleTypes.DRIP_LAVA),
        DRIP_WATER(EnumParticleTypes.DRIP_WATER);

        public EnumParticleTypes particleType;

        private ParticleType(EnumParticleTypes particleType) {
            this.particleType = particleType;
        }
    }
}
