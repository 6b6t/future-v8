/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.EntityHelper;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public final class QuakeAimbot
extends ToggleableModule {
    private final Property<Boolean> silent = new Property<Boolean>(true, "Silent", "s", "lock");
    private final NumberProperty<Integer> fov = new NumberProperty<Integer>(Integer.valueOf(100), 30, 180, "Fov", "f");
    private final NumberProperty<Float> reach = new NumberProperty<Float>(Float.valueOf(60.0f), Float.valueOf(1.0f), Float.valueOf(120.0f), "Reach", "range", "r", "distance");
    private EntityLivingBase target;

    public QuakeAimbot() {
        super("QuakeAimbot", new String[]{"quakeaimbot", "qa", "quakeaim"}, -6892400, ModuleType.COMBAT);
        this.offerProperties(this.fov, this.silent, this.reach);
        this.listeners.add(new Listener<MotionUpdateEvent>("quake_aimbot_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getTime()) {
                    case BEFORE: {
                        if (QuakeAimbot.this.target == null) {
                            QuakeAimbot.this.target = QuakeAimbot.this.getClosestEntity();
                        }
                        if (QuakeAimbot.this.isValidEntity(QuakeAimbot.this.target)) {
                            event.setLockview((Boolean)QuakeAimbot.this.silent.getValue() == false);
                            float[] rotations = EntityHelper.getRotations(QuakeAimbot.this.target);
                            if (((Boolean)QuakeAimbot.this.silent.getValue()).booleanValue()) {
                                event.setRotationYaw(PlayerHelper.wrapAngleTo180(rotations[0]));
                                event.setRotationPitch(PlayerHelper.wrapAngleTo180(rotations[1]));
                                break;
                            }
                            ((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.rotationYaw = PlayerHelper.wrapAngleTo180(rotations[0]);
                            ((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.rotationPitch = PlayerHelper.wrapAngleTo180(rotations[1]);
                            break;
                        }
                        QuakeAimbot.this.target = null;
                        break;
                    }
                    case AFTER: {
                        if (QuakeAimbot.this.isValidEntity(QuakeAimbot.this.target)) {
                            if (((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.inventory.getCurrentItem() == null || !(((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe) || ((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.experienceLevel != 0) break;
                            QuakeAimbot.this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(((QuakeAimbot)QuakeAimbot.this).minecraft.thePlayer.inventory.getCurrentItem()));
                            break;
                        }
                        QuakeAimbot.this.target = null;
                    }
                }
            }
        });
    }

    private boolean isValidEntity(EntityLivingBase entity) {
        return entity != null && entity instanceof EntityPlayer && !entity.equals(this.minecraft.thePlayer) && !(this.minecraft.thePlayer.getDistanceToEntity(entity) > ((Float)this.reach.getValue()).floatValue()) && !Exeter.getInstance().getFriendManager().isFriend(entity.getName());
    }

    private EntityLivingBase getClosestEntity() {
        double range = ((Float)this.reach.getValue()).floatValue();
        EntityLivingBase closest = null;
        for (Object object : this.minecraft.theWorld.loadedEntityList) {
            EntityLivingBase entity;
            float distance;
            if (!(object instanceof EntityLivingBase) || !((double)(distance = this.minecraft.thePlayer.getDistanceToEntity(entity = (EntityLivingBase)object)) < range) || !this.isValidEntity(entity) || !PlayerHelper.isAiming(EntityHelper.getRotations(entity)[0], EntityHelper.getRotations(entity)[1], (Integer)this.fov.getValue())) continue;
            closest = entity;
            range = distance;
        }
        return closest;
    }
}

