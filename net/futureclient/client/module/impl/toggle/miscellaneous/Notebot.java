/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public final class Notebot
extends ToggleableModule {
    public Notebot() {
        super("Notebot", new String[]{"notebot", "nb", "note", "bot"}, -5052827, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<MotionUpdateEvent>("note_bot_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
            }
        });
    }

    private void playNoteblock(BlockPos blockPos) {
        this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(blockPos, -1, this.minecraft.thePlayer.getCurrentEquippedItem(), -1.0f, -1.0f, -1.0f));
    }
}

