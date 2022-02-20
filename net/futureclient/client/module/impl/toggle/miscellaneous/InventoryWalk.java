/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.module.impl.toggle.render.clickgui.ClickGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public final class InventoryWalk
extends ToggleableModule {
    public InventoryWalk() {
        super("Inventory Walk", new String[]{"inventorywalk", "iw"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<MotionUpdateEvent>("inventory_walk_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getTime()) {
                    case BEFORE: {
                        KeyBinding[] keys = new KeyBinding[]{((InventoryWalk)InventoryWalk.this).minecraft.gameSettings.keyBindForward, ((InventoryWalk)InventoryWalk.this).minecraft.gameSettings.keyBindBack, ((InventoryWalk)InventoryWalk.this).minecraft.gameSettings.keyBindLeft, ((InventoryWalk)InventoryWalk.this).minecraft.gameSettings.keyBindRight, ((InventoryWalk)InventoryWalk.this).minecraft.gameSettings.keyBindJump};
                        if (((InventoryWalk)InventoryWalk.this).minecraft.currentScreen instanceof GuiContainer || ((InventoryWalk)InventoryWalk.this).minecraft.currentScreen instanceof ClickGui) {
                            for (KeyBinding bind : keys) {
                                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown((int)bind.getKeyCode()));
                            }
                            event.setLockview(true);
                            if (Keyboard.isKeyDown((int)200)) {
                                ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch -= 4.0f;
                            }
                            if (Keyboard.isKeyDown((int)208)) {
                                ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch += 4.0f;
                            }
                            if (Keyboard.isKeyDown((int)203)) {
                                ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationYaw -= 5.0f;
                            }
                            if (Keyboard.isKeyDown((int)205)) {
                                ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationYaw += 5.0f;
                            }
                            if (((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch > 90.0f) {
                                ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch = 90.0f;
                            }
                            if (!(((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch < -90.0f)) break;
                            ((InventoryWalk)InventoryWalk.this).minecraft.thePlayer.rotationPitch = -90.0f;
                            break;
                        }
                        if (((InventoryWalk)InventoryWalk.this).minecraft.currentScreen != null) break;
                        for (KeyBinding bind : keys) {
                            if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                            KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                        }
                        break;
                    }
                }
            }
        });
        this.setRunning(true);
    }
}

