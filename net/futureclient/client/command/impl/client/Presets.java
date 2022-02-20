/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.client;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.presets.Preset;
import java.util.StringJoiner;

public final class Presets
extends Command {
    public Presets() {
        super(new String[]{"preset", "presets"}, new Argument("module"), new Argument("preset"));
    }

    @Override
    public String dispatch() {
        Module module = Exeter.getInstance().getModuleManager().getModuleByAlias(this.getArgument("module").getValue());
        if (module == null) {
            return "No such module exists.";
        }
        if (module.getPresets().size() < 1) {
            return "That module has no presets.";
        }
        Preset preset = module.getPresetByLabel(this.getArgument("preset").getValue());
        if (preset == null) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (Preset prese : module.getPresets()) {
                stringJoiner.add(prese.getLabel());
            }
            return String.format("Try: %s.", stringJoiner.toString());
        }
        preset.onSet();
        return String.format("Loaded &e%s&7 preset for &e%s&7.", preset.getLabel(), module.getLabel());
    }
}

