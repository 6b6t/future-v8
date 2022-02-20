/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.events.TickEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Mouse;

public final class AutoClicker
extends ToggleableModule {
    private final NumberProperty<Integer> cps = new NumberProperty<Integer>(Integer.valueOf(9), 1, 25, "CPS", "clicks", "click");
    private final Stopwatch stopwatch = new Stopwatch();

    public AutoClicker() {
        super("Auto Clicker", new String[]{"autoclicker", "ac", "clicker"}, -4615980, ModuleType.COMBAT);
        this.offerProperties(this.cps);
        this.listeners.add(new Listener<TickEvent>("auto_clicker_tick_listener"){

            @Override
            public void call(TickEvent event) {
                if (((AutoClicker)AutoClicker.this).minecraft.currentScreen == null && Mouse.isButtonDown((int)0)) {
                    if (AutoClicker.this.stopwatch.hasCompleted(1000L / (long)((Integer)AutoClicker.this.cps.getValue()).intValue())) {
                        KeyBinding.setKeyBindState(-100, true);
                        KeyBinding.onTick(-100);
                        AutoClicker.this.stopwatch.reset();
                    } else {
                        KeyBinding.setKeyBindState(-100, false);
                    }
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("auto_clicker_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                C02PacketUseEntity c02PacketUseEntity;
                if (event.getPacket() instanceof C02PacketUseEntity && (c02PacketUseEntity = (C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && c02PacketUseEntity.getEntityFromWorld(((AutoClicker)AutoClicker.this).minecraft.theWorld) instanceof EntityPlayer) {
                    EntityPlayer entityPlayer = (EntityPlayer)c02PacketUseEntity.getEntityFromWorld(((AutoClicker)AutoClicker.this).minecraft.theWorld);
                    if (Exeter.getInstance().getFriendManager().isFriend(entityPlayer.getName())) {
                        event.setCanceled(true);
                    }
                }
            }
        });
    }
}

