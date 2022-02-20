/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.player;

import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;

public final class Damage
extends Command {
    public Damage() {
        super(new String[]{"damage", "dmg", "td"}, new Argument[0]);
    }

    @Override
    public String dispatch() {
        if (!PlayerHelper.isInLiquid()) {
            PlayerHelper.damagePlayer();
        } else {
            PlayerHelper.drownPlayer();
        }
        return "Damaged.";
    }
}

