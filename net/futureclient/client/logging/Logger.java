/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.logging;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public final class Logger {
    private static Logger logger = null;

    public void print(String message) {
        System.out.println(String.format("[%s] %s", "Exeter", message));
    }

    public void printToChat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("\u00a7c[%s] \u00a77%s", "Exeter", message.replace("&", "\u00a7"))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
    }

    public static Logger getLogger() {
        return logger == null ? (logger = new Logger()) : logger;
    }
}

