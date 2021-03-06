/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.command.impl.player;

import me.friendly.exeter.command.Argument;
import me.friendly.exeter.command.Command;

public final class StackSize
extends Command {
    public StackSize() {
        super(new String[]{"stacksize", "size", "ss"}, new Argument("size"));
    }

    @Override
    public String dispatch() {
        Integer size = Integer.parseInt(this.getArgument("size").getValue());
        if (!this.minecraft.thePlayer.capabilities.isCreativeMode) {
            return "Must be in creative mode.";
        }
        if (this.minecraft.thePlayer.inventory.getCurrentItem() == null) {
            return "Invalid item.";
        }
        this.minecraft.thePlayer.inventory.getCurrentItem().stackSize = size;
        return String.format("Set item stack size to &e%s&7.", size);
    }
}

