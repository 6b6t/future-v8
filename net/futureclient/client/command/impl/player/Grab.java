/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.player;

import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public final class Grab
extends Command {
    public Grab() {
        super(new String[]{"grab", "grabip", "grabcoords"}, new Argument("ip|coords"));
    }

    @Override
    public String dispatch() {
        String type;
        switch (type = this.getArgument("ip|coords").getValue()) {
            case "ip": 
            case "i": {
                String address = this.minecraft.getCurrentServerData().serverIP;
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(address), null);
                break;
            }
            case "coords": 
            case "coord": 
            case "coordinates": 
            case "coordinate": 
            case "c": {
                String coords = String.format("X: %s, Y: %s, Z: %s", (int)this.minecraft.thePlayer.posX, (int)this.minecraft.thePlayer.posY, (int)this.minecraft.thePlayer.posZ);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(coords), null);
                break;
            }
            default: {
                return "Incorrect type.";
            }
        }
        return "Copied the selected type.";
    }
}

