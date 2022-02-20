/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.keybind;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.registry.ListRegistry;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.InputEvent;
import com.gitlab.nuf.exeter.keybind.Keybind;
import java.util.ArrayList;

public final class KeybindManager
extends ListRegistry<Keybind> {
    public KeybindManager() {
        this.registry = new ArrayList();
        Exeter.getInstance().getEventManager().register(new Listener<InputEvent>("keybinds_input_listener"){

            @Override
            public void call(InputEvent event) {
                if (event.getType() == InputEvent.Type.KEYBOARD_KEY_PRESS) {
                    KeybindManager.this.registry.forEach(keybind -> {
                        if (keybind.getKey() != 0 && keybind.getKey() == event.getKey()) {
                            keybind.onPressed();
                        }
                    });
                }
            }
        });
    }

    public Keybind getKeybindByLabel(String label) {
        for (Keybind keybind : this.registry) {
            if (!label.equalsIgnoreCase(keybind.getLabel())) continue;
            return keybind;
        }
        return null;
    }
}

