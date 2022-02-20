/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public final class Notebot
extends ToggleableModule {
    public Notebot() {
        super("Note bot", new String[]{"notebot", "nb", "note", "bot"}, -5052827, ModuleType.MISCELLANEOUS);
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

