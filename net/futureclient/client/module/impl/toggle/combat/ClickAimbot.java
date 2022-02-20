/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.EntityHelper;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.InputEvent;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public final class ClickAimbot
extends ToggleableModule {
    private final NumberProperty<Float> reach = new NumberProperty<Float>(Float.valueOf(3.9f), Float.valueOf(3.0f), Float.valueOf(5.0f), "Reach", "range", "r");
    private final NumberProperty<Integer> fov = new NumberProperty<Integer>(Integer.valueOf(60), 30, 360, "Fov", "view");
    private final EnumProperty<InputEvent.Type> button = new EnumProperty<InputEvent.Type>(InputEvent.Type.MOUSE_LEFT_CLICK, "Button", "b");
    private final Property<Boolean> rayTrace = new Property<Boolean>(false, "Ray-Trace", "raytrace", "rt", "trace", "ray");
    private final Property<Boolean> players = new Property<Boolean>(true, "Players", "player", "p", "play");
    private final Property<Boolean> animals = new Property<Boolean>(false, "Animals", "ani", "a", "animal");
    private final Property<Boolean> monsters = new Property<Boolean>(false, "Monsters", "monster", "m", "mon");
    private final Property<Boolean> invisibles = new Property<Boolean>(false, "Invisibles", "invisible", "invis", "i", "inv");
    private boolean attackedTarget = false;
    private Entity target = null;

    public ClickAimbot() {
        super("ClickAimbot", new String[]{"clickaimbot", "ca", "clickaim"}, -688231, ModuleType.COMBAT);
        this.offerProperties(this.reach, this.rayTrace, this.fov, this.button, this.players, this.monsters, this.animals, this.invisibles);
        this.listeners.add(new Listener<InputEvent>("click_aimbot_input_listener"){

            @Override
            public void call(InputEvent event) {
                if (event.getType() == ClickAimbot.this.button.getValue() && ClickAimbot.this.attackedTarget) {
                    ClickAimbot.this.attackedTarget = false;
                }
            }
        });
        this.listeners.add(new Listener<MotionUpdateEvent>("click_aimbot_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getTime()) {
                    case BEFORE: {
                        if (!ClickAimbot.this.isValidEntity(ClickAimbot.this.target)) {
                            ClickAimbot.this.target = null;
                        }
                        if (ClickAimbot.this.target == null) {
                            ClickAimbot.this.target = ClickAimbot.this.getClosestEntity();
                        }
                        if (ClickAimbot.this.isValidEntity(ClickAimbot.this.target)) {
                            float[] rotations = EntityHelper.getRotations(ClickAimbot.this.target);
                            event.setRotationYaw(rotations[0]);
                            event.setRotationPitch(rotations[1]);
                            break;
                        }
                        ClickAimbot.this.target = null;
                        break;
                    }
                    case AFTER: {
                        if (ClickAimbot.this.attackedTarget) break;
                        if (ClickAimbot.this.isValidEntity(ClickAimbot.this.target)) {
                            ((ClickAimbot)ClickAimbot.this).minecraft.thePlayer.swingItem();
                            ((ClickAimbot)ClickAimbot.this).minecraft.playerController.attackEntity(((ClickAimbot)ClickAimbot.this).minecraft.thePlayer, ClickAimbot.this.target);
                        }
                        ClickAimbot.this.target = null;
                        ClickAimbot.this.attackedTarget = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.attackedTarget = false;
        this.target = null;
    }

    private boolean isValidEntity(Entity entity) {
        if (entity == null || entity.equals(this.minecraft.thePlayer) || this.minecraft.thePlayer.getDistanceToEntity(entity) > ((Float)this.reach.getValue()).floatValue() || !PlayerHelper.isAiming(EntityHelper.getRotations(entity)[0], EntityHelper.getRotations(entity)[1], (Integer)this.fov.getValue())) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            if (entity.isInvisible() && !this.invisibles.getValue().booleanValue()) {
                return false;
            }
            if (!this.minecraft.thePlayer.canEntityBeSeen(entity) && !this.rayTrace.getValue().booleanValue()) {
                return false;
            }
            return this.players.getValue() != false && !Exeter.getInstance().getFriendManager().isFriend(entity.getName());
        }
        return entity instanceof EntityMob && this.monsters.getValue() != false || entity instanceof EntityAnimal && this.animals.getValue() != false;
    }

    private Entity getClosestEntity() {
        double range = ((Float)this.reach.getValue()).floatValue();
        Entity closest = null;
        for (Object object : this.minecraft.theWorld.loadedEntityList) {
            float distance;
            Entity entity = (Entity)object;
            if (!(entity instanceof EntityLivingBase) || !((double)(distance = this.minecraft.thePlayer.getDistanceToEntity(entity)) < range) || !this.isValidEntity(entity)) continue;
            closest = entity;
            range = distance;
        }
        return closest;
    }
}

