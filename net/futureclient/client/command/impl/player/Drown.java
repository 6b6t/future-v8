/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.command.impl.player;

import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.command.Argument;
import me.friendly.exeter.command.Command;

public final class Drown
extends Command {
    public Drown() {
        super(new String[]{"drown"}, new Argument[0]);
    }

    @Override
    public String dispatch() {
        PlayerHelper.drownPlayer();
        return "Drowning...";
    }
}

