/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.command.impl.player;

import me.friendly.api.minecraft.helper.PlayerHelper;
import me.friendly.exeter.command.Argument;
import me.friendly.exeter.command.Command;

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

