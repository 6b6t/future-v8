/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.player;

import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;

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

