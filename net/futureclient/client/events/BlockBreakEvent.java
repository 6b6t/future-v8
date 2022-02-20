/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.events;

import me.friendly.api.event.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockBreakEvent
extends Event {
    private BlockPos blockPos;
    private EnumFacing enumFacing;

    public BlockBreakEvent(BlockPos blockPos, EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public void setEnumFacing(EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}

