/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.InputEvent;
import me.friendly.exeter.friend.Friend;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public final class MiddleClickFriends
extends ToggleableModule {
    public MiddleClickFriends() {
        super("MiddleClick", new String[]{"middleclick", "mcf", "middleclick"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<InputEvent>("middle_click_input_listener"){

            @Override
            public void call(InputEvent event) {
                if (event.getType() == InputEvent.Type.MOUSE_MIDDLE_CLICK && ((MiddleClickFriends)MiddleClickFriends.this).minecraft.objectMouseOver != null && ((MiddleClickFriends)MiddleClickFriends.this).minecraft.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && ((MiddleClickFriends)MiddleClickFriends.this).minecraft.objectMouseOver.entityHit instanceof EntityPlayer) {
                    EntityPlayer entityPlayer = (EntityPlayer)((MiddleClickFriends)MiddleClickFriends.this).minecraft.objectMouseOver.entityHit;
                    if (Exeter.getInstance().getFriendManager().isFriend(entityPlayer.getName())) {
                        Exeter.getInstance().getFriendManager().unregister(Exeter.getInstance().getFriendManager().getFriendByAliasOrLabel(entityPlayer.getName()));
                    } else {
                        Exeter.getInstance().getFriendManager().register(new Friend(entityPlayer.getName(), entityPlayer.getName()));
                    }
                }
            }
        });
        this.setRunning(true);
    }
}

