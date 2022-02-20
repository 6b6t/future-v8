/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.player;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;

public final class VClip
extends Command {
    public VClip() {
        super(new String[]{"vclip", "vc", "v"}, new Argument("blocks"));
    }

    @Override
    public String dispatch() {
        double blocks = Double.parseDouble(this.getArgument("blocks").getValue());
        this.minecraft.thePlayer.setBoundingBox(this.minecraft.thePlayer.getEntityBoundingBox().offset(0.0, blocks, 0.0));
        return String.format("Teleported %s &e%s&7 block(s).", blocks < 0.0 ? "down" : "up", blocks);
    }
}

