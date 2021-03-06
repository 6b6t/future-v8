/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.command.impl.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.friendly.api.stopwatch.Stopwatch;
import me.friendly.exeter.command.Argument;
import me.friendly.exeter.command.Command;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;

public class Ban
extends Command {
    private Queue<String> players = new ConcurrentLinkedQueue<String>();
    private final Stopwatch stopwatch = new Stopwatch();

    public Ban() {
        super(new String[]{"ban"}, new Argument("get|list|clear|all"));
    }

    @Override
    public String dispatch() {
        switch (this.getArgument("get|list|clear|all").getValue()) {
            case "get": {
                if (this.minecraft.thePlayer != null) {
                    System.out.println(this.players);
                    for (Object o : this.minecraft.getNetHandler().func_175106_d()) {
                        NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)o;
                        String mcname = StringUtils.stripControlCodes(((NetworkPlayerInfo)o).func_178845_a().getName());
                        if (mcname.equalsIgnoreCase(this.minecraft.thePlayer.getName())) continue;
                        this.players.add(mcname);
                    }
                }
                return "List collected.";
            }
            case "all": {
                String player = (String)this.players.iterator().next();
                if (this.stopwatch.hasCompleted(600L)) {
                    for (int index = 0; index < 2; ++index) {
                        this.minecraft.thePlayer.sendChatMessage("Hi " + player);
                        this.players.remove(player);
                    }
                }
                return "Banned " + this.players.size() + " players!";
            }
            case "clear": {
                this.players.clear();
                return "Ban list cleared!";
            }
            case "list": {
                return String.valueOf(this.players);
            }
        }
        return null;
    }
}

