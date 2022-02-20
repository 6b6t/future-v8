/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.client;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;

public final class Prefix
extends Command {
    public Prefix() {
        super(new String[]{"prefix"}, new Argument("character"));
    }

    @Override
    public String dispatch() {
        String prefix = this.getArgument("character").getValue();
        Exeter.getInstance().getCommandManager().setPrefix(prefix);
        return String.format("&e%s&7 is now your prefix.", prefix);
    }
}

