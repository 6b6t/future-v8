/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.miscellaneous;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.MotionUpdateEvent;
import me.friendly.exeter.events.PacketEvent;
import me.friendly.exeter.logging.Logger;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;

public class Murder
extends ToggleableModule {
    private static final String MINIGAME_BEGIN = "your secret identity is";
    private static final String[] MINIGAME_END = new String[]{"murderer was", "murderer has been", "you were killed", "has killed everyone"};
    private static final String MINIGAME_RESET = "murderer has left";
    private EntityPlayer murderer;
    private boolean looking;

    public Murder() {
        super("Murder", new String[]{"murder"}, ModuleType.MISCELLANEOUS);
        this.listeners.add(new Listener<MotionUpdateEvent>("murderer_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                if (Murder.this.murderer != null || !Murder.this.looking) {
                    return;
                }
                EntityPlayerSP player = ((Murder)Murder.this).minecraft.thePlayer;
                for (Object object : player.getEntityWorld().playerEntities) {
                    EntityPlayer target = (EntityPlayer)object;
                    if (target == player || !target.isEntityAlive() || !Murder.this.isMurderer(target)) continue;
                    Murder.this.murderer = target;
                    Murder.this.looking = false;
                    Logger.getLogger().printToChat(((Murder)Murder.this).minecraft.thePlayer + "The murderer is \u00a7c%s\u00a77." + target.getCommandSenderEntity());
                    break;
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent>("murderer_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                S02PacketChat packet = (S02PacketChat)event.getPacket();
                String message = StringUtils.stripControlCodes(packet.getChatComponent().getUnformattedText()).toLowerCase();
                if (message.contains(Murder.MINIGAME_BEGIN)) {
                    Murder.this.murderer = null;
                    Murder.this.looking = true;
                    return;
                }
                if (!message.startsWith("murder")) {
                    return;
                }
                if (message.contains(Murder.MINIGAME_RESET)) {
                    Murder.this.murderer = null;
                    Murder.this.looking = true;
                    return;
                }
                for (String ending : MINIGAME_END) {
                    if (!message.contains(ending)) continue;
                    Murder.this.murderer = null;
                    Murder.this.looking = false;
                }
            }
        });
    }

    private boolean isMurderer(EntityPlayer player) {
        ItemStack itemStack;
        if (player.isSprinting()) {
            return true;
        }
        return player.getHeldItem() != null && (itemStack = player.getHeldItem()).getItem() instanceof ItemSword;
    }
}

