/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.api.minecraft.helper;

import me.friendly.api.minecraft.helper.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
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
        if (PlayerHelper.minecraft.thePlayer == null) {
            return false;
        }
        boolean inLiquid = false;
        int y = (int)PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = PlayerHelper.minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block instanceof BlockAir) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                if (block instanceof BlockLiquid) {
                    return true;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static boolean isInLiquid(double offset) {
        return PlayerHelper.getBlockBelowPlayer(-offset) instanceof BlockLiquid;
    }

    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = PlayerHelper.minecraft.thePlayer.getEntityBoundingBox();
        boundingBox = boundingBox.contract(0.0, 0.0, 0.0).offset(0.0, -0.02, 0.0);
        boolean onLiquid = false;
        int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                Block block = WorldHelper.getBlock(x, y, z);
                if (block == Blocks.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    Block block = WorldHelper.getBlock(x, y, z);
                    if (block == null || block instanceof BlockAir) continue;
                    if (block instanceof BlockTallGrass) {
                        return false;
                    }
                    AxisAlignedBB boundingBox = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z));
                    if (boundingBox == null || !PlayerHelper.minecraft.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAiming(float yaw, float pitch, int fov) {
        float pitchDiff;
        yaw = PlayerHelper.wrapAngleTo180(yaw);
        pitch = PlayerHelper.wrapAngleTo180(pitch);
        float curYaw = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationYaw);
        float curPitch = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationPitch);
        float yawDiff = Math.abs(yaw - curYaw);
        return yawDiff + (pitchDiff = Math.abs(pitch - curPitch)) <= (float)fov;
    }

    public static float getFOV(float[] rotations) {
        float yaw = rotations[0];
        float pitch = rotations[1];
        yaw = PlayerHelper.wrapAngleTo180(yaw);
        pitch = PlayerHelper.wrapAngleTo180(pitch);
        float curYaw = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationYaw);
        float curPitch = PlayerHelper.wrapAngleTo180(PlayerHelper.minecraft.thePlayer.rotationPitch);
        float yawDiff = Math.abs(yaw - curYaw);
        float pitchDiff = Math.abs(pitch - curPitch);
        return yawDiff + pitchDiff;
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

