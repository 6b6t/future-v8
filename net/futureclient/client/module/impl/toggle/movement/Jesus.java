/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.movement;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.events.BlockBoundingBoxEvent;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public final class Jesus
extends ToggleableModule {
    private boolean nextTick = false;
    private float offset = 0.02f;

    public Jesus() {
        super("Jesus", new String[]{"jesus", "watermark"}, -7807509, ModuleType.MOVEMENT);
        this.listeners.add(new Listener<MotionUpdateEvent>("jesus_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (event.getTime() == MotionUpdateEvent.Time.BEFORE && PlayerHelper.isInLiquid() && !event.isSneaking() && !((Jesus)Jesus.this).minecraft.thePlayer.isCollidedVertically) {
                    ((Jesus)Jesus.this).minecraft.thePlayer.motionY = 0.085;
                }
            }
        });
        this.listeners.add(new Listener<BlockBoundingBoxEvent>("jesus_block_bounding_box_listener"){

            @Override
            public void call(BlockBoundingBoxEvent event) {
                if (event.getState() != null && event.getState().getBlock() instanceof BlockLiquid && !((Jesus)Jesus.this).minecraft.thePlayer.isSneaking() && ((Jesus)Jesus.this).minecraft.thePlayer.fallDistance <= 3.0f && (double)event.getBlockPos().getY() < ((Jesus)Jesus.this).minecraft.thePlayer.posY - (double)Jesus.this.offset) {
                    event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), (float)event.getBlockPos().getX() + 1.0f - Jesus.this.offset, (float)event.getBlockPos().getY() + 1.0f, (float)event.getBlockPos().getZ() + 1.0f));
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("jesus_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer player = (C03PacketPlayer)event.getPacket();
                    if (PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid() && ((Jesus)Jesus.this).minecraft.thePlayer.ticksExisted % 2 == 0) {
                        player.setPositionY(player.getPositionY() - (double)0.01f);
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.nextTick = false;
    }
}

