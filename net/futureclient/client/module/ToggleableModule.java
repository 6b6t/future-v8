/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module;

import java.util.ArrayList;
import java.util.List;
import me.friendly.api.event.Listener;
import me.friendly.api.interfaces.Toggleable;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.keybind.Keybind;
import me.friendly.exeter.module.Module;
import me.friendly.exeter.module.ModuleType;

public class ToggleableModule
extends Module
implements Toggleable {
    private boolean running;
    private boolean drawn;
    private final int color;
    private final ModuleType moduleType;
    protected final List<Listener> listeners = new ArrayList<Listener>();

    private ToggleableModule(String label, String[] aliases, boolean drawn, int color, ModuleType moduleType) {
        super(label, aliases);
        this.drawn = drawn;
        this.color = color;
        this.moduleType = moduleType;
        Exeter.getInstance().getKeybindManager().register(new Keybind(label, 0){

            @Override
            public void onPressed() {
                ToggleableModule.this.toggle();
            }
        });
    }

    protected ToggleableModule(String label, String[] aliases, int color, ModuleType moduleType) {
        this(label, aliases, true, color, moduleType);
    }

    protected ToggleableModule(String label, String[] aliases, ModuleType moduleType) {
        this(label, aliases, false, 0, moduleType);
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if (this.isRunning()) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    @Override
    public void toggle() {
        this.setRunning(!this.running);
    }

    public boolean isDrawn() {
        return this.drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public int getColor() {
        return this.color;
    }

    public ModuleType getModuleType() {
        return this.moduleType;
    }

    protected void onEnable() {
        this.listeners.forEach(listener -> Exeter.getInstance().getEventManager().register((Listener)listener));
    }

    protected void onDisable() {
        this.listeners.forEach(listener -> Exeter.getInstance().getEventManager().unregister((Listener)listener));
    }
}

