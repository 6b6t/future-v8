/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.events.TickEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class AutoArmor
extends ToggleableModule {
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(500L), "Delay", "D");
    private final Stopwatch stopwatch = new Stopwatch();

    public AutoArmor() {
        super("Auto Armor", new String[]{"autoarmor", "aa", "armor"}, -5385072, ModuleType.COMBAT);
        this.offerProperties(this.delay);
        this.listeners.add(new Listener<TickEvent>("auto_armor_tick_listener"){

            @Override
            public void call(TickEvent event) {
                if (!AutoArmor.this.stopwatch.hasCompleted((Long)AutoArmor.this.delay.getValue()) || ((AutoArmor)AutoArmor.this).minecraft.thePlayer.capabilities.isCreativeMode || ((AutoArmor)AutoArmor.this).minecraft.currentScreen != null && !(((AutoArmor)AutoArmor.this).minecraft.currentScreen instanceof GuiChat)) {
                    return;
                }
                for (byte b2 = 5; b2 <= 8; b2 = (byte)(b2 + 1)) {
                    if (!AutoArmor.this.equipArmor(b2)) continue;
                    AutoArmor.this.stopwatch.reset();
                    break;
                }
            }
        });
    }

    private boolean equipArmor(byte b2) {
        int currentProtection = -1;
        int slot = -1;
        ItemArmor current = null;
        if (this.minecraft.thePlayer.inventoryContainer.getSlot(b2).getStack() != null && this.minecraft.thePlayer.inventoryContainer.getSlot(b2).getStack().getItem() instanceof ItemArmor) {
            current = (ItemArmor)this.minecraft.thePlayer.inventoryContainer.getSlot(b2).getStack().getItem();
            currentProtection = current.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.effectId, this.minecraft.thePlayer.inventoryContainer.getSlot(b2).getStack());
        }
        for (int i2 = 9; i2 <= 44; i2 = (int)((byte)(i2 + 1))) {
            ItemStack stack = this.minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack();
            if (stack == null || !(stack.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorProtection = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.effectId, stack);
            if (!this.checkArmor(armor, b2) || current != null && currentProtection >= armorProtection) continue;
            currentProtection = armorProtection;
            current = armor;
            slot = i2;
        }
        if (slot != -1) {
            boolean isNull;
            boolean bl2 = isNull = this.minecraft.thePlayer.inventoryContainer.getSlot(b2).getStack() == null;
            if (!isNull) {
                this.clickSlot(b2, 0, false);
            }
            this.clickSlot(slot, 0, true);
            if (!isNull) {
                this.clickSlot(slot, 0, false);
            }
            return true;
        }
        return false;
    }

    private boolean checkArmor(ItemArmor item, byte b2) {
        return b2 == 5 && item.getUnlocalizedName().startsWith("item.helmet") || b2 == 6 && item.getUnlocalizedName().startsWith("item.chestplate") || b2 == 7 && item.getUnlocalizedName().startsWith("item.leggings") || b2 == 8 && item.getUnlocalizedName().startsWith("item.boots");
    }

    private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
        this.minecraft.playerController.windowClick(this.minecraft.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, this.minecraft.thePlayer);
    }
}

