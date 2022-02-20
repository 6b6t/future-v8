/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import me.friendly.api.event.Listener;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.events.TickEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.NumberProperty;
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
        super("AutoArmor", new String[]{"autoarmor", "aa", "armor"}, -5385072, ModuleType.COMBAT);
        this.offerProperties(this.delay);
        this.listeners.add(new Listener<TickEvent>("auto_armor_tick_listener"){

            @Override
            public void call(TickEvent event) {
                if (!AutoArmor.this.stopwatch.hasCompleted((Long)AutoArmor.this.delay.getValue()) || ((AutoArmor)AutoArmor.this).minecraft.thePlayer.capabilities.isCreativeMode || ((AutoArmor)AutoArmor.this).minecraft.currentScreen != null && !(((AutoArmor)AutoArmor.this).minecraft.currentScreen instanceof GuiChat)) {
                    return;
                }
                for (byte b = 5; b <= 8; b = (byte)(b + 1)) {
                    if (!AutoArmor.this.equipArmor(b)) continue;
                    AutoArmor.this.stopwatch.reset();
                    break;
                }
            }
        });
    }

    private boolean equipArmor(byte b) {
        int currentProtection = -1;
        int slot = -1;
        ItemArmor current = null;
        if (this.minecraft.thePlayer.inventoryContainer.getSlot(b).getStack() != null && this.minecraft.thePlayer.inventoryContainer.getSlot(b).getStack().getItem() instanceof ItemArmor) {
            current = (ItemArmor)this.minecraft.thePlayer.inventoryContainer.getSlot(b).getStack().getItem();
            currentProtection = current.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.effectId, this.minecraft.thePlayer.inventoryContainer.getSlot(b).getStack());
        }
        for (int i = 9; i <= 44; i = (int)((byte)(i + 1))) {
            ItemStack stack = this.minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null || !(stack.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorProtection = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.effectId, stack);
            if (!this.checkArmor(armor, b) || current != null && currentProtection >= armorProtection) continue;
            currentProtection = armorProtection;
            current = armor;
            slot = i;
        }
        if (slot != -1) {
            boolean isNull;
            boolean bl = isNull = this.minecraft.thePlayer.inventoryContainer.getSlot(b).getStack() == null;
            if (!isNull) {
                this.clickSlot(b, 0, false);
            }
            this.clickSlot(slot, 0, true);
            if (!isNull) {
                this.clickSlot(slot, 0, false);
            }
            return true;
        }
        return false;
    }

    private boolean checkArmor(ItemArmor item, byte b) {
        return b == 5 && item.getUnlocalizedName().startsWith("item.helmet") || b == 6 && item.getUnlocalizedName().startsWith("item.chestplate") || b == 7 && item.getUnlocalizedName().startsWith("item.leggings") || b == 8 && item.getUnlocalizedName().startsWith("item.boots");
    }

    private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
        this.minecraft.playerController.windowClick(this.minecraft.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, this.minecraft.thePlayer);
    }
}

