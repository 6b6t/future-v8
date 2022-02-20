/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.friendly.exeter.core;

import java.io.File;
import java.io.IOException;
import me.friendly.api.event.basic.BasicEventManager;
import me.friendly.exeter.command.CommandManager;
import me.friendly.exeter.config.ConfigManager;
import me.friendly.exeter.friend.FriendManager;
import me.friendly.exeter.gui.screens.accountmanager.AccountManager;
import me.friendly.exeter.keybind.KeybindManager;
import me.friendly.exeter.logging.Logger;
import me.friendly.exeter.module.ModuleManager;
import me.friendly.exeter.plugin.PluginManager;
import org.lwjgl.opengl.Display;

public final class Exeter {
    private static Exeter instance = null;
    public static final String TITLE = "Exeter";
    public static final int BUILD = 23;
    public final long startTime = System.nanoTime() / 1000000L;
    private final BasicEventManager eventManager;
    private final KeybindManager keybindManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final FriendManager friendManager;
    private final ConfigManager configManager;
    private final AccountManager accountManager;
    private final PluginManager pluginManager;
    private final File directory;

    public Exeter() {
        Logger.getLogger().print("Initializing...");
        instance = this;
        this.directory = new File(System.getProperty("user.home"), "clarinet");
        if (!this.directory.exists()) {
            Logger.getLogger().print(String.format("%s client directory.", this.directory.mkdir() ? "Created" : "Failed to create"));
        }
        this.eventManager = new BasicEventManager();
        this.configManager = new ConfigManager();
        this.friendManager = new FriendManager();
        this.keybindManager = new KeybindManager();
        this.commandManager = new CommandManager();
        this.moduleManager = new ModuleManager();
        this.accountManager = new AccountManager();
        this.pluginManager = new PluginManager();
        this.getConfigManager().getRegistry().forEach(config -> config.load(new Object[0]));
        try {
            this.pluginManager.onLoad();
            System.out.println("Plugin manager started.");
            System.out.println(this.pluginManager.getList() + "has been loaded.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Thread"){

            @Override
            public void run() {
                Logger.getLogger().print("Shutting down...");
                Exeter.this.getConfigManager().getRegistry().forEach(config -> config.save(new Object[0]));
                Logger.getLogger().print("Shutdown.");
            }
        });
        Display.setTitle((String)String.format("%s b%s  ", TITLE, 23));
        Logger.getLogger().print(String.format("Initialized, took %s milliseconds.", System.nanoTime() / 1000000L - this.startTime));
    }

    public static Exeter getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public KeybindManager getKeybindManager() {
        return this.keybindManager;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public BasicEventManager getEventManager() {
        return this.eventManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public File getDirectory() {
        return this.directory;
    }
}

