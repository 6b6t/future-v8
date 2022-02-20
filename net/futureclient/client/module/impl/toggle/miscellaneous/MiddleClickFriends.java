/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.miscellaneous;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.InputEvent;
import com.gitlab.nuf.exeter.friend.Friend;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public final class MiddleClickFriends
extends ToggleableModule {
    public MiddleClickFriends() {
        super("Middle Click", new String[]{"middleclick", "mcf", "middleclick"}, ModuleType.MISCELLANEOUS);
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

