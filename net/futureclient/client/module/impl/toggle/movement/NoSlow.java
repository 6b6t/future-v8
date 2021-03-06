/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.BlockSoulSandSlowdownEvent;
import me.friendly.exeter.events.ItemInUseEvent;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.toggle.combat.AutoHeal;
import me.friendly.exeter.properties.EnumProperty;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class NoSlow
extends ToggleableModule {
    public NoSlow() {
        super("NoSlow", new String[]{"noslow", "noslowdown", "ns"}, ModuleType.MOVEMENT);
        this.listeners.add(new Listener<MotionUpdateEvent>("no_slow_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                AutoHeal autoHeal = (AutoHeal)Exeter.getInstance().getModuleManager().getModuleByAlias("autoheal");
                EnumProperty mode = (EnumProperty)autoHeal.getPropertyByAlias("Mode");
                boolean isPotting = autoHeal.isPotting();
                if (((NoSlow)NoSlow.this).minecraft.thePlayer.isBlocking() && !isPotting) {
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
                    event.setSpeed(1.0f);
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

