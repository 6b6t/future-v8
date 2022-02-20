/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.events.BlockSoulSandSlowdownEvent;
import com.gitlab.nuf.exeter.events.ItemInUseEvent;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class NoSlow
extends ToggleableModule {
    public NoSlow() {
        super("No Slow", new String[]{"noslow", "noslowdown", "ns"}, ModuleType.MOVEMENT);
        this.listeners.add(new Listener<MotionUpdateEvent>("no_slow_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (((NoSlow)NoSlow.this).minecraft.thePlayer.isBlocking()) {
                    if (PlayerHelper.isMoving()) {
                        switch (event.getTime()) {
                            case BEFORE: {
                                NoSlow.this.minecraft.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                break;
                            }
                            case AFTER: {
                                NoSlow.this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(((NoSlow)NoSlow.this).minecraft.thePlayer.getCurrentEquippedItem()));
                            }
                        }
                    } else {
                        NoSlow.this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(((NoSlow)NoSlow.this).minecraft.thePlayer.getCurrentEquippedItem()));
                    }
                }
            }
        });
        this.listeners.add(new Listener<ItemInUseEvent>("no_slow_item_in_use_listener"){

            @Override
            public void call(ItemInUseEvent event) {
                if (!((NoSlow)NoSlow.this).minecraft.thePlayer.isSneaking()) {
                    event.setSpeed(1.7f);
                }
            }
        });
        this.listeners.add(new Listener<BlockSoulSandSlowdownEvent>("no_slow_block_soul_sand_slowdown_listener"){

            @Override
            public void call(BlockSoulSandSlowdownEvent event) {
                event.setCanceled(true);
            }
        });
        this.setRunning(true);
    }
}

