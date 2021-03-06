/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.render;

import me.friendly.api.event.Listener;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.ShowMessageEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.EnumProperty;
import net.minecraft.util.EnumChatFormatting;

public final class NameProtect
extends ToggleableModule {
    private final EnumProperty<EnumChatFormatting> color = new EnumProperty<EnumChatFormatting>(EnumChatFormatting.DARK_AQUA, "Color", "c");

    public NameProtect() {
        super("NameProtect", new String[]{"nameprotect", "name", "protect", "np"}, ModuleType.RENDER);
        this.offerProperties(this.color);
        this.listeners.add(new Listener<ShowMessageEvent>("name_protect_show_message_listener"){

            @Override
            public void call(ShowMessageEvent event) {
                Exeter.getInstance().getFriendManager().getRegistry().forEach(friend -> {
                    if (event.getMessage().contains(friend.getLabel())) {
                        String message = event.getMessage().replace(friend.getLabel(), String.format("%s%s%s", new Object[]{NameProtect.this.color.getValue(), friend.getAlias(), EnumChatFormatting.RESET}));
                        event.setMessage(message);
                    }
                });
            }
        });
        this.setRunning(true);
    }
}

