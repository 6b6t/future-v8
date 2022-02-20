/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.EntityHelper;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.combat.AutoHeal;
import com.gitlab.nuf.exeter.presets.Preset;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class KillAura
extends ToggleableModule {
    private final NumberProperty<Long> aps = new NumberProperty<Long>(Long.valueOf(10L), 1L, 20L, "APS", "d", "delay");
    private final Property<Boolean> autoBlock = new Property<Boolean>(true, "AutoBlock", "ab", "block");
    private final Property<Boolean> direction = new Property<Boolean>(false, "Direction", "dir");
    private final Property<Boolean> aac = new Property<Boolean>(false, "AAC");
    private final Property<Boolean> rayTrace = new Property<Boolean>(true, "Ray-Trace", "raytrace", "rtrace", "trace", "ray");
    private final Property<Boolean> players = new Property<Boolean>(true, "Players", "player", "p");
    private final Property<Boolean> animals = new Property<Boolean>(true, "Animals", "animal", "a", "ani");
    private final Property<Boolean> monsters = new Property<Boolean>(true, "Monsters", "monster", "mon", "m");
    private final Property<Boolean> invisibles = new Property<Boolean>(true, "Invisibles", "invisible", "invis", "inv");
    private final Property<Boolean> silent = new Property<Boolean>(true, "Silent", "s");
    private final NumberProperty<Float> reach = new NumberProperty<Float>(Float.valueOf(4.2f), Float.valueOf(3.0f), Float.valueOf(6.0f), "Reach", "range", "r");
    private final NumberProperty<Integer> fov = new NumberProperty<Integer>(Integer.valueOf(60), 30, 180, "Fov", "f");
    private final NumberProperty<Integer> livingTicks = new NumberProperty<Integer>(Integer.valueOf(51), 0, 100, "Living-Ticks", "livingticks", "ticks", "lt");
    private final EnumProperty<Targeting> targeting = new EnumProperty<Targeting>(Targeting.SWITCH, "Targeting", "target", "t");
    private final EnumProperty<EntityHelper.Location> bone = new EnumProperty<EntityHelper.Location>(EntityHelper.Location.HEAD, "Bone", "b");
    private final List<Entity> validTargets = new CopyOnWriteArrayList<Entity>();
    private Entity target = null;
    private final Stopwatch stopwatch = new Stopwatch();

    public KillAura() {
        super("Kill Aura", new String[]{"killaura", "aura", "ka"}, -4240565, ModuleType.COMBAT);
        this.offerProperties(this.targeting, this.fov, this.autoBlock, this.direction, this.players, this.bone, this.aac, this.rayTrace, this.animals, this.monsters, this.invisibles, this.aps, this.silent, this.reach, this.livingTicks);
        this.offsetPresets(new Preset("Legit"){

            @Override
            public void onSet() {
                KillAura.this.aps.setValue(9L);
                KillAura.this.reach.setValue(Float.valueOf(3.8f));
                KillAura.this.livingTicks.setValue(51);
                KillAura.this.targeting.setValue(Targeting.SINGLE);
                KillAura.this.invisibles.setValue(false);
                KillAura.this.rayTrace.setValue(false);
                KillAura.this.animals.setValue(false);
                KillAura.this.monsters.setValue(false);
            }
        }, new Preset("Rage"){

            @Override
            public void onSet() {
                KillAura.this.aps.setValue(12L);
                KillAura.this.reach.setValue(Float.valueOf(4.2f));
                KillAura.this.livingTicks.setValue(0);
                KillAura.this.targeting.setValue(Targeting.SWITCH);
                KillAura.this.rayTrace.setValue(true);
                KillAura.this.animals.setValue(false);
                KillAura.this.monsters.setValue(false);
                KillAura.this.invisibles.setValue(true);
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("kill_aura_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getTime()) {
                    case BEFORE: {
                        if (KillAura.this.validTargets.isEmpty()) {
                            for (Object object : ((KillAura)KillAura.this).minecraft.theWorld.loadedEntityList) {
                                EntityLivingBase entityLivingBase;
                                Entity entity2 = (Entity)object;
                                if (!(entity2 instanceof EntityLivingBase) || !KillAura.this.isValidTarget(entityLivingBase = (EntityLivingBase)entity2)) continue;
                                KillAura.this.validTargets.add(entityLivingBase);
                            }
                        } else {
                            KillAura.this.validTargets.forEach(target -> {
                                if (!KillAura.this.isValidTarget(target)) {
                                    KillAura.this.validTargets.clear();
                                }
                            });
                        }
                        if (KillAura.this.validTargets.isEmpty()) {
                            return;
                        }
                        if (KillAura.this.target == null && KillAura.this.targeting.getValue() != Targeting.MULTI) {
                            KillAura.this.validTargets.forEach(entity -> {
                                if (KillAura.this.isValidTarget(entity)) {
                                    if (PlayerHelper.isAiming(EntityHelper.getRotations(entity)[0], EntityHelper.getRotations(entity)[1], (Integer)KillAura.this.fov.getValue())) {
                                        KillAura.this.target = entity;
                                    }
                                } else {
                                    KillAura.this.validTargets.remove(entity);
                                }
                            });
                        }
                        if (((Boolean)KillAura.this.direction.getValue()).booleanValue() && ((Boolean)KillAura.this.silent.getValue()).booleanValue()) {
                            event.setRotationPitch(90.0f);
                        }
                        if (KillAura.this.targeting.getValue() == Targeting.MULTI) break;
                        if (KillAura.this.isValidTarget(KillAura.this.target)) {
                            AutoHeal autoHeal = (AutoHeal)Exeter.getInstance().getModuleManager().getModuleByAlias("autoheal");
                            EnumProperty mode = (EnumProperty)autoHeal.getPropertyByAlias("Mode");
                            if (autoHeal != null && autoHeal.isRunning() && mode.getValue() == AutoHeal.Mode.POTION && autoHeal.isPotting()) {
                                return;
                            }
                            float[] rotations = EntityHelper.getRotationsAtLocation((EntityHelper.Location)((Object)KillAura.this.bone.getValue()), KillAura.this.target);
                            event.setLockview((Boolean)KillAura.this.silent.getValue() == false);
                            if (((Boolean)KillAura.this.silent.getValue()).booleanValue()) {
                                event.setRotationYaw(PlayerHelper.wrapAngleTo180(rotations[0]));
                                event.setRotationPitch(PlayerHelper.wrapAngleTo180(rotations[1]));
                                break;
                            }
                            ((KillAura)KillAura.this).minecraft.thePlayer.rotationYaw = PlayerHelper.wrapAngleTo180(rotations[0]);
                            ((KillAura)KillAura.this).minecraft.thePlayer.rotationPitch = PlayerHelper.wrapAngleTo180(rotations[1]);
                            break;
                        }
                        KillAura.this.validTargets.remove(KillAura.this.target);
                        KillAura.this.target = null;
                        break;
                    }
                    case AFTER: {
                        AutoHeal autoHeal = (AutoHeal)Exeter.getInstance().getModuleManager().getModuleByAlias("autoheal");
                        EnumProperty mode = (EnumProperty)autoHeal.getPropertyByAlias("Mode");
                        if (autoHeal != null && autoHeal.isRunning() && mode.getValue() == AutoHeal.Mode.POTION && autoHeal.isPotting()) {
                            return;
                        }
                        switch ((Targeting)((Object)KillAura.this.targeting.getValue())) {
                            case SINGLE: {
                                if (KillAura.this.isValidTarget(KillAura.this.target)) {
                                    KillAura.this.attack(KillAura.this.target);
                                    KillAura.this.blockHit();
                                    break;
                                }
                                KillAura.this.validTargets.remove(KillAura.this.target);
                                KillAura.this.target = null;
                                break;
                            }
                            case SWITCH: {
                                if (KillAura.this.isValidTarget(KillAura.this.target)) {
                                    KillAura.this.attack(KillAura.this.target);
                                    KillAura.this.blockHit();
                                    if (KillAura.this.validTargets.size() <= 1) break;
                                    KillAura.this.validTargets.remove(KillAura.this.target);
                                    KillAura.this.target = null;
                                    break;
                                }
                                KillAura.this.validTargets.remove(KillAura.this.target);
                                KillAura.this.target = null;
                                break;
                            }
                            case MULTI: {
                                if (KillAura.this.validTargets.isEmpty()) break;
                                KillAura.this.validTargets.forEach(target -> {
                                    if (KillAura.this.isValidTarget(target)) {
                                        KillAura.this.attack(target);
                                        KillAura.this.blockHit();
                                    }
                                    KillAura.this.validTargets.remove(target);
                                });
                            }
                        }
                        KillAura.this.validTargets.forEach(target -> {
                            if (!KillAura.this.isValidTarget(target)) {
                                KillAura.this.validTargets.remove(target);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.validTargets.clear();
        this.target = null;
    }

    private void attack(Entity entity) {
        if (!this.stopwatch.hasCompleted(1000L / (Long)this.aps.getValue())) {
            return;
        }
        boolean wasSprinting = this.minecraft.thePlayer.isSprinting();
        boolean wasSneaking = this.minecraft.thePlayer.isSneaking();
        boolean wasBlocking = this.minecraft.thePlayer.isBlocking();
        if (wasSprinting) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        }
        if (wasSneaking) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (wasBlocking) {
            this.minecraft.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        this.minecraft.thePlayer.swingItem();
        this.minecraft.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (wasSprinting) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
        if (wasSneaking) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        if (wasBlocking) {
            this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.getCurrentEquippedItem()));
        }
        if (this.targeting.getValue() == Targeting.MULTI && this.validTargets.size() > 1) {
            return;
        }
        this.stopwatch.reset();
    }

    private boolean isValidTarget(Entity entity) {
        if (entity == null || this.rayTrace.getValue() == false && !this.minecraft.thePlayer.canEntityBeSeen(entity) || entity.isDead || !entity.isEntityAlive() || this.minecraft.thePlayer.getDistanceToEntity(entity) > ((Float)this.reach.getValue()).floatValue() || entity.ticksExisted < (Integer)this.livingTicks.getValue()) {
            return false;
        }
        if (this.players.getValue().booleanValue() && entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            return !(entityPlayer.equals(this.minecraft.thePlayer) || this.aac.getValue() != false && !entityPlayer.onGround && (double)entityPlayer.fallDistance == 0.0 || this.invisibles.getValue() == false && entityPlayer.isInvisible() || entityPlayer.capabilities.isCreativeMode || Exeter.getInstance().getFriendManager().isFriend(entity.getName()));
        }
        return this.animals.getValue() != false && entity instanceof EntityAnimal || this.monsters.getValue() != false && entity instanceof EntityMob;
    }

    private void blockHit() {
        if (this.minecraft.thePlayer.inventory.getCurrentItem() != null && this.autoBlock.getValue().booleanValue() && this.minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            ItemSword itemSword = (ItemSword)this.minecraft.thePlayer.inventory.getCurrentItem().getItem();
            itemSword.onItemRightClick(this.minecraft.thePlayer.inventory.getCurrentItem(), this.minecraft.theWorld, this.minecraft.thePlayer);
        }
    }

    private static enum Targeting {
        SINGLE,
        SWITCH,
        MULTI;

    }
}

