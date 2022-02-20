/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import me.friendly.exeter.properties.NumberProperty;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;

public final class Chest
extends ToggleableModule {
    private final NumberProperty<Long> delay = new NumberProperty<Long>(Long.valueOf(150L), 10L, 250L, "Delay", "D");
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.STEAL, "Mode", "m");
    private final Stopwatch stopwatch = new Stopwatch();

    public Chest() {
        super("Chest", new String[]{"chest", "steal", "drop", "cheststealer"}, -357023, ModuleType.MISCELLANEOUS);
        this.offerProperties(this.delay, this.mode);
        this.listeners.add(new Listener<MotionUpdateEvent>("chest_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                Chest.this.setTag(Chest.this.mode.getFixedValue());
                if (!(((Chest)Chest.this).minecraft.currentScreen instanceof GuiChest)) {
                    return;
                }
                GuiChest chest = (GuiChest)((Chest)Chest.this).minecraft.currentScreen;
                for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                    ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                    if (stack == null || !Chest.this.stopwatch.hasCompleted((Long)Chest.this.delay.getValue())) continue;
                    switch ((Mode)((Object)Chest.this.mode.getValue())) {
                        case STEAL: {
                            ((Chest)Chest.this).minecraft.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, ((Chest)Chest.this).minecraft.thePlayer);
                            break;
                        }
                        case DROP: {
                            ((Chest)Chest.this).minecraft.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 4, ((Chest)Chest.this).minecraft.thePlayer);
                        }
                    }
                    Chest.this.stopwatch.reset();
                }
            }
        });
    }

    public static enum Mode {
        STEAL,
        DROP;

    }
}

