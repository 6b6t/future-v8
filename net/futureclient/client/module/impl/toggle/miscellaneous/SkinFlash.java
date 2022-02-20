//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\aesthetical\Documents\Minecraft\decomped\mappings\1.8.9"!

/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import java.util.Random;
import net.minecraft.entity.player.EnumPlayerModelParts;

public final class SkinFlash
extends ToggleableModule {
    private final Random random = new Random();

    public SkinFlash() {
        super("Skin Flash", new String[]{"skinflash", "sf", "flash"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<MotionUpdateEvent>("skin_flash_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
                if (parts != null) {
                    for (EnumPlayerModelParts part : parts) {
                        ((SkinFlash)SkinFlash.this).minecraft.gameSettings.setModelPartEnabled(part, SkinFlash.this.random.nextBoolean());
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (this.minecraft.thePlayer == null) {
            return;
        }
        EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
        if (parts != null) {
            for (EnumPlayerModelParts part : parts) {
                this.minecraft.gameSettings.setModelPartEnabled(part, true);
            }
        }
    }
}

