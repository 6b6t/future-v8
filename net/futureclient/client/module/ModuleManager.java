/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.friendly.exeter.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import me.friendly.api.interfaces.Toggleable;
import me.friendly.api.registry.ListRegistry;
import me.friendly.exeter.config.Config;
import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.module.Module;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.module.impl.active.combat.AntiAim;
import me.friendly.exeter.module.impl.active.render.Render;
import me.friendly.exeter.module.impl.active.render.TextGUI;
import me.friendly.exeter.module.impl.toggle.combat.AutoArmor;
import me.friendly.exeter.module.impl.toggle.combat.AutoClicker;
import me.friendly.exeter.module.impl.toggle.combat.AutoHeal;
import me.friendly.exeter.module.impl.toggle.combat.BowAimbot;
import me.friendly.exeter.module.impl.toggle.combat.ClickAimbot;
import me.friendly.exeter.module.impl.toggle.combat.Criticals;
import me.friendly.exeter.module.impl.toggle.combat.KillAura;
import me.friendly.exeter.module.impl.toggle.combat.QuakeAimbot;
import me.friendly.exeter.module.impl.toggle.exploits.CivBreak;
import me.friendly.exeter.module.impl.toggle.exploits.FastUse;
import me.friendly.exeter.module.impl.toggle.exploits.InfiniteDurability;
import me.friendly.exeter.module.impl.toggle.exploits.ItemSpoof;
import me.friendly.exeter.module.impl.toggle.exploits.NoHunger;
import me.friendly.exeter.module.impl.toggle.exploits.QuickAttack;
import me.friendly.exeter.module.impl.toggle.exploits.Regen;
import me.friendly.exeter.module.impl.toggle.exploits.Zoot;
import me.friendly.exeter.module.impl.toggle.miscellaneous.AntiCommand;
import me.friendly.exeter.module.impl.toggle.miscellaneous.AutoAccept;
import me.friendly.exeter.module.impl.toggle.miscellaneous.Chest;
import me.friendly.exeter.module.impl.toggle.miscellaneous.Freecam;
import me.friendly.exeter.module.impl.toggle.miscellaneous.InventoryWalk;
import me.friendly.exeter.module.impl.toggle.miscellaneous.MiddleClickFriends;
import me.friendly.exeter.module.impl.toggle.miscellaneous.NoFall;
import me.friendly.exeter.module.impl.toggle.miscellaneous.PotionSaver;
import me.friendly.exeter.module.impl.toggle.miscellaneous.SkinFlash;
import me.friendly.exeter.module.impl.toggle.miscellaneous.Sneak;
import me.friendly.exeter.module.impl.toggle.miscellaneous.SpamBypass;
import me.friendly.exeter.module.impl.toggle.miscellaneous.XCarry;
import me.friendly.exeter.module.impl.toggle.movement.AntiVelocity;
import me.friendly.exeter.module.impl.toggle.movement.Blink;
import me.friendly.exeter.module.impl.toggle.movement.FastLadder;
import me.friendly.exeter.module.impl.toggle.movement.Flight;
import me.friendly.exeter.module.impl.toggle.movement.Glide;
import me.friendly.exeter.module.impl.toggle.movement.Jesus;
import me.friendly.exeter.module.impl.toggle.movement.LongJump;
import me.friendly.exeter.module.impl.toggle.movement.NoSlow;
import me.friendly.exeter.module.impl.toggle.movement.Speed;
import me.friendly.exeter.module.impl.toggle.movement.Sprint;
import me.friendly.exeter.module.impl.toggle.movement.Step;
import me.friendly.exeter.module.impl.toggle.render.ClickGui;
import me.friendly.exeter.module.impl.toggle.render.Fullbright;
import me.friendly.exeter.module.impl.toggle.render.NameProtect;
import me.friendly.exeter.module.impl.toggle.render.NameTags;
import me.friendly.exeter.module.impl.toggle.render.Search;
import me.friendly.exeter.module.impl.toggle.render.Seeker;
import me.friendly.exeter.module.impl.toggle.render.StorageESP;
import me.friendly.exeter.module.impl.toggle.render.TabGui;
import me.friendly.exeter.module.impl.toggle.render.Tracers;
import me.friendly.exeter.module.impl.toggle.render.Trails;
import me.friendly.exeter.module.impl.toggle.render.Wallhack;
import me.friendly.exeter.module.impl.toggle.render.Waypoints;
import me.friendly.exeter.module.impl.toggle.render.WorldeditESP;
import me.friendly.exeter.module.impl.toggle.world.AutoFarm;
import me.friendly.exeter.module.impl.toggle.world.Avoid;
import me.friendly.exeter.module.impl.toggle.world.FastPlace;
import me.friendly.exeter.module.impl.toggle.world.Phase;
import me.friendly.exeter.module.impl.toggle.world.SafeWalk;
import me.friendly.exeter.module.impl.toggle.world.Speedmine;

public final class ModuleManager
extends ListRegistry<Module> {
    public ModuleManager() {
        this.registry = new ArrayList();
        this.register(new TextGUI());
        this.register(new KillAura());
        this.register(new AntiAim());
        this.register(new Fullbright());
        this.register(new Criticals());
        this.register(new NoFall());
        this.register(new PotionSaver());
        this.register(new ClickAimbot());
        this.register(new LongJump());
        this.register(new Glide());
        this.register(new Flight());
        this.register(new Zoot());
        this.register(new BowAimbot());
        this.register(new Search());
        this.register(new InfiniteDurability());
        this.register(new Regen());
        this.register(new Speedmine());
        this.register(new Sprint());
        this.register(new Step());
        this.register(new NameProtect());
        this.register(new MiddleClickFriends());
        this.register(new FastPlace());
        this.register(new NameTags());
        this.register(new AutoHeal());
        this.register(new Jesus());
        this.register(new InventoryWalk());
        this.register(new AutoArmor());
        this.register(new AntiCommand());
        this.register(new AutoAccept());
        this.register(new Speed());
        this.register(new ClickGui());
        this.register(new FastLadder());
        this.register(new Blink());
        this.register(new SafeWalk());
        this.register(new TabGui());
        this.register(new Chest());
        this.register(new SpamBypass());
        this.register(new XCarry());
        this.register(new Phase());
        this.register(new Freecam());
        this.register(new AntiVelocity());
        this.register(new NoHunger());
        this.register(new NoSlow());
        this.register(new Tracers());
        this.register(new FastUse());
        this.register(new Sneak());
        this.register(new StorageESP());
        this.register(new Render());
        this.register(new Wallhack());
        this.register(new QuickAttack());
        this.register(new AutoClicker());
        this.register(new Seeker());
        this.register(new WorldeditESP());
        this.register(new SkinFlash());
        this.register(new Trails());
        this.register(new Waypoints());
        this.register(new Avoid());
        this.register(new AutoFarm());
        this.register(new ItemSpoof());
        this.register(new QuakeAimbot());
        this.register(new CivBreak());
        this.registry.sort((mod1, mod2) -> mod1.getLabel().compareTo(mod2.getLabel()));
        Exeter.getInstance().getKeybindManager().getKeybindByLabel("Click Gui").setKey(54);
        new Config("module_configurations.json"){

            @Override
            public void load(Object ... source) {
                try {
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                }
                File modDirectory = new File(Exeter.getInstance().getDirectory(), "modules");
                if (!modDirectory.exists()) {
                    modDirectory.mkdir();
                }
                Exeter.getInstance().getModuleManager().getRegistry().forEach(mod -> {
                    File file = new File(modDirectory, mod.getLabel().toLowerCase().replaceAll(" ", "") + ".json");
                    if (!file.exists()) {
                        return;
                    }
                    try (FileReader reader = new FileReader(file);){
                        JsonElement node = new JsonParser().parse((Reader)reader);
                        if (!node.isJsonObject()) {
                            return;
                        }
                        mod.loadConfig(node.getAsJsonObject());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                this.loadConfig();
            }

            @Override
            public void save(Object ... destination) {
                try {
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (!this.getFile().exists()) {
                    return;
                }
                Exeter.getInstance().getModuleManager().getRegistry().forEach(Module::saveConfig);
                this.saveConfig();
            }

            private void loadConfig() {
                JsonElement root;
                File modsFile = new File(this.getFile().getAbsolutePath());
                if (!modsFile.exists()) {
                    return;
                }
                try (FileReader reader = new FileReader(modsFile);){
                    root = new JsonParser().parse((Reader)reader);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (!(root instanceof JsonArray)) {
                    return;
                }
                JsonArray mods = (JsonArray)root;
                mods.forEach(node -> {
                    if (!(node instanceof JsonObject)) {
                        return;
                    }
                    try {
                        JsonObject modNode = (JsonObject)node;
                        Exeter.getInstance().getModuleManager().getRegistry().forEach(mod -> {
                            if (mod.getLabel().equalsIgnoreCase(modNode.get("module-label").getAsString()) && mod instanceof Toggleable) {
                                ToggleableModule toggleableModule = (ToggleableModule)mod;
                                if (modNode.get("module-state").getAsBoolean()) {
                                    toggleableModule.setRunning(true);
                                }
                                toggleableModule.setDrawn(modNode.get("module-drawn").getAsBoolean());
                                Exeter.getInstance().getKeybindManager().getKeybindByLabel(toggleableModule.getLabel()).setKey(modNode.get("module-keybind").getAsInt());
                            }
                        });
                    }
                    catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
            }

            private void saveConfig() {
                File modsFile = new File(this.getFile().getAbsolutePath());
                if (modsFile.exists()) {
                    modsFile.delete();
                }
                if (Exeter.getInstance().getModuleManager().getRegistry().isEmpty()) {
                    return;
                }
                JsonArray mods = new JsonArray();
                Exeter.getInstance().getModuleManager().getRegistry().forEach(mod -> {
                    try {
                        JsonObject modObject = new JsonObject();
                        modObject.addProperty("module-label", mod.getLabel());
                        if (mod instanceof Toggleable) {
                            ToggleableModule toggleableModule = (ToggleableModule)mod;
                            modObject.addProperty("module-state", Boolean.valueOf(toggleableModule.isRunning()));
                            modObject.addProperty("module-drawn", Boolean.valueOf(toggleableModule.isDrawn()));
                            modObject.addProperty("module-keybind", (Number)Exeter.getInstance().getKeybindManager().getKeybindByLabel(toggleableModule.getLabel()).getKey());
                        }
                        mods.add((JsonElement)modObject);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try (FileWriter writer = new FileWriter(modsFile);){
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)mods));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public Module getModuleByAlias(String alias) {
        for (Module module : this.registry) {
            for (String moduleAlias : module.getAliases()) {
                if (!alias.equalsIgnoreCase(moduleAlias)) continue;
                return module;
            }
        }
        return null;
    }
}

