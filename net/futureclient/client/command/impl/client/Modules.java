/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.client;

import com.gitlab.nuf.api.interfaces.Toggleable;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import java.util.List;
import java.util.StringJoiner;

public final class Modules
extends Command {
    public Modules() {
        super(new String[]{"modules", "mods", "ms", "ml", "lm"}, new Argument[0]);
    }

    @Override
    public String dispatch() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        List<Module> modules = Exeter.getInstance().getModuleManager().getRegistry();
        modules.sort((mod1, mod2) -> mod1.getLabel().compareTo(mod2.getLabel()));
        modules.forEach(module -> {
            if (module instanceof Toggleable) {
                ToggleableModule toggleableModule = (ToggleableModule)module;
                stringJoiner.add(String.format("%s%s&7", toggleableModule.isRunning() ? "&a" : "&c", toggleableModule.getLabel()));
            }
        });
        return String.format("Modules (%s) %s", Exeter.getInstance().getModuleManager().getRegistry().size(), stringJoiner.toString());
    }
}

