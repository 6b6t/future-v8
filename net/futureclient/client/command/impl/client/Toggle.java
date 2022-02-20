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

public final class Toggle
extends Command {
    public Toggle() {
        super(new String[]{"toggle", "t"}, new Argument("module"));
    }

    @Override
    public String dispatch() {
        Module module = Exeter.getInstance().getModuleManager().getModuleByAlias(this.getArgument("module").getValue());
        if (module == null) {
            return "No such module exists.";
        }
        if (!(module instanceof Toggleable)) {
            return "That module is not toggleable.";
        }
        ToggleableModule toggleableModule = (ToggleableModule)module;
        toggleableModule.toggle();
        return String.format("&e%s&7 has been %s&7.", toggleableModule.getLabel(), toggleableModule.isRunning() ? "&aenabled" : "&cdisabled");
    }
}

