/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.movement;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.events.BlockBoundingBoxEvent;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public final class Jesus
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.PERFECT, "Mode", "m");
    private final Property<Boolean> lava = new Property<Boolean>(true, "Lava", "l");
    private final Property<Boolean> offset = new Property<Boolean>(false, "Offset", "off", "o");
    private boolean nextTick = false;

    public Jesus() {
        super("Jesus", new String[]{"jesus", "watermark"}, -7807509, ModuleType.MOVEMENT);
        this.offerProperties(this.mode, this.lava, this.offset);
        this.listeners.add(new Listener<MotionUpdateEvent>("jesus_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch ((Mode)((Object)Jesus.this.mode.getValue())) {
                    case OLD: 
                    case NEW: {
                        if (!PlayerHelper.isInLiquid() || ((Jesus)Jesus.this).minecraft.thePlayer.isSneaking()) break;
                        ((Jesus)Jesus.this).minecraft.thePlayer.motionY = 0.08;
                        break;
                    }
                    case PERFECT: {
                        if (!PlayerHelper.isInLiquid() || !(((Jesus)Jesus.this).minecraft.thePlayer.fallDistance < 3.0f) || ((Jesus)Jesus.this).minecraft.thePlayer.isSneaking()) break;
                        ((Jesus)Jesus.this).minecraft.thePlayer.motionY = 0.1;
                    }
                }
            }
        });
        this.listeners.add(new Listener<BlockBoundingBoxEvent>("jesus_block_bounding_box_listener"){

            @Override
            public void call(BlockBoundingBoxEvent event) {
                if (event.getBlock() instanceof BlockLiquid && ((Jesus)Jesus.this).minecraft.thePlayer.fallDistance < 3.0f && !((Jesus)Jesus.this).minecraft.thePlayer.isSneaking() && PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid()) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), (double)event.getBlockPos().getX() + 1.0, (double)event.getBlockPos().getY() + ((Boolean)Jesus.this.offset.getValue() != false ? 0.7 : (Jesus.this.mode.getValue() == Mode.PERFECT ? 0.9 : 1.0)), (double)event.getBlockPos().getZ() + 1.0));
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("jesus_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                if (Jesus.this.mode.getValue() == Mode.PERFECT) {
                    return;
                }
                if (event.getPacket() instanceof C03PacketPlayer && !((Jesus)Jesus.this).minecraft.thePlayer.isSneaking()) {
                    C03PacketPlayer player = (C03PacketPlayer)event.getPacket();
                    if (((Boolean)Jesus.this.lava.getValue()).booleanValue() && !((Jesus)Jesus.this).minecraft.gameSettings.keyBindForward.getIsKeyPressed() && !((Jesus)Jesus.this).minecraft.gameSettings.keyBindLeft.getIsKeyPressed() && !((Jesus)Jesus.this).minecraft.gameSettings.keyBindBack.getIsKeyPressed() && !((Jesus)Jesus.this).minecraft.gameSettings.keyBindRight.getIsKeyPressed() && PlayerHelper.getBlockBelowPlayer(1.0).getMaterial().equals(Material.lava) && PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid()) {
                        event.setCanceled(true);
                        return;
                    }
                    if (!player.isOnGround()) {
                        return;
                    }
                    if (PlayerHelper.getBlockBelowPlayer(1.0) instanceof BlockLiquid && PlayerHelper.isOnLiquid() && !PlayerHelper.isInLiquid()) {
                        Jesus.this.nextTick = !Jesus.this.nextTick;
                        if (Jesus.this.nextTick) {
                            player.setPositionY(player.getPositionY() - (Jesus.this.mode.getValue() == Mode.OLD ? 0.01 : 0.215));
                        }
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

    public static enum Mode {
        NEW,
        OLD,
        PERFECT;

    }
}

