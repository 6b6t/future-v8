/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.world;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.api.minecraft.helper.WorldHelper;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.BlockClickedEvent;
import com.gitlab.nuf.exeter.events.MiningSpeedEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.combat.KillAura;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public final class Speedmine
extends ToggleableModule {
    private final NumberProperty<Float> speed = new NumberProperty<Float>(Float.valueOf(1.1f), Float.valueOf(0.1f), Float.valueOf(10.0f), "Speed", "s");
    private final Property<Boolean> fastfall = new Property<Boolean>(true, "Fastfall", "ff");
    private final Property<Boolean> autoTool = new Property<Boolean>(true, "AutoTool", "at", "tool");

    public Speedmine() {
        super("Speedmine", new String[]{"speedmine", "speedygonzales", "sg", "sm", "fastbreak"}, -3373970, ModuleType.WORLD);
        this.offerProperties(this.speed, this.fastfall, this.autoTool);
        this.listeners.add(new Listener<MiningSpeedEvent>("speedy_gonzales_mining_speed_listener"){

            @Override
            public void call(MiningSpeedEvent event) {
                KillAura killAura = (KillAura)Exeter.getInstance().getModuleManager().getModuleByAlias("killaura");
                if (killAura != null && killAura.isRunning()) {
                    return;
                }
                event.setCanceled(true);
                event.setSpeed(((Float)Speedmine.this.speed.getValue()).floatValue());
            }
        });
        this.listeners.add(new Listener<BlockClickedEvent>("speedy_gonzales_block_clicked_listener"){

            @Override
            public void call(BlockClickedEvent event) {
                KillAura killAura = (KillAura)Exeter.getInstance().getModuleManager().getModuleByAlias("killaura");
                if (killAura != null && killAura.isRunning()) {
                    return;
                }
                if (((Boolean)Speedmine.this.fastfall.getValue()).booleanValue() && PlayerHelper.getBlockBelowPlayer(1.0).equals(WorldHelper.getBlock(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ())) && ((Speedmine)Speedmine.this).minecraft.thePlayer.onGround) {
                    ((Speedmine)Speedmine.this).minecraft.thePlayer.motionY -= 1.0;
                }
                if (((Boolean)Speedmine.this.autoTool.getValue()).booleanValue()) {
                    int slot = Speedmine.this.getBestTool(WorldHelper.getBlock(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    if (slot == -1) {
                        return;
                    }
                    if (slot < 9) {
                        ((Speedmine)Speedmine.this).minecraft.thePlayer.inventory.currentItem = slot;
                        ((Speedmine)Speedmine.this).minecraft.playerController.syncCurrentPlayItem();
                    } else {
                        ((Speedmine)Speedmine.this).minecraft.playerController.windowClick(0, slot, ((Speedmine)Speedmine.this).minecraft.thePlayer.inventory.currentItem, 2, ((Speedmine)Speedmine.this).minecraft.thePlayer);
                    }
                }
            }
        });
    }

    private int getBestTool(Block block) {
        int maxStrSlot = -1;
        float hardness = 1.0f;
        for (int index = 44; index >= 9; --index) {
            ItemStack stack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) continue;
            float strength = stack.getStrVsBlock(block);
            if (strength > 1.0f) {
                int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.EFFICIENCY.effectId, stack);
                strength += (float)(efficiencyLevel * efficiencyLevel + 1);
            }
            if (!(strength > hardness) || !(strength > 1.0f)) continue;
            hardness = strength;
            maxStrSlot = index;
        }
        return maxStrSlot;
    }
}

