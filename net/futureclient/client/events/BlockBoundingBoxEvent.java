/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.events;

import com.gitlab.nuf.api.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockBoundingBoxEvent
extends Event {
    private Block block;
    private AxisAlignedBB boundingBox;
    private BlockPos blockPos;

    public BlockBoundingBoxEvent(Block block, AxisAlignedBB boundingBox, BlockPos blockPos) {
        this.block = block;
        this.boundingBox = boundingBox;
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}

