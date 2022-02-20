/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.minecraft.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public final class WorldHelper {
    private static Minecraft minecraft = Minecraft.getMinecraft();

    public static BlockPos getSpawnPoint() {
        return WorldHelper.minecraft.theWorld.getSpawnPoint();
    }

    public static Block getBlock(double x2, double y2, double z2) {
        return WorldHelper.minecraft.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
    }
}

