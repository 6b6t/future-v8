/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.config;

import com.gitlab.nuf.api.registry.ListRegistry;
import com.gitlab.nuf.exeter.config.Config;
import java.util.ArrayList;

public final class ConfigManager
extends ListRegistry<Config> {
    public ConfigManager() {
        this.registry = new ArrayList();
    }
}

