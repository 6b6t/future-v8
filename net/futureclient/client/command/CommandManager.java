/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.registry.ListRegistry;
import com.gitlab.nuf.exeter.command.Command;
import com.gitlab.nuf.exeter.command.impl.client.Bind;
import com.gitlab.nuf.exeter.command.impl.client.Friends;
import com.gitlab.nuf.exeter.command.impl.client.Help;
import com.gitlab.nuf.exeter.command.impl.client.Modules;
import com.gitlab.nuf.exeter.command.impl.client.Prefix;
import com.gitlab.nuf.exeter.command.impl.client.Presets;
import com.gitlab.nuf.exeter.command.impl.client.Runtime;
import com.gitlab.nuf.exeter.command.impl.client.Toggle;
import com.gitlab.nuf.exeter.command.impl.player.Breed;
import com.gitlab.nuf.exeter.command.impl.player.Damage;
import com.gitlab.nuf.exeter.command.impl.player.Drown;
import com.gitlab.nuf.exeter.command.impl.player.Grab;
import com.gitlab.nuf.exeter.command.impl.player.HClip;
import com.gitlab.nuf.exeter.command.impl.player.StackSize;
import com.gitlab.nuf.exeter.command.impl.player.VClip;
import com.gitlab.nuf.exeter.command.impl.server.Connect;
import com.gitlab.nuf.exeter.command.impl.server.Crash;
import com.gitlab.nuf.exeter.config.Config;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PacketEvent;
import com.gitlab.nuf.exeter.logging.Logger;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.Property;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import net.minecraft.network.play.client.C01PacketChatMessage;

public final class CommandManager
extends ListRegistry<Command> {
    private String prefix = ".";

    public CommandManager() {
        this.registry = new ArrayList();
        this.register(new Toggle());
        this.register(new Runtime());
        this.register(new Grab());
        this.register(new Help());
        this.register(new Modules());
        this.register(new Breed());
        this.register(new Prefix());
        this.register(new Connect());
        this.register(new Crash());
        this.register(new StackSize());
        this.register(new Damage());
        this.register(new Presets());
        this.register(new HClip());
        this.register(new Drown());
        this.register(new VClip());
        this.register(new Friends.Add());
        this.register(new Friends.Remove());
        this.register(new Bind());
        this.registry.sort((cmd1, cmd2) -> cmd1.getAliases()[0].compareTo(cmd2.getAliases()[0]));
        Exeter.getInstance().getEventManager().register(new Listener<PacketEvent>("commands_packet_listener"){

            @Override
            public void call(PacketEvent event) {
                C01PacketChatMessage packet;
                String message;
                if (event.getPacket() instanceof C01PacketChatMessage && (message = (packet = (C01PacketChatMessage)event.getPacket()).getMessage().trim()).startsWith(CommandManager.this.getPrefix())) {
                    event.setCanceled(true);
                    boolean exists = false;
                    String[] arguments = message.split(" ");
                    if (message.length() < 1) {
                        Logger.getLogger().printToChat("No command was entered.");
                        return;
                    }
                    String execute = message.contains(" ") ? arguments[0] : message;
                    for (Command command : CommandManager.this.getRegistry()) {
                        for (String alias : command.getAliases()) {
                            if (!execute.replace(CommandManager.this.getPrefix(), "").equalsIgnoreCase(alias.replaceAll(" ", ""))) continue;
                            exists = true;
                            try {
                                Logger.getLogger().printToChat(command.dispatch(arguments));
                            }
                            catch (Exception e2) {
                                Logger.getLogger().printToChat(String.format("%s%s %s", CommandManager.this.getPrefix(), alias, command.getSyntax()));
                            }
                        }
                    }
                    String[] argz = message.split(" ");
                    for (Module mod : Exeter.getInstance().getModuleManager().getRegistry()) {
                        for (String alias : mod.getAliases()) {
                            try {
                                if (!argz[0].equalsIgnoreCase(CommandManager.this.getPrefix() + alias.replace(" ", ""))) continue;
                                exists = true;
                                if (argz.length > 1) {
                                    String valueName = argz[1];
                                    if (argz[1].equalsIgnoreCase("list")) {
                                        if (mod.getProperties().size() > 0) {
                                            StringJoiner stringJoiner = new StringJoiner(", ");
                                            for (Property property : mod.getProperties()) {
                                                stringJoiner.add(String.format("%s&e[%s]&7", property.getAliases()[0], property.getValue() instanceof Enum ? ((EnumProperty)property).getFixedValue() : property.getValue()));
                                            }
                                            Logger.getLogger().printToChat(String.format("Properties (%s) %s.", mod.getProperties().size(), stringJoiner.toString()));
                                            continue;
                                        }
                                        Logger.getLogger().printToChat(String.format("&e%s&7 has no properties.", mod.getLabel()));
                                        continue;
                                    }
                                    Property property = mod.getPropertyByAlias(valueName);
                                    if (property == null) continue;
                                    if (property.getValue() instanceof Number) {
                                        if (!argz[2].equalsIgnoreCase("get")) {
                                            if (property.getValue() instanceof Double) {
                                                property.setValue(Double.parseDouble(argz[2]));
                                            }
                                            if (property.getValue() instanceof Integer) {
                                                property.setValue(Integer.parseInt(argz[2]));
                                            }
                                            if (property.getValue() instanceof Float) {
                                                property.setValue(Float.valueOf(Float.parseFloat(argz[2])));
                                            }
                                            if (property.getValue() instanceof Long) {
                                                property.setValue(Long.parseLong(argz[2]));
                                            }
                                            Logger.getLogger().printToChat(String.format("&e%s&7 has been set to &e%s&7 for &e%s&7.", property.getAliases()[0], property.getValue(), mod.getLabel()));
                                            continue;
                                        }
                                        Logger.getLogger().printToChat(String.format("&e%s&7 current value is &e%s&7 for &e%s&7.", property.getAliases()[0], property.getValue(), mod.getLabel()));
                                        continue;
                                    }
                                    if (property.getValue() instanceof Enum) {
                                        if (!argz[2].equalsIgnoreCase("list")) {
                                            ((EnumProperty)property).setValue(argz[2]);
                                            Logger.getLogger().printToChat(String.format("&e%s&7 has been set to &e%s&7 for &e%s&7.", property.getAliases()[0], ((EnumProperty)property).getFixedValue(), mod.getLabel()));
                                            continue;
                                        }
                                        StringJoiner stringJoiner = new StringJoiner(", ");
                                        Enum[] array = (Enum[])property.getValue().getClass().getEnumConstants();
                                        int length = array.length;
                                        for (int i2 = 0; i2 < length; ++i2) {
                                            stringJoiner.add(String.format("%s%s&7", array[i2].name().equalsIgnoreCase(property.getValue().toString()) ? "&a" : "&c", CommandManager.this.getFixedValue(array[i2])));
                                        }
                                        Logger.getLogger().printToChat(String.format("Modes (%s) %s.", array.length, stringJoiner.toString()));
                                        continue;
                                    }
                                    if (property.getValue() instanceof String) {
                                        property.setValue(argz[2]);
                                        Logger.getLogger().printToChat(String.format("&e%s&7 has been set to &e\"%s\"&7 for &e%s&7.", property.getAliases()[0], property.getValue(), mod.getLabel()));
                                        continue;
                                    }
                                    if (!(property.getValue() instanceof Boolean)) continue;
                                    property.setValue((Boolean)property.getValue() == false);
                                    Logger.getLogger().printToChat(String.format("&e%s&7 has been %s&7 for &e%s&7.", property.getAliases()[0], (Boolean)property.getValue() != false ? "&aenabled" : "&cdisabled", mod.getLabel()));
                                    continue;
                                }
                                Logger.getLogger().printToChat(String.format("%s &e[list|valuename] [list|get]", argz[0]));
                            }
                            catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                    if (!exists) {
                        Logger.getLogger().printToChat("Invalid command entered.");
                    }
                }
            }
        });
        new Config("command_prefix.txt"){

            @Override
            public void load(Object ... source) {
                try {
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (!this.getFile().exists()) {
                    return;
                }
                try {
                    String readLine;
                    BufferedReader br2 = new BufferedReader(new FileReader(this.getFile()));
                    while ((readLine = br2.readLine()) != null) {
                        try {
                            String[] split = readLine.split(":");
                            CommandManager.this.prefix = split[0];
                        }
                        catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    br2.close();
                }
                catch (Exception e4) {
                    e4.printStackTrace();
                }
            }

            @Override
            public void save(Object ... destination) {
                try {
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                try {
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(this.getFile()));
                    bw2.write(CommandManager.this.prefix);
                    bw2.newLine();
                    bw2.close();
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        };
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String getFixedValue(Enum enumd) {
        return Character.toString(enumd.name().charAt(0)) + enumd.name().toLowerCase().replace(Character.toString(enumd.name().charAt(0)).toLowerCase(), "");
    }
}

