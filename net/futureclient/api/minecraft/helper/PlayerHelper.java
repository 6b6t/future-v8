/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.minecraft.helper;

import com.gitlab.nuf.api.minecraft.helper.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public final class PlayerHelper {
    private static Minecraft minecraft = Minecraft.getMinecraft();

    public static Block getBlockBelowPlayer(double height) {
        return WorldHelper.getBlock(PlayerHelper.minecraft.thePlayer.posX, PlayerHelper.minecraft.thePlayer.posY - height, PlayerHelper.minecraft.thePlayer.posZ);
    }

    public static Block getBlockAbovePlayer(double height) {
        return WorldHelper.getBlock(PlayerHelper.minecraft.thePlayer.posX, PlayerHelper.minecraft.thePlayer.posY + height, PlayerHelper.minecraft.thePlayer.posZ);
    }

    public static boolean isInLiquid() {
        return PlayerHelper.getBlockBelowPlayer(0.0) instanceof BlockLiquid;
    }

    public static boolean isOnLiquid() {
        int y2 = (int)PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().offset((double)0.0, (double)-0.01, (double)0.0).minY;
        for (int x2 = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minX); x2 < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x2) {
            for (int z2 = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minZ); z2 < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z2) {
                Block block = WorldHelper.getBlock(x2, y2, z2);
                if (block == null || block instanceof BlockAir || !(block instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isInsideBlock() {
        for (int x2 = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minX); x2 < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x2) {
            for (int y2 = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minY); y2 < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxY) + 1; ++y2) {
                for (int z2 = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minZ); z2 < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z2) {
                    AxisAlignedBB boundingBox;
                    Block block = WorldHelper.getBlock(x2, y2, z2);
                    if (block == null || block instanceof BlockAir || (boundingBox = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x2, y2, z2))) == null || !PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAiming(float yaw, float pitch, int fov) {
        yaw = PlayerHelper.wrapAngleTo180(yaw);
        pitch = PlayerHelper.wrapAngleTo180(pitch);
        float curYaw = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationYaw);
        float curPitch = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationPitch);
        float yawDiff = Math.abs(yaw - curYaw);
        float pitchDiff = Math.abs(pitch - curPitch);
        return yawDiff <= (float)fov && pitchDiff <= (float)fov;
    }

    public static float wrapAngleTo180(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static boolean isMoving() {
        return (double)PlayerHelper.minecraft.thePlayer.moveForward != 0.0 || (double)PlayerHelper.minecraft.thePlayer.moveStrafing != 0.0;
    }

    public static boolean isPressingMoveKeybinds() {
        return PlayerHelper.minecraft.gameSettings.keyBindForward.getIsKeyPressed() || PlayerHelper.minecraft.gameSettings.keyBindBack.getIsKeyPressed() || PlayerHelper.minecraft.gameSettings.keyBindLeft.getIsKeyPressed() || PlayerHelper.minecraft.gameSettings.keyBindRight.getIsKeyPressed();
    }

    public static void damagePlayer() {
        for (int index = 0; index < 81; ++index) {
            PlayerHelper.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerHelper.minecraft.thePlayer.posX, PlayerHelper.minecraft.thePlayer.posY + 0.05, PlayerHelper.minecraft.thePlayer.posZ, false));
            PlayerHelper.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerHelper.minecraft.thePlayer.posX, PlayerHelper.minecraft.thePlayer.posY, PlayerHelper.minecraft.thePlayer.posZ, false));
        }
    }

    public static void drownPlayer() {
        for (int index = 0; index < 500; ++index) {
            minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer());
            minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer());
            minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer());
            minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer());
            minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer());
        }
    }

    public static String getFacingWithProperCapitals() {
        String directionLabel;
        switch (directionLabel = minecraft.getRenderViewEntity().getDirectionFacing().getName()) {
            case "north": {
                directionLabel = "North";
                break;
            }
            case "south": {
                directionLabel = "South";
                break;
            }
            case "west": {
                directionLabel = "West";
                break;
            }
            case "east": {
                directionLabel = "East";
            }
        }
        return directionLabel;
    }
}

