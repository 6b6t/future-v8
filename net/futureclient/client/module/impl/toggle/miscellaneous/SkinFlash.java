/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import java.util.Random;
import me.friendly.api.event.Listener;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.entity.player.EnumPlayerModelParts;

public final class SkinFlash
extends ToggleableModule {
    private final Random random = new Random();

    public SkinFlash() {
        super("SkinFlash", new String[]{"skinflash", "sf", "flash"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<MotionUpdateEvent>("skin_flash_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
                if (parts != null) {
                    for (EnumPlayerModelParts part : parts) {
                        ((SkinFlash)SkinFlash.this).minecraft.gameSettings.func_178878_a(part, SkinFlash.this.random.nextBoolean());
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
                this.minecraft.gameSettings.func_178878_a(part, true);
            }
        }
    }
}

