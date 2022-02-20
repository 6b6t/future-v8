/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.combat;

import me.friendly.api.event.Listener;
import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import me.friendly.exeter.properties.Property;
import net.minecraft.init.Items;
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
    private final NumberProperty<Float> health = new NumberProperty<Float>(Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(20.0f), "Health", "h", "<3");
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(350L), 0L, 1000L, "Delay", "d");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.POTION, "Mode", "m");
    private final Property<Boolean> jump = new Property<Boolean>(false, "Jump", "j");
    private boolean potting = false;
    private boolean souping = false;
    private final Stopwatch stopwatch = new Stopwatch();
    private int lockedTicks = -1;
    private double x;
    private double y;
    private double z;

    public AutoHeal() {
        super("AutoHeal", new String[]{"autoheal", "autosoup", "autopot", "autopotion", "as", "ap", "heal", "potion", "pot", "soup"}, -14955381, ModuleType.COMBAT);
        this.offerProperties(this.health, this.delay, this.mode, this.jump);
        this.listeners.add(new Listener<MotionUpdateEvent>("auto_heal_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                AutoHeal.this.setTag(String.format("Auto%s \u00a77%s", AutoHeal.this.mode.getFixedValue(), AutoHeal.this.countItems()));
                switch (event.getTime()) {
                    case BEFORE: {
                        if (((AutoHeal)AutoHeal.this).minecraft.thePlayer.getHealth() <= ((Float)AutoHeal.this.health.getValue()).floatValue()) {
                            int currentItem = ((AutoHeal)AutoHeal.this).minecraft.thePlayer.inventory.currentItem;
                            switch ((Mode)((Object)AutoHeal.this.mode.getValue())) {
                                case POTION: {
                                    if (AutoHeal.this.hotbarHasItems() && !PlayerHelper.isInLiquid() && !PlayerHelper.isOnLiquid() && AutoHeal.this.stopwatch.hasCompleted((Long)AutoHeal.this.delay.getValue())) {
                                        AutoHeal.this.potting = true;
                                        if (((Boolean)AutoHeal.this.jump.getValue()).booleanValue() && ((AutoHeal)AutoHeal.this).minecraft.thePlayer.isCollidedVertically) {
                                            event.setRotationPitch(-90.0f);
                                        } else {
                                            event.setRotationPitch(110.0f);
                                        }
                                        if (((Boolean)AutoHeal.this.jump.getValue()).booleanValue() && ((AutoHeal)AutoHeal.this).minecraft.thePlayer.isCollidedVertically) {
                                            AutoHeal.this.useItem();
                                            AutoHeal.this.jump();
                                            AutoHeal.this.x = ((AutoHeal)AutoHeal.this).minecraft.thePlayer.posX;
                                            AutoHeal.this.y = ((AutoHeal)AutoHeal.this).minecraft.thePlayer.posY + 1.24;
                                            AutoHeal.this.z = ((AutoHeal)AutoHeal.this).minecraft.thePlayer.posZ;
                                            System.out.println(AutoHeal.this.lockedTicks + "before");
                                            AutoHeal.this.lockedTicks = 5;
                                            AutoHeal.this.potting = false;
                                        }
                                    } else if (!AutoHeal.this.hotbarHasItems()) {
                                        AutoHeal.this.getItemsFromInventory();
                                    }
                                    if (AutoHeal.this.lockedTicks >= 0 && ((Boolean)AutoHeal.this.jump.getValue()).booleanValue()) {
                                        event.setCanceled(true);
                                    }
                                    if (AutoHeal.this.lockedTicks == 0 && ((Boolean)AutoHeal.this.jump.getValue()).booleanValue()) {
                                        ((AutoHeal)AutoHeal.this).minecraft.thePlayer.motionX = 0.0;
                                        ((AutoHeal)AutoHeal.this).minecraft.thePlayer.motionZ = 0.0;
                                        ((AutoHeal)AutoHeal.this).minecraft.thePlayer.setPositionAndUpdate(AutoHeal.this.x, AutoHeal.this.y, AutoHeal.this.z);
                                        ((AutoHeal)AutoHeal.this).minecraft.thePlayer.motionY = -0.08;
                                        AutoHeal.this.potting = false;
                                        System.out.println(AutoHeal.this.lockedTicks + "during");
                                    }
                                    AutoHeal.this.lockedTicks = AutoHeal.this.lockedTicks - 1;
                                    System.out.println(AutoHeal.this.lockedTicks + "after");
                                    break;
                                }
                                case SOUP: {
                                    if (AutoHeal.this.hotbarHasItems()) {
                                        AutoHeal.this.souping = true;
                                        AutoHeal.this.useItem();
                                        AutoHeal.this.souping = false;
                                        break;
                                    }
                                    AutoHeal.this.getItemsFromInventory();
                                    AutoHeal.this.souping = false;
                                }
                            }
                            ((AutoHeal)AutoHeal.this).minecraft.thePlayer.inventory.currentItem = currentItem;
                        }
                        AutoHeal.this.souping = false;
                        break;
                    }
                    case AFTER: {
                        if (!(((AutoHeal)AutoHeal.this).minecraft.thePlayer.getHealth() <= ((Float)AutoHeal.this.health.getValue()).floatValue())) break;
                        switch ((Mode)((Object)AutoHeal.this.mode.getValue())) {
                            case POTION: {
                                if (AutoHeal.this.hotbarHasItems() && AutoHeal.this.potting && !PlayerHelper.isInLiquid() && !PlayerHelper.isOnLiquid() && !((Boolean)AutoHeal.this.jump.getValue()).booleanValue()) {
                                    AutoHeal.this.useItem();
                                    AutoHeal.this.potting = false;
                                    break;
                                }
                                AutoHeal.this.potting = false;
                                AutoHeal.this.souping = false;
                            }
                        }
                        AutoHeal.this.potting = false;
                        AutoHeal.this.souping = false;
                    }
                }
            }
        });
    }

    private int countItems() {
        int items = 0;
        block4: for (int index = 9; index < 45; ++index) {
            ItemStack itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block4;
                    items += itemStack.stackSize;
                    continue block4;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block4;
                    items += itemStack.stackSize;
                }
            }
        }
        return items;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.lockedTicks = -1;
        this.potting = false;
    }

    private void jump() {
        this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.42, this.minecraft.thePlayer.posZ, true));
        this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.75, this.minecraft.thePlayer.posZ, true));
        this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 1.0, this.minecraft.thePlayer.posZ, true));
        this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 1.16, this.minecraft.thePlayer.posZ, true));
        this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 1.24, this.minecraft.thePlayer.posZ, true));
    }

    private void getItemsFromInventory() {
        ItemStack itemStack;
        int index;
        int item = -1;
        boolean found = false;
        boolean splash = false;
        block8: for (index = 36; index >= 9; --index) {
            itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block8;
                    item = index;
                    found = true;
                    splash = ItemPotion.isSplash(itemStack.getItemDamage());
                    continue block8;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block8;
                    item = index;
                    found = true;
                }
            }
        }
        if (found) {
            if (!splash) {
                block9: for (index = 0; index < 45; ++index) {
                    itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
                    if (itemStack == null) continue;
                    switch ((Mode)((Object)this.mode.getValue())) {
                        case POTION: {
                            if (itemStack.getItem() != Items.glass_bottle || index < 36 || index > 44) continue block9;
                            this.minecraft.playerController.windowClick(0, index, 0, 0, this.minecraft.thePlayer);
                            this.minecraft.playerController.windowClick(0, -999, 0, 0, this.minecraft.thePlayer);
                            continue block9;
                        }
                        case SOUP: {
                            if (itemStack.getItem() != Items.bowl || index < 36 || index > 44) continue block9;
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
        block4: for (int index = 36; index < 45; ++index) {
            ItemStack itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block4;
                    found = true;
                    continue block4;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block4;
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
        block8: for (index = 36; index < 45; ++index) {
            itemStack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null) continue;
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (!this.isItemHealthPotion(itemStack)) continue block8;
                    item = index;
                    found = true;
                    splash = ItemPotion.isSplash(itemStack.getItemDamage());
                    continue block8;
                }
                case SOUP: {
                    if (!(itemStack.getItem() instanceof ItemSoup)) continue block8;
                    item = index;
                    found = true;
                }
            }
        }
        if (found) {
            switch ((Mode)((Object)this.mode.getValue())) {
                case POTION: {
                    if (splash) {
                        if (this.jump.getValue().booleanValue()) {
                            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.moveForward == 0.0f ? (this.minecraft.thePlayer.onGround ? -90.0f : -90.0f) : -90.0f, this.minecraft.thePlayer.onGround));
                        } else {
                            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.moveForward == 0.0f ? (this.minecraft.thePlayer.onGround ? 110.0f : 95.0f) : 90.0f, this.minecraft.thePlayer.onGround));
                        }
                        int lastSlot = this.minecraft.thePlayer.inventory.currentItem;
                        this.minecraft.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(item - 36));
                        this.minecraft.playerController.updateController();
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.minecraft.thePlayer.inventory.getCurrentItem()));
                        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.minecraft.thePlayer.rotationYaw, this.minecraft.thePlayer.rotationPitch, this.minecraft.thePlayer.onGround));
                        this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(lastSlot));
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

    public boolean isPotting() {
        return this.potting;
    }

    boolean isSouping() {
        return this.souping;
    }

    public static enum Mode {
        POTION,
        SOUP;

    }
}

