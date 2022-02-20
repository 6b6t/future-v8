/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.EntityHelper;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.combat.AutoHeal;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;

public final class BowAimbot
extends ToggleableModule {
    private final NumberProperty<Integer> ticks = new NumberProperty<Integer>(Integer.valueOf(51), 0, 100, "Ticks-Existed", "te", "ticks", "existed");
    private final NumberProperty<Integer> fov = new NumberProperty<Integer>(Integer.valueOf(60), 30, 180, "Fov", "f");
    private final NumberProperty<Float> reach = new NumberProperty<Float>(Float.valueOf(50.0f), Float.valueOf(6.0f), Float.valueOf(80.0f), "Reach", "range", "r", "distance", "dist");
    private final Property<Boolean> players = new Property<Boolean>(true, "Players", "player", "p", "player");
    private final Property<Boolean> animals = new Property<Boolean>(false, "Animals", "ani", "animal");
    private final Property<Boolean> invisibles = new Property<Boolean>(true, "Invisibles", "invis", "inv", "invisible");
    private final Property<Boolean> monsters = new Property<Boolean>(false, "Monsters", "monster", "mon", "m", "monst");
    private final Property<Boolean> silent = new Property<Boolean>(true, "Silent", "s", "lock");
    private final List<EntityLivingBase> validTargets = new CopyOnWriteArrayList<EntityLivingBase>();
    private EntityLivingBase target = null;

    public BowAimbot() {
        super("Bow Aimbot", new String[]{"bowaimbot", "ba", "bowaim", "bow"}, -3358823, ModuleType.COMBAT);
        this.offerProperties(this.ticks, this.reach, this.players, this.animals, this.invisibles, this.monsters, this.silent, this.fov);
        this.listeners.add(new Listener<MotionUpdateEvent>("bow_aimbot_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getTime()) {
                    case BEFORE: {
                        if (BowAimbot.this.validTargets.isEmpty()) {
                            for (Object object : ((BowAimbot)BowAimbot.this).minecraft.theWorld.loadedEntityList) {
                                EntityLivingBase entityLivingBase2;
                                Entity entity = (Entity)object;
                                if (!(entity instanceof EntityLivingBase) || !BowAimbot.this.isValidEntity(entityLivingBase2 = (EntityLivingBase)entity) || BowAimbot.this.validTargets.size() >= 5) continue;
                                BowAimbot.this.validTargets.add(entityLivingBase2);
                            }
                        }
                        BowAimbot.this.validTargets.forEach(entityLivingBase -> {
                            if (!BowAimbot.this.isValidEntity(entityLivingBase)) {
                                BowAimbot.this.validTargets.remove(entityLivingBase);
                            }
                        });
                        BowAimbot.this.target = BowAimbot.this.getClosestEntity();
                        if (BowAimbot.this.isValidEntity(BowAimbot.this.target)) {
                            AutoHeal autoHeal = (AutoHeal)Exeter.getInstance().getModuleManager().getModuleByAlias("autoheal");
                            EnumProperty mode = (EnumProperty)autoHeal.getPropertyByAlias("Mode");
                            if (autoHeal != null && autoHeal.isRunning() && mode.getValue() == AutoHeal.Mode.POTION && autoHeal.isPotting()) {
                                return;
                            }
                            float[] rotations = EntityHelper.getRotations(BowAimbot.this.target);
                            if (((BowAimbot)BowAimbot.this).minecraft.thePlayer.getCurrentEquippedItem() == null) {
                                return;
                            }
                            if (!(((BowAimbot)BowAimbot.this).minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
                                return;
                            }
                            event.setLockview((Boolean)BowAimbot.this.silent.getValue() == false);
                            if (!((BowAimbot)BowAimbot.this).minecraft.thePlayer.isUsingItem()) break;
                            if (((Boolean)BowAimbot.this.silent.getValue()).booleanValue()) {
                                event.setRotationYaw(PlayerHelper.wrapAngleTo180(rotations[0]));
                                event.setRotationPitch(PlayerHelper.wrapAngleTo180(rotations[1] + ((BowAimbot)BowAimbot.this).minecraft.thePlayer.getDistanceToEntity(BowAimbot.this.target) * -0.15f));
                                break;
                            }
                            ((BowAimbot)BowAimbot.this).minecraft.thePlayer.rotationYaw = PlayerHelper.wrapAngleTo180(rotations[0]);
                            ((BowAimbot)BowAimbot.this).minecraft.thePlayer.rotationPitch = PlayerHelper.wrapAngleTo180(rotations[1] + ((BowAimbot)BowAimbot.this).minecraft.thePlayer.getDistanceToEntity(BowAimbot.this.target) * -0.15f);
                            break;
                        }
                        BowAimbot.this.validTargets.remove(BowAimbot.this.target);
                        BowAimbot.this.target = null;
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.target = null;
        this.validTargets.clear();
    }

    private boolean isValidEntity(EntityLivingBase entity) {
        if (entity == null || entity.isDead || !entity.isEntityAlive() || this.minecraft.thePlayer.getDistanceToEntity(entity) > ((Float)this.reach.getValue()).floatValue() || entity.ticksExisted < (Integer)this.ticks.getValue() || !this.minecraft.thePlayer.canEntityBeSeen(entity)) {
            return false;
        }
        if (entity instanceof IMob) {
            return this.monsters.getValue();
        }
        if (entity instanceof IAnimals) {
            return this.animals.getValue();
        }
        if (entity instanceof EntityPlayer) {
            if (!this.players.getValue().booleanValue()) {
                return false;
            }
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (entityPlayer.equals(this.minecraft.thePlayer) || entityPlayer.capabilities.isCreativeMode) {
                return false;
            }
            if (entityPlayer.isInvisible()) {
                return this.invisibles.getValue();
            }
            return !Exeter.getInstance().getFriendManager().isFriend(entityPlayer.getName());
        }
        return true;
    }

    private EntityLivingBase getClosestEntity() {
        double range = ((Float)this.reach.getValue()).floatValue();
        EntityLivingBase closest = null;
        for (EntityLivingBase entity : this.validTargets) {
            float distance = this.minecraft.thePlayer.getDistanceToEntity(entity);
            if (!((double)distance < range) || !this.isValidEntity(entity) || !PlayerHelper.isAiming(EntityHelper.getRotations(entity)[0], EntityHelper.getRotations(entity)[1], (Integer)this.fov.getValue())) continue;
            closest = entity;
            range = distance;
        }
        return closest;
    }
}

