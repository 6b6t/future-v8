//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\aesthetical\Documents\Minecraft\decomped\mappings\1.8.9"!

/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.api.minecraft.helper;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public final class EntityHelper {
    private static Minecraft minecraft = Minecraft.getMinecraft();

    public static Entity getClosestEntity(double x2, double y2, double z2, float distance) {
        double var9 = -1.0;
        Entity closestEntity = null;
        for (int loadedEntity = 0; loadedEntity < EntityHelper.minecraft.theWorld.loadedEntityList.size(); ++loadedEntity) {
            Entity entity = (Entity)EntityHelper.minecraft.theWorld.loadedEntityList.get(loadedEntity);
            if (!IEntitySelector.NOT_SPECTATING.apply((Object)entity)) continue;
            double distanceSq = entity.getDistanceSq(x2, y2, z2);
            if (!((double)distance < 0.0) && !(distanceSq < (double)(distance * distance)) || var9 != -1.0 && !(distanceSq < var9)) continue;
            var9 = distanceSq;
            closestEntity = entity;
        }
        return closestEntity;
    }

    public static float[] getRotations(Entity entity) {
        double positionX = entity.posX - EntityHelper.minecraft.thePlayer.posX;
        double positionZ = entity.posZ - EntityHelper.minecraft.thePlayer.posZ;
        double positionY = entity.posY + (double)entity.getEyeHeight() / 1.3 - (EntityHelper.minecraft.thePlayer.posY + (double)EntityHelper.minecraft.thePlayer.getEyeHeight());
        double positions = MathHelper.sqrt_double(positionX * positionX + positionZ * positionZ);
        float yaw = (float)(Math.atan2(positionZ, positionX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(positionY, positions) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsAtLocation(Location location, Entity entity) {
        double locationMath;
        double positionX = entity.posX - EntityHelper.minecraft.thePlayer.posX;
        double positionZ = entity.posZ - EntityHelper.minecraft.thePlayer.posZ;
        switch (location) {
            case HEAD: {
                locationMath = 1.0;
                break;
            }
            case BODY: {
                locationMath = 1.3;
                break;
            }
            case LEGS: {
                locationMath = 2.9;
                break;
            }
            case FEET: {
                locationMath = 4.0;
                break;
            }
            default: {
                locationMath = 1.3;
            }
        }
        double positionY = entity.posY + (double)entity.getEyeHeight() / locationMath - (EntityHelper.minecraft.thePlayer.posY + (double)EntityHelper.minecraft.thePlayer.getEyeHeight());
        double positions = MathHelper.sqrt_double(positionX * positionX + positionZ * positionZ);
        float yaw = (float)(Math.atan2(positionZ, positionX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(positionY, positions) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static UUID getUUID(EntityPlayer entityPlayer) {
        return entityPlayer.getUniqueID();
    }

    public static enum Location {
        HEAD,
        BODY,
        LEGS,
        FEET;

    }
}

