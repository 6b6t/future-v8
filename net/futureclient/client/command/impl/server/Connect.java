/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.server;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public final class Connect
extends Command {
    public Connect() {
        super(new String[]{"connect", "c"}, new Argument("ip"));
    }

    @Override
    public String dispatch() {
        ServerData serverData = new ServerData("", this.getArgument("ip").getValue());
        this.minecraft.theWorld.sendQuittingDisconnectingPacket();
        this.minecraft.loadWorld(null);
        this.minecraft.displayGuiScreen(new GuiConnecting(null, this.minecraft, serverData));
        return "Connecting...";
    }
}

