/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.EntityHelper;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.toggle.combat.AutoHeal;
import me.friendly.exeter.module.impl.toggle.movement.Speed;
import me.friendly.exeter.presets.Preset;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class KillAura
extends ToggleableModule {
    private final NumberProperty<Long> aps = new NumberProperty<Long>(Long.valueOf(10L), 1L, 20L, "APS", "d", "delay");
    private final Property<Boolean> autoBlock = new Property<Boolean>(true, "AutoBlock", "ab", "block");
    private final Property<Boolean> armor = new Property<Boolean>(false, "Armor", "ArmorCheck");
    private final Property<Boolean> dura = new Property<Boolean>(false, "ArmorBreaker", "dura");
    private final Property<Boolean> team = new Property<Boolean>(false, "Team", "t");
    private final Property<Boolean> direction = new Property<Boolean>(false, "Direction", "dir");
    private final Property<Boolean> aac = new Property<Boolean>(false, "AAC");
    private final Property<Boolean> rayTrace = new Property<Boolean>(true, "Ray-Trace", "raytrace", "rtrace", "trace", "ray");
    private final Property<Boolean> players = new Property<Boolean>(true, "Players", "player", "p");
    private final Property<Boolean> animals = new Property<Boolean>(true, "Animals", "animal", "a", "ani");
    private final Property<Boolean> monsters = new Property<Boolean>(true, "Monsters", "monster", "mon", "m");
    private final Property<Boolean> invisibles = new Property<Boolean>(true, "Invisibles", "invisible", "invis", "inv");
    private final Property<Boolean> silent = new Property<Boolean>(true, "Silent", "s");
    private final NumberProperty<Float> reach = new NumberProperty<Float>(Float.valueOf(4.2f), Float.valueOf(3.0f), Float.valueOf(6.0f), "Reach", "range", "r");
    private final NumberProperty<Integer> fov = new NumberProperty<Integer>(Integer.valueOf(60), 30, 360, "Fov", "f");
    private final NumberProperty<Integer> livingTicks = new NumberProperty<Integer>(Integer.valueOf(1), 0, 100, "Living-Ticks", "livingticks", "ticks", "lt");
    private final EnumProperty<Targeting> targeting = new EnumProperty<Targeting>(Targeting.SWITCH, "Targeting", "target", "t");
    private final EnumProperty<EntityHelper.Location> bone = new EnumProperty<EntityHelper.Location>(EntityHelper.Location.HEAD, "Bone", "b");
    private final List<Entity> validTargets = new CopyOnWriteArrayList<Entity>();
    private Entity target = null;
    private final Stopwatch stopwatch = new Stopwatch();
    public static boolean shouldCrit = true;
    private int switchTime;
    private int attacks;

    public KillAura() {
        super("KillAura", new String[]{"killaura", "aura", "ka"}, -4240565, ModuleType.COMBAT);
        this.offerProperties(this.targeting, this.fov, this.autoBlock, this.direction, this.players, this.bone, this.aac, this.rayTrace, this.animals, this.monsters, this.invisibles, this.aps, this.silent, this.reach, this.livingTicks, this.team, this.dura, this.armor);
        this.offsetPresets(new Preset("Legit"){

            @Override
            public void onSet() {
                KillAura.this.aps.setValue(9L);
                KillAura.this.reach.setValue(Float.valueOf(3.8f));
                KillAura.this.livingTicks.setValue(0);
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
                KillAura.this.fov.setValue(360);
                KillAura.this.reach.setValue(Float.valueOf(4.0f));
                KillAura.this.livingTicks.setValue(0);
                KillAura.this.targeting.setValue(Targeting.TICK);
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
                        Optional<Entity> entity;
                        if (KillAura.this.validTargets.isEmpty()) {
                            for (Object object : ((KillAura)KillAura.this).minecraft.theWorld.loadedEntityList) {
                                EntityLivingBase entityLivingBase;
                                Entity entity3 = (Entity)object;
                                if (!(entity3 instanceof EntityLivingBase) || !KillAura.this.isValidTarget(entityLivingBase = (EntityLivingBase)entity3) || KillAura.this.validTargets.contains(entityLivingBase)) continue;
                                KillAura.this.validTargets.add(entityLivingBase);
                            }
                        } else {
                            KillAura.this.validTargets.forEach(target -> {
                                if (!KillAura.this.isValidTarget(target)) {
                                    KillAura.this.validTargets.remove(target);
                                }
                            });
                        }
                        if (KillAura.this.validTargets.isEmpty()) {
                            return;
                        }
                        if (KillAura.this.target == null && (entity = KillAura.this.validTargets.stream().filter(ent -> PlayerHelper.isAiming(EntityHelper.getRotations(ent)[0], EntityHelper.getRotations(ent)[1], (Integer)KillAura.this.fov.getValue())).filter(ent -> KillAura.this.isValidTarget(ent)).sorted((entity1, entity2) -> {
                            float entity2FOV;
                            float entityFOV = PlayerHelper.getFOV(EntityHelper.getRotations(entity1));
                            return entityFOV > (entity2FOV = PlayerHelper.getFOV(EntityHelper.getRotations(entity2))) ? 1 : (entityFOV < entity2FOV ? -1 : 0);
                        }).findFirst()).isPresent()) {
                            KillAura.this.target = entity.get();
                        }
                        if (((Boolean)KillAura.this.direction.getValue()).booleanValue() && ((Boolean)KillAura.this.silent.getValue()).booleanValue()) {
                            event.setRotationPitch(90.0f);
                        }
                        shouldCrit = (Boolean)KillAura.this.dura.getValue() != false;
                        if (((Boolean)KillAura.this.autoBlock.getValue()).booleanValue() && ((KillAura)KillAura.this).minecraft.thePlayer.inventory.getCurrentItem() != null && ((KillAura)KillAura.this).minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                            ((KillAura)KillAura.this).minecraft.thePlayer.setItemInUse(((KillAura)KillAura.this).minecraft.thePlayer.inventory.getCurrentItem(), ((KillAura)KillAura.this).minecraft.thePlayer.inventory.getCurrentItem().getMaxItemUseDuration());
                        }
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
                        if (autoHeal != null && autoHeal.isRunning() && (mode.getValue() == AutoHeal.Mode.POTION && autoHeal.isPotting() || mode.getValue() == AutoHeal.Mode.SOUP && autoHeal.isSouping())) {
                            return;
                        }
                        switch ((Targeting)((Object)KillAura.this.targeting.getValue())) {
                            case SINGLE: {
                                if (!KillAura.this.isValidTarget(KillAura.this.target) || !KillAura.this.stopwatch.hasCompleted(1000L / (Long)KillAura.this.aps.getValue())) break;
                                KillAura.this.attack(KillAura.this.target, false, false);
                                KillAura.this.target = null;
                                break;
                            }
                            case TICK: {
                                if (!KillAura.this.isValidTarget(KillAura.this.target) || !KillAura.this.stopwatch.hasCompleted(KillAura.this.switchTime())) break;
                                if (((Boolean)KillAura.this.dura.getValue()).booleanValue()) {
                                    KillAura.this.swap(9, ((KillAura)KillAura.this).minecraft.thePlayer.inventory.currentItem);
                                    KillAura.this.attack(KillAura.this.target, true, true);
                                    KillAura.this.swap(9, ((KillAura)KillAura.this).minecraft.thePlayer.inventory.currentItem);
                                    KillAura.this.attack(KillAura.this.target, true, true);
                                    KillAura.this.attacks++;
                                    if (KillAura.this.attacks < 1) break;
                                    KillAura.this.attacks = 0;
                                    KillAura.this.validTargets.remove(KillAura.this.target);
                                    KillAura.this.target = null;
                                    KillAura.this.stopwatch.reset();
                                    break;
                                }
                                KillAura.this.attack(KillAura.this.target, false, false);
                                KillAura.this.attack(KillAura.this.target, false, false);
                                KillAura.this.validTargets.remove(KillAura.this.target);
                                KillAura.this.target = null;
                                break;
                            }
                            case SWITCH: {
                                if (!KillAura.this.isValidTarget(KillAura.this.target) || !KillAura.this.stopwatch.hasCompleted(1000L / (Long)KillAura.this.aps.getValue())) break;
                                KillAura.this.attack(KillAura.this.target, false, false);
                                KillAura.this.attack(KillAura.this.target, false, false);
                                if (KillAura.this.validTargets.size() > 1) {
                                    KillAura.this.validTargets.remove(KillAura.this.target);
                                    KillAura.this.target = null;
                                }
                                KillAura.this.validTargets.remove(KillAura.this.target);
                                KillAura.this.target = null;
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
        shouldCrit = false;
    }

    private void attack(Entity entity, boolean tick, boolean uncrit) {
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
        if (uncrit) {
            this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        }
        this.minecraft.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (tick) {
            this.minecraft.thePlayer.swingItem();
            Speed speed = (Speed)Exeter.getInstance().getModuleManager().getModuleByAlias("speed");
            EnumProperty speedMode = (EnumProperty)speed.getPropertyByAlias("Mode");
            if (speed != null && speed.isRunning() && speedMode.getValue() == Speed.Mode.YPORT) {
                return;
            }
            this.crit();
            this.minecraft.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        }
        if (wasBlocking) {
            this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.getCurrentEquippedItem()));
        }
        if (wasSprinting) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
        if (wasSneaking) {
            this.minecraft.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        this.stopwatch.reset();
    }

    private boolean isValidTarget(Entity entity) {
        EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        if (entity == null || this.rayTrace.getValue() == false && !this.minecraft.thePlayer.canEntityBeSeen(entity) || entity.isDead || !entity.isEntityAlive() || this.minecraft.thePlayer.getDistanceToEntity(entity) > ((Float)this.reach.getValue()).floatValue() || entity.ticksExisted < (Integer)this.livingTicks.getValue()) {
            return false;
        }
        if (this.team.getValue().booleanValue() && entityLivingBase.isOnSameTeam(this.minecraft.thePlayer)) {
            return false;
        }
        if (this.players.getValue().booleanValue() && entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (this.armor.getValue().booleanValue() && !this.hasArmor(entityPlayer)) {
                return false;
            }
            return !(entityPlayer.equals(this.minecraft.thePlayer) || this.aac.getValue() != false && !entityPlayer.onGround && (double)entityPlayer.fallDistance == 0.0 || this.invisibles.getValue() == false && entityPlayer.isInvisible() || entityPlayer.capabilities.isCreativeMode || Exeter.getInstance().getFriendManager().isFriend(entity.getName()));
        }
        return this.animals.getValue() != false && entity instanceof EntityAnimal || this.monsters.getValue() != false && entity instanceof EntityMob;
    }

    private long switchTime() {
        if (this.validTargets.size() == 1) {
            this.switchTime = 200;
        } else if (this.validTargets.size() > 1) {
            this.switchTime = this.dura.getValue() != false ? 500 : 250;
        }
        return this.switchTime;
    }

    protected void swap(int slot, int hotbarNum) {
        this.minecraft.playerController.windowClick(this.minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.minecraft.thePlayer);
    }

    private void crit() {
        if (this.minecraft.thePlayer.isCollidedVertically) {
            this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + (double)0.05f, this.minecraft.thePlayer.posZ, false));
            this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));
            this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + (double)0.012511f, this.minecraft.thePlayer.posZ, false));
            this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));
        }
    }

    private boolean hasArmor(EntityPlayer player) {
        ItemStack boots = player.inventory.armorInventory[0];
        ItemStack pants = player.inventory.armorInventory[1];
        ItemStack chest = player.inventory.armorInventory[2];
        ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }

    private static enum Targeting {
        SINGLE,
        SWITCH,
        TICK;

    }
}

