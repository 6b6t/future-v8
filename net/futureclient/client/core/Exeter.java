/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.lwjgl.opengl.Display
 */
package com.gitlab.nuf.exeter.core;

import com.gitlab.nuf.api.event.basic.BasicEventManager;
import com.gitlab.nuf.exeter.command.CommandManager;
import com.gitlab.nuf.exeter.config.ConfigManager;
import com.gitlab.nuf.exeter.friend.FriendManager;
import com.gitlab.nuf.exeter.gui.screens.accountmanager.AccountManager;
import com.gitlab.nuf.exeter.keybind.KeybindManager;
import com.gitlab.nuf.exeter.logging.Logger;
import com.gitlab.nuf.exeter.module.ModuleManager;
import java.io.File;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.opengl.Display;

public final class Exeter {
    private static Exeter instance = null;
    public static final String TITLE = "Exeter";
    public static final int BUILD = 13;
    public final long startTime = System.nanoTime() / 1000000L;
    private final BasicEventManager eventManager;
    private final KeybindManager keybindManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final FriendManager friendManager;
    private final ConfigManager configManager;
    private final AccountManager accountManager;
    private final File directory;

    public Exeter() {
        String hash = "9dbec2d9e12dad273a9e8c9312a43befb55643f077053f88dec2a03666044755";
        if (!hash.equalsIgnoreCase(DigestUtils.sha256Hex((String)this.getWebsite()))) {
            System.exit(0);
        }
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
        this.getConfigManager().getRegistry().forEach(config -> config.load(new Object[0]));
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Thread"){

            @Override
            public void run() {
                Logger.getLogger().print("Shutting down...");
                Exeter.this.getConfigManager().getRegistry().forEach(config -> config.save(new Object[0]));
                Logger.getLogger().print("Shutdown.");
            }
        });
        Display.setTitle((String)String.format("%s v14 b%s", TITLE, 13));
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

    public File getDirectory() {
        return this.directory;
    }

    public String getWebsite() {
        return "http://imnuf.ml/";
    }
}

