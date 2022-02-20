/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.events.PotionDecrementEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public final class PotionSaver
extends ToggleableModule {
    private final Potion[] potions = new Potion[]{Potion.ABSORPTION, Potion.DAMAGE_BOOST, Potion.MOVE_SPEED, Potion.DIG_SPEED, Potion.FIRE_RESISTANCE, Potion.INVISIBILITY, Potion.JUMP_BOOST, Potion.REGENERATION, Potion.RESISTANCE, Potion.WATER_BREATHING, Potion.SATURATION};

    public PotionSaver() {
        super("PotionSaver", new String[]{"potionsaver", "ps"}, -11808797, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<PacketEvent>("potion_saver_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C03PacketPlayer && !PlayerHelper.isMoving()) {
                    for (Potion potion : PotionSaver.this.potions) {
                        if (potion == null || !((PotionSaver)PotionSaver.this).minecraft.thePlayer.isPotionActive(potion)) continue;
                        event.setCanceled(true);
                    }
                }
            }
        });
        this.listeners.add(new Listener<PotionDecrementEvent>("potion_saver_potion_deincrement_listener"){

            @Override
            public void call(PotionDecrementEvent event) {
                if (!PlayerHelper.isMoving() && ((PotionSaver)PotionSaver.this).minecraft.thePlayer.getActivePotionEffects().size() > 0) {
                    event.setCanceled(true);
                }
            }
        });
    }
}

