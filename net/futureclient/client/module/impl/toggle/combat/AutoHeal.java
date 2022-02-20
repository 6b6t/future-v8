/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.combat;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class AutoHeal
extends ToggleableModule {
    private final NumberProperty<Float> health = new NumberProperty<Float>(Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(19.0f), "Health", "h", "<3");
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(230L), 1L, 1000L, "Delay", "d");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.POTION, "Mode", "m");
    private boolean potting = false;
    private final Stopwatch stopwatch = new Stopwatch();

    public AutoHeal() {
        super("Auto Heal", new String[]{"autoheal", "autosoup", "autopot", "autopotion", "as", "ap", "heal", "potion", "pot", "soup"}, -14955381, ModuleType.COMBAT);
        this.offerProperties(this.health, this.delay, this.mode);
        this.listeners.add(new Listener<MotionUpdateEvent>("auto_heal_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                AutoHeal.this.setTag(String.format("Auto %s \u00a77%s", AutoHeal.this.mode.getFixedValue(), AutoHeal.this.countItems()));
                switch (event.getTime()) {
                    case BEFORE: {
                        if (!(((AutoHeal)AutoHeal.this).minecraft.thePlayer.getHealth() <= ((Float)AutoHeal.this.health.getValue()).floatValue())) break;
                        int currentItem = ((AutoHeal)AutoHeal.this).minecraft.thePlayer.inventory.currentItem;
                        switch ((Mode)((Object)AutoHeal.this.mode.getValue())) {
                            case POTION: {
                                AutoHeal.this.potting = true;
                                if (AutoHeal.this.hotbarHasItems()) {
                                    event.setRotationPitch(95.0f);
                                    AutoHeal.this.useItem();
                                } else {
                                    AutoHeal.this.getItemsFromInventory();
                                }
                                AutoHeal.this.potting = false;
                                break;
                            }
                            case APPLE: 
                            case SOUP: {
                                if (AutoHeal.this.hotbarHasItems()) {
                                    AutoHeal.this.useItem();
                                    break;
                                }
                                AutoHeal.this.getItemsFromInventory();
                            }
                        }
                        ((AutoHeal)AutoHeal.this).minecraft.thePlayer.inventory.currentItem = currentItem;
                    }
                }
            }
        });
    }

    private int countItems() {
        int items = 0;
        block5: for (int index = 9; index < 45; ++index) {
            ItemStack itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block5;
                    items += itemStack.stackSize;
                    continue block5;
                }
                case APPLE: {
                    if (!(itemStack.getItem() instanceof ItemAppleGold)) continue block5;
                    items += itemStack.stackSize;
                    continue block5;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block5;
                    items += itemStack.stackSize;
                }
            }
        }
        return items;
    }

    private void getItemsFromInventory() {
        ItemStack itemStack;
        int index;
        int item = -1;
        boolean found = false;
        boolean splash = false;
        block9: for (index = 36; index >= 9; --index) {
            itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block9;
                    item = index;
                    found = true;
                    splash = ItemPotion.isSplash(itemStack.getItemDamage());
                    continue block9;
                }
                case APPLE: {
                    if (!(itemStack.getItem() instanceof ItemAppleGold)) continue block9;
                    item = index;
                    found = true;
                    continue block9;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block9;
                    item = index;
                    found = true;
                }
            }
        }
        if (found) {
            if (!splash) {
                block10: for (index = 0; index < 45; ++index) {
                    itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
                    if (itemStack == null) continue;
                    switch ((Mode)((Object)this.mode.getValue())) {
                        case POTION: {
                            if (itemStack.getItem() != Items.glass_bottle || index < 36 || index > 44) continue block10;
                            this.minecraft.playerController.windowClick(0, index, 0, 0, this.minecraft.thePlayer);
                            this.minecraft.playerController.windowClick(0, -999, 0, 0, this.minecraft.thePlayer);
                            continue block10;
                        }
                        case SOUP: {
                            if (itemStack.getItem() != Items.bowl || index < 36 || index > 44) continue block10;
                            this.minecraft.playerController.windowClick(0, index, 0, 0, this.minecraft.thePlayer);
                            this.minecraft.playerController.windowClick(0, -999, 0, 0, this.minecraft.thePlayer);
                        }
                    }
                }
            }
            this.minecraft.playerController.windowClick(0, item, 0, 1, this.minecraft.thePlayer);
        }
    }

    private boolean hotbarHasItems() {
        boolean found = false;
        block5: for (int index = 36; index < 45; ++index) {
            ItemStack itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block5;
                    found = true;
                    continue block5;
                }
                case APPLE: {
                    if (!(itemStack.getItem() instanceof ItemAppleGold)) continue block5;
                    found = true;
                    continue block5;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block5;
                    found = true;
                }
            }
        }
        return found;
    }

    private void useItem() {
        ItemStack itemStack;
        int index;
        int item = -1;
        boolean found = false;
        boolean splash = false;
        block10: for (index = 36; index < 45; ++index) {
            itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block10;
                    item = index;
                    found = true;
                    splash = ItemPotion.isSplash(itemStack.getItemDamage());
                    continue block10;
                }
                case APPLE: {
                    if (!(itemStack.getItem() instanceof ItemAppleGold)) continue block10;
                    item = index;
                    found = true;
                    continue block10;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block10;
                    item = index;
                    found = true;
                }
            }
        }
        if (found) {
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (splash) {
                        if (!this.stopwatch.hasCompleted((Long)this.delay.getValue())) break;
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.moveForward == 0.0f ? (this.minecraft.thePlayer.onGround ? 90.0f : -90.0f) : 45.0f, this.minecraft.thePlayer.onGround));
                        this.minecraft.thePlayer.inventory.currentItem = item - 36;
                        this.minecraft.playerController.updateController();
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.inventory.getCurrentItem()));
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.rotationPitch, this.minecraft.thePlayer.onGround));
                        this.stopwatch.reset();
                        break;
                    }
                    if (!this.minecraft.thePlayer.onGround) break;
                    for (index = 0; index < 45; ++index) {
                        itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
                        if (itemStack == null || itemStack.getItem() != Items.glass_bottle || index < 36 || index > 44) continue;
                        this.minecraft.playerController.windowClick(0, index, 0, 0, this.minecraft.thePlayer);
                        this.minecraft.playerController.windowClick(0, -999, 0, 0, this.minecraft.thePlayer);
                    }
                    this.minecraft.thePlayer.inventory.currentItem = item - 36;
                    this.minecraft.playerController.updateController();
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.inventory.getCurrentItem()));
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.minecraft.thePlayer.inventory.currentItem));
                    for (index = 0; index < 32; ++index) {
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(this.minecraft.thePlayer.onGround));
                    }
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.minecraft.thePlayer.stopUsingItem();
                    break;
                }
                case APPLE: {
                    if (!this.minecraft.thePlayer.onGround) break;
                    this.minecraft.thePlayer.inventory.currentItem = item - 36;
                    this.minecraft.playerController.updateController();
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.inventory.getCurrentItem()));
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.minecraft.thePlayer.inventory.currentItem));
                    for (index = 0; index < 32; ++index) {
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(this.minecraft.thePlayer.onGround));
                    }
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.minecraft.thePlayer.stopUsingItem();
                    break;
                }
                case SOUP: {
                    for (index = 0; index < 45; ++index) {
                        itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
                        if (itemStack == null || itemStack.getItem() != Items.bowl || index < 36 || index > 44) continue;
                        this.minecraft.playerController.windowClick(0, index, 0, 0, this.minecraft.thePlayer);
                        this.minecraft.playerController.windowClick(0, -999, 0, 0, this.minecraft.thePlayer);
                    }
                    this.minecraft.thePlayer.inventory.currentItem = item - 36;
                    this.minecraft.playerController.updateController();
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.inventory.getCurrentItem()));
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.minecraft.thePlayer.inventory.currentItem));
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.minecraft.thePlayer.stopUsingItem();
                }
            }
        }
    }

    private boolean isItemHealthPotion(ItemStack itemStack) {
        ItemPotion potion;
        if (itemStack.getItem() instanceof ItemPotion && (potion = (ItemPotion)itemStack.getItem()).hasEffect(itemStack)) {
            for (Object o : potion.getEffects(itemStack)) {
                PotionEffect effect = (PotionEffect)o;
                if (!effect.getEffectName().equals("potion.heal")) continue;
                return true;
            }
        }
        return false;
    }

    boolean isPotting() {
        return this.potting;
    }

    public static enum Mode {
        POTION,
        SOUP,
        APPLE;

    }
}

