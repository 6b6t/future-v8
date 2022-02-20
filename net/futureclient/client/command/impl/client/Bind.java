/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.gitlab.nuf.exeter.command.impl.client;

import com.gitlab.nuf.api.interfaces.Toggleable;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import org.lwjgl.input.Keyboard;

public final class Bind
extends Command {
    public Bind() {
        super(new String[]{"bind"}, new Argument("module"), new Argument("key"));
    }

    @Override
    public String dispatch() {
        Module module = Exeter.getInstance().getModuleManager().getModuleByAlias(this.getArgument("module").getValue());
        int key = Keyboard.getKeyIndex((String)this.getArgument("key").getValue().toUpperCase());
        if (module == null) {
            return "No such module exists.";
        }
        if (!(module instanceof Toggleable)) {
            return "That module is not toggleable.";
        }
        ToggleableModule toggleableModule = (ToggleableModule)module;
        Exeter.getInstance().getKeybindManager().getKeybindByLabel(toggleableModule.getLabel()).setKey(key);
        return String.format("&e%s&7 has been bound to &e%s&7.", toggleableModule.getLabel(), Keyboard.getKeyName((int)key).toUpperCase());
    }
}

