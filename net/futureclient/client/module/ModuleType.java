/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module;

public enum ModuleType {
    COMBAT("Combat"),
    EXPLOITS("Exploits"),
    MISCELLANEOUS("Miscellaneous"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World");

    private final String label;

    private ModuleType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}

