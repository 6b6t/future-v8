/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.client;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import java.util.StringJoiner;

public final class Help
extends Command {
    public Help() {
        super(new String[]{"help", "halp", "autism", "how"}, new Argument[0]);
    }

    @Override
    public String dispatch() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Exeter.getInstance().getCommandManager().getRegistry().forEach(command -> stringJoiner.add(command.getAliases()[0]));
        return String.format("Commands (%s) %s", Exeter.getInstance().getCommandManager().getRegistry().size(), stringJoiner.toString());
    }
}

